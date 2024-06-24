package com.myf.zouding.database.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.myf.common.util.DateUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-12-17  17:05
 * @Description: TODO
 */
@Component
public class MetaObjectHandlerImpl implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = metaObject.getValue("createTime");
        if (Objects.isNull(createTime)) {
            metaObject.setValue("createTime", DateUtils.stringToDate(DateUtils.dateToString(new Date(), null), null));
        }
        metaObject.setValue("updateTime", DateUtils.stringToDate(DateUtils.dateToString(new Date(), null), null));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", DateUtils.stringToDate(DateUtils.dateToString(new Date(), null), null));
    }
}
