package com.cykj.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageResult {
    private int pageNum; // 页码
    private int pageSize; // 页面大小
    private long total; // 总条数
    private List<Object> list = new ArrayList<>();
}
