package com.dj.utils;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;


import java.util.*;


/**
 * 代码生成器
 * v1.0
 * 作者：dj
 */
@Slf4j
public class CodeGenerator {

    private static final String PACK_NAME = "com.dj";
    private static final String AUTHOR = "dj";
    private static final String TABLE = "sys_user";

    /*下面的不用改动*/
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String MAPPER_XML_PATH = "/src/main/resources/mapper/";
    public static final String JAVA_CODE_PATH = "/src/main/java/";

    public static void main(String[] args) {
        generateJava(TABLE);
    }




    private static void generateJava(String tableName) {
        ClassPathResource resource = new ClassPathResource("application.yml");
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(resource);
        Properties properties = yamlPropertiesFactoryBean.getObject();
        FastAutoGenerator.create(properties.getProperty("spring.datasource.url"),
                properties.getProperty("spring.datasource.username"),
                properties.getProperty("spring.datasource.password"))
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .enableSwagger()
                            .disableOpenDir()
                            .outputDir(PROJECT_PATH + JAVA_CODE_PATH); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(PACK_NAME) // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, PROJECT_PATH + MAPPER_XML_PATH)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.controllerBuilder().fileOverride().enableRestStyle().enableHyphenStyle()
                            .serviceBuilder().fileOverride()
                            .mapperBuilder().fileOverride()
                            .entityBuilder().fileOverride().enableLombok()
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE));
                    builder.addInclude(tableName) // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
