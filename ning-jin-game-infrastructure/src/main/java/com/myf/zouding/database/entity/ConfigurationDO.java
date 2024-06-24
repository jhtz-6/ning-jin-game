package com.myf.zouding.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * @author myf
 */
@Data
@TableName("configuration")
public class ConfigurationDO extends BaseDO {

    @TableField("key_str")
    private String keyStr;

    @TableField("value_str")
    private String valueStr;
}
