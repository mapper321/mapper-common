package com.mapper.core.wx.ma;

import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import lombok.Data;
import me.chanjar.weixin.mp.enums.TicketType;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Data
public class WxMaInRedisConfigStorage extends WxMaInMemoryConfig {

    private static final String ACCESS_TOKEN_KEY = "wx:ma:access_token:";

    private RedisTemplate<String, Object> redisTemplate;

    private String accessTokenKey;

    private String appType;

    public WxMaInRedisConfigStorage(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 每个小程序生成独有的存储key.
     */
    @Override
    public void setAppid(String appid) {
        super.setAppid(appid);
        this.accessTokenKey = ACCESS_TOKEN_KEY.concat(appid);
    }

    @Override
    public String getAccessToken() {
        return redisTemplate.opsForValue().get(this.accessTokenKey).toString();
    }


    @Override
    public boolean isAccessTokenExpired() {
        return redisTemplate.getExpire(accessTokenKey) < 2;
    }

    @Override
    public void expireAccessToken() {
        redisTemplate.expire(this.accessTokenKey, 0, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        redisTemplate.opsForValue().set(this.accessTokenKey, accessToken);
        redisTemplate.expire(this.accessTokenKey, expiresInSeconds - 200, TimeUnit.SECONDS);
    }

    private String getTicketRedisKey(TicketType type) {
        return String.format("wx:mp:ticket:key:%s:%s", this.appid, type.getCode());
    }
}