package com.mapper.core.wx.mp;

import com.mapper.core.util.RedisDistributeLock;
import me.chanjar.weixin.common.WxType;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.util.UUID;
/**
 * token获取加入分布式锁实现
 * @author mapper
 * @since  2019-06-06
 */
public class WxMpServiceImpl extends me.chanjar.weixin.mp.api.impl.WxMpServiceImpl{

    public static final String REDIS_LOCK_PRIFIX = "distributeLock:wx:mp:";

    @Override
    public String getAccessToken(boolean forceRefresh) throws WxErrorException {
        if (!this.getWxMpConfigStorage().isAccessTokenExpired() && !forceRefresh) {
            return this.getWxMpConfigStorage().getAccessToken();
        }
        //此处仍然有潜在风险，如刚巧到此位置时，其它服务也刚巧走完下面的方法，已经刷新token，此时会导致再次刷新token，从而引起之前token失效问题
        String appId = this.getWxMpConfigStorage().getAppId();
        String randomValue = UUID.randomUUID().toString();
        //设置分布式锁，过期时间5s
        boolean setLock = RedisDistributeLock.setLock(REDIS_LOCK_PRIFIX + appId, randomValue, 50);
        if(setLock) {
            try {
                String url = String.format(WxMpService.GET_ACCESS_TOKEN_URL, this.getWxMpConfigStorage().getAppId(),
                        this.getWxMpConfigStorage().getSecret());
                HttpGet httpGet = new HttpGet(url);
                if (this.getRequestHttpProxy() != null) {
                    RequestConfig config = RequestConfig.custom().setProxy(this.getRequestHttpProxy()).build();
                    httpGet.setConfig(config);
                }
                try (CloseableHttpResponse response = getRequestHttpClient().execute(httpGet)) {
                    String resultContent = new BasicResponseHandler().handleResponse(response);
                    WxError error = WxError.fromJson(resultContent, WxType.MP);
                    if (error.getErrorCode() != 0) {
                        throw new WxErrorException(error);
                    }
                    WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
                    this.getWxMpConfigStorage().updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
                    return this.getWxMpConfigStorage().getAccessToken();
                } finally {
                    httpGet.releaseConnection();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                //释放锁
                RedisDistributeLock.releaseLock(REDIS_LOCK_PRIFIX + appId, randomValue);
            }
        }else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return this.getAccessToken(forceRefresh);
        }

    }
}
