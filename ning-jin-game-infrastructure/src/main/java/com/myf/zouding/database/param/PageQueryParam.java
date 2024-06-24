package com.myf.zouding.database.param;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2024-06-22  14:34
 * @Description: PageQueryParam
 */
@Data
public class PageQueryParam {
    private int pageNumber;
    /**
     * pageSize范围
     */
    private int pageSize;

    private int totalPage;


}
