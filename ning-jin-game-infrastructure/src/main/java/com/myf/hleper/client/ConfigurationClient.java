package com.myf.hleper.client;

import com.myf.zouding.database.driver.ConfigurationRepo;
import com.myf.zouding.database.entity.ConfigurationDO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: myf
 * @CreateTime: 2024-06-23  15:15
 * @Description: ConfigurationClient
 */
@Component
public class ConfigurationClient {

    public static Map<String, String> configMap = new ConcurrentHashMap<>(12);

    /**
     * 腾讯邮箱用户名
     */
    public static final String TENCENT_MAIL_USER_NAME = "tencentMailUserName";

    /**
     * 腾讯邮箱密码
     */
    public static final String TENCENT_MAIL_PASSWORD = "tencentMailPassword";


    @Autowired
    private ConfigurationRepo configurationRepo;

    @PostConstruct
    private void init() {
        List<ConfigurationDO> configurationList = configurationRepo.selectList();
        if (CollectionUtils.isNotEmpty(configurationList)) {
            for (ConfigurationDO configurationDO : configurationList) {
                configMap.put(configurationDO.getKeyStr(), configurationDO.getValueStr());
            }
        }
    }

    public String getValueByKey(String key) {
        return configMap.get(key);
    }
}
