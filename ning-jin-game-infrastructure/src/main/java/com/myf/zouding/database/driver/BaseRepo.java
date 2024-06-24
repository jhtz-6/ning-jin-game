package com.myf.zouding.database.driver;

import com.myf.zouding.database.entity.BaseDO;

import java.io.Serializable;

/**
 * @author myf
 */
public interface BaseRepo<T extends BaseDO> {

    /**
     * 保存数据
     *
     * @param t
     * @return
     */
    int save(T t);

    /**
     * 更新数据
     *
     * @param t
     * @return
     */
    int updateById(T t);

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    T selectById(Serializable id);

    /**
     * 根据id删除数据
     * @param id
     * @return
     */
    int deleteById(Serializable id);
}
