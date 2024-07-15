package com.xunmeng.jccommerce;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 代码生成器
 *
 * @author 金多虾
 * @since 2023-12-06
 */
public class CodeGenerateTest {
    //    static String MODULE_NAME = "jccommerce";
    static String JDBC_URL = "jdbc:mysql://10.10.1.3:3306/xunmeng_test02?useUnicode=true&characterEncoding=utf-8";
    static String JDBC_USERNAME = "xunmeng_test02";
    static String JDBC_PW = "3c4JjPmtbBe4yh4k";
//    static String JAVA_DIR = "\\" + MODULE_NAME + "\\src\\main\\java";
//    static String MAPPER_DIR = "\\" + MODULE_NAME + "\\src\\main\\resources\\mapper";

    // mac文件路径不能用反斜杠
    static String JAVA_DIR = "/" + "/src/main/java";
    static String MAPPER_DIR = "/" + "/src/main/resources/mapper";
    static String AUTHOR = "ltm";
    static String PACKAGE_NAME = "com.xunmeng.jccommerce";

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        FastAutoGenerator.create(JDBC_URL, JDBC_USERNAME, JDBC_PW)
                // 全局配置
                .globalConfig((scanner, builder) ->
                        builder.author(AUTHOR)
                                .disableOpenDir()
                                .enableSwagger()
                                .outputDir(projectPath + JAVA_DIR)
                                .commentDate("yyyy-MM-dd HH:mm:ss")
                                .dateType(DateType.TIME_PACK)
                )
                // 包配置
                .packageConfig((scanner, builder) ->
                        builder.parent(PACKAGE_NAME).entity("domain")
                                .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + MAPPER_DIR)))
                // 策略配置
                .strategyConfig((scanner, builder) ->
                        builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                                .controllerBuilder().enableRestStyle().enableHyphenStyle()
                                .entityBuilder().enableLombok().addTableFills(
                                        new Column("create_time", FieldFill.INSERT),
                                        new Column("update_time", FieldFill.INSERT_UPDATE)
                                ).enableFileOverride().build())
                .templateConfig((scanner, builder) ->
                        builder.controller("")
                                .build())
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                   .templateEngine(new BeetlTemplateEngine())
                   .templateEngine(new FreemarkerTemplateEngine())
                 */
                .execute();
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

}
