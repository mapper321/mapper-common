package com.mapper.core.wx.ma;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.mapper.core.util.RedisDistributeLock;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.util.UUID;
public class WxMaServiceImpl extends cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl{

    public static final String REDIS_LOCK_PRIFIX = "distributeLock:wx:ma:";

    @Override
    public String getAccessToken(boolean forceRefresh) throws WxErrorException {
        if (!this.getWxMaConfig().isAccessTokenExpired() && !forceRefresh) {
            return this.getWxMaConfig().getAccessToken();
        }
        // 此处仍然有潜在风险，如刚巧到此位置时，其它服务也刚巧走完下面的方法，已经刷新token，此时会导致再次刷新token，从而引起之前token失效问题
        String appId = this.getWxMaConfig().getAppid();
        String randomValue = UUID.randomUUID().toString();
        // 设置分布式锁，过期时间5s
        boolean setLock = RedisDistributeLock.setLock(REDIS_LOCK_PRIFIX + appId, randomValue, 50);
        if (setLock) {
            String url = String.format(WxMaService.GET_ACCESS_TOKEN_URL, this.getWxMaConfig().getAppid(),
                    this.getWxMaConfig().getSecret());
            try {
                HttpGet httpGet = new HttpGet(url);
                if (this.getRequestHttpProxy() != null) {
                    RequestConfig config = RequestConfig.custom().setProxy(this.getRequestHttpProxy()).build();
                    httpGet.setConfig(config);
                }
                try (CloseableHttpResponse response = getRequestHttpClient().execute(httpGet)) {
                    String resultContent = new BasicResponseHandler().handleResponse(response);
                    WxError error = WxError.fromJson(resultContent);
                    if (error.getErrorCode() != 0) {
                        throw new WxErrorException(error);
                    }
                    WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
                    this.getWxMaConfig().updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());

                    return this.getWxMaConfig().getAccessToken();
                } finally {
                    httpGet.releaseConnection();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                RedisDistributeLock.releaseLock(REDIS_LOCK_PRIFIX + appId, randomValue);
            }
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return this.getAccessToken(forceRefresh);
        }
    }
}
