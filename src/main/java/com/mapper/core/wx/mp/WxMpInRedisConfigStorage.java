package com.mapper.core.wx.mp;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.enums.TicketType;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
/**
 * 基于Redis的微信配置provider.
 * @author mapper
 */
public class WxMpInRedisConfigStorage extends WxMpInMemoryConfigStorage{
    private static final String ACCESS_TOKEN_KEY = "wx:mp:access_token:";

    /**
     * 使用连接池保证线程安全.
     */
    private RedisTemplate<String, Object> redisTemplate;

    private String accessTokenKey;

    public WxMpInRedisConfigStorage(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 每个公众号生成独有的存储key.
     */
    @Override
    public void setAppId(String appId) {
        super.setAppId(appId);
        this.accessTokenKey = ACCESS_TOKEN_KEY.concat(appId);
    }

    private String getTicketRedisKey(TicketType type) {
        return String.format("wx:mp:ticket:key:%s:%s", this.appId, type.getCode());
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
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        redisTemplate.opsForValue().set(this.accessTokenKey, accessToken);
        redisTemplate.expire(this.accessTokenKey, expiresInSeconds - 200, TimeUnit.SECONDS);
    }

    @Override
    public void expireAccessToken() {
        redisTemplate.expire(this.accessTokenKey, 0, TimeUnit.SECONDS);
    }

    @Override
    public String getTicket(TicketType type) {
        return redisTemplate.opsForValue().get(this.getTicketRedisKey(type)).toString();
    }

    @Override
    public boolean isTicketExpired(TicketType type) {
        return redisTemplate.getExpire(this.getTicketRedisKey(type)) < 2;
    }

    @Override
    public synchronized void updateTicket(TicketType type, String jsapiTicket, int expiresInSeconds) {
        redisTemplate.opsForValue().set(this.getTicketRedisKey(type), jsapiTicket);
        redisTemplate.expire(this.getTicketRedisKey(type), expiresInSeconds - 200, TimeUnit.SECONDS);
    }

    @Override
    public void expireTicket(TicketType type) {
        redisTemplate.expire(this.getTicketRedisKey(type), 0, TimeUnit.SECONDS);
    }
}
