package com.myf.hleper.client;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: myf
 * @CreateTime: 2023-12-17  21:34
 * @Description: RedisClient
 */
@Component
public class RedisClient {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void addValue(String setKey, String value) {
        if (StringUtils.isBlank(setKey) || StringUtils.isBlank(value)) {
            return ;
        } else {
            stringRedisTemplate.opsForValue().set(setKey, value);
        }
    }

    public String addValueToRedis(String setKey, String value, Long timeOut, TimeUnit timeUnit) {
        if (StringUtils.isBlank(setKey) || StringUtils.isBlank(value)) {
            return null;
        } else {
            if (Objects.isNull(timeOut) || Objects.isNull(timeUnit)) {
                stringRedisTemplate.opsForValue().set(setKey, value);
            } else {
                stringRedisTemplate.opsForValue().set(setKey, value, timeOut, timeUnit);
            }
            return stringRedisTemplate.opsForValue().get(setKey);
        }
    }

    public Set<String> getAllKeys(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return Sets.newHashSet();
        }
        return stringRedisTemplate.keys(pattern);
    }

    public String getValueByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        String forValue = stringRedisTemplate.opsForValue().get(key);
        return StringUtils.isEmpty(forValue) ? "" : forValue;
    }

    public Boolean deleteValueByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return stringRedisTemplate.delete(key);
    }

    public List<String> addStrValueToLimitedList(String key, String value, int size) {
        Long listSize = stringRedisTemplate.opsForList().size(key);
        if (listSize == size) {
            stringRedisTemplate.opsForList().leftPop(key);
        }
        stringRedisTemplate.opsForList().rightPush(key, value);
        return stringRedisTemplate.boundListOps(key).range(0, size);
    }

    public List<String> getListValueByKey(String key, int size) {
        if (StringUtils.isBlank(key)) {
            return Collections.emptyList();
        }
        return stringRedisTemplate.boundListOps(key).range(0, size);
    }
}
