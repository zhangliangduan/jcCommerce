package com.xunmeng.jccommerce.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.xunmeng.jccommerce.config.EasyExcelLocalDateTimeConverter;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 导出excel工具类
 *
 * @author 金多虾
 * @since 2024-05-27 14:07
 */
@Log4j2
@SuppressWarnings("unused")
public class ExportUtil {

    private static final String basePath = "/log/download/";

    public static <T> void exportExcel(List<?> dataList, Class<T> head, String filePath) {
        EasyExcel.write(filePath, head)
                .registerConverter(new EasyExcelLocalDateTimeConverter())
                .sheet("sheet1")
                .doWrite(dataList);
    }

    /**
     * 生成文件目录 带前缀 后缀
     *
     * @param filenamePrefix 文件前缀
     * @param suffix         文件后缀
     * @return key path value filename
     */
    public static Pair<String, String> generateFileWithPrefixAndSuffix(String filenamePrefix, String suffix) {
        if (StrUtil.isNotEmpty(filenamePrefix)) {
            filenamePrefix = filenamePrefix + "-";
        }
        LocalDateTime now = LocalDateTime.now();
        String path = now.format(DatePattern.PURE_DATE_FORMATTER) + "/";
        String fileName = filenamePrefix + now.format(DatePattern.PURE_DATETIME_FORMATTER) + suffix;
        String absolutePath = System.getProperty("user.dir");
        ExportUtil.isExistDir(absolutePath + basePath + path);
        return new Pair<>(absolutePath + basePath + path, fileName); //不能编码
    }

    /**
     * 判断多级路径是否存在，不存在就创建
     *
     * @param filePath 支持带文件名的Path：如：D:\news\2014\12\abc.text，和不带文件名的Path：如：D:\news\2014\12
     */
    public static void isExistDir(String filePath) {
        String[] paths = {""};
        //切割路径
        try {
            //File对象转换为标准路径并进行切割，有两种windows和linux
            String tempPath = new File(filePath).getCanonicalPath();
            //windows
            paths = tempPath.split("\\\\");
            if (paths.length == 1) {
                //linux
                paths = tempPath.split("/");
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        //判断是否有后缀
        boolean hasType = false;
        if (paths.length > 0) {
            String tempPath = paths[paths.length - 1];
            if (!tempPath.isEmpty()) {
                if (tempPath.indexOf(".") > 0) {
                    hasType = true;
                }
            }
        }
        //创建文件夹
        String dir = paths[0];
        for (int i = 0; i < paths.length - (hasType ? 2 : 1); i++) {
            // 注意此处循环的长度，有后缀的就是文件路径，没有则文件夹路径
            try {
                //采用linux下的标准写法进行拼接，由于windows可以识别这样的路径，所以这里采用警容的写法
                dir = dir + "/" + paths[i + 1];
                File dirFile = new File(dir);
                if (!dirFile.exists()) {
                    boolean mkdir = dirFile.mkdir();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 导出 Excel 并下载，不需要存储到服务器
     *
     * @param dataList 数据列表
     * @param head     表头类
     * @param fileName 文件名
     * @param res      HttpServletResponse对象
     */
    public static <T> void exportAndDownloadExcel(List<?> dataList, Class<T> head, String fileName, HttpServletResponse res) {
        res.setContentType("application/octet-stream;charset=UTF-8");
        res.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            res.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            res.addHeader("Pargam", "no-cache");
            res.addHeader("Cache-Control", "no-cache");
            res.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            EasyExcel.write(res.getOutputStream(), head)
                    .registerConverter(new EasyExcelLocalDateTimeConverter())
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 导出的格式调整
                    .sheet("sheet1")
                    .doWrite(dataList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
