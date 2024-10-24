package com.cykj.utils;

import com.github.pagehelper.PageInfo;

public class PageUtil {
    private static PageResult pageResult = new PageResult();
    public static PageResult initPageResult(PageInfo<Object> info){
        pageResult.setPageNum(info.getPageNum());
        pageResult.setPageSize(info.getPageSize());
        pageResult.setTotal(info.getTotal());
        pageResult.setList(info.getList());
        return pageResult;
    }
}
