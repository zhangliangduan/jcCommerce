package com.xunmeng.jccommerce.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xunmeng.jccommerce.dto.page.PageVo;


import java.util.List;
import java.util.stream.Collectors;

/**
 * PageVo通用操作
 *
 * @author ltm
 * @since 2023/11/27
 */
@SuppressWarnings("unused")
public class PageVoUtil {

    /**
     * 获取分页信息
     *
     * @param page 分页对象
     * @param <T> 泛型类型
     * @return 分页视图对象
     */
    public static <T, R> PageVo<R> getPageVo(Page<T> page) {
        PageVo<R> pageVo = new PageVo<>();
        pageVo.setCurrent(page.getCurrent());
        pageVo.setSize(page.getSize());
        pageVo.setTotal(page.getTotal());
        return pageVo;
    }

    /**
     * 根据给定的结果列表、当前页码和每页显示数量，生成公司信息的分页对象
     * @param resultList 结果列表
     * @param current 当前页码
     * @param size 每页显示数量
     * @return 分页对象
     */
    public static <T> PageVo<T> getSpecificPageVo(List<T> resultList, Long current, Long size) {
        // 计算开始的索引
        int start = (current.intValue() - 1) * size.intValue();

        // 使用流进行分页
        List<T> pageList = resultList.stream()
                .skip(start)  // 跳过前面的元素
                .limit(size)  // 限制数量
                .collect(Collectors.toList());  // 转换为列表

        // 创建分页对象
        PageVo<T> pageVo = new PageVo<>();
        pageVo.setCurrent(current);
        pageVo.setSize(size);
        pageVo.setTotal((long) resultList.size());
        pageVo.setRecords(pageList);
        return pageVo;
    }

}
