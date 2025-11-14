package com.rehabilitation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Web配置类 - 处理日期格式转换和静态资源访问
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${rehabilitation.file.upload-path:/uploads/}")
    private String uploadPath;

    @Value("${rehabilitation.file.static-path:/static/}")
    private String staticPath;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // LocalDate格式化器
        registry.addFormatter(new Formatter<LocalDate>() {
            @Override
            public LocalDate parse(String text, Locale locale) {
                if (text == null || text.trim().isEmpty()) {
                    return null;
                }
                try {
                    return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    throw new IllegalArgumentException("无效的日期格式，请使用 yyyy-MM-dd 格式");
                }
            }

            @Override
            public String print(LocalDate date, Locale locale) {
                return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        });

        // LocalDateTime格式化器
        registry.addFormatter(new Formatter<LocalDateTime>() {
            @Override
            public LocalDateTime parse(String text, Locale locale) {
                if (text == null || text.trim().isEmpty()) {
                    return null;
                }
                try {
                    return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e) {
                    throw new IllegalArgumentException("无效的日期时间格式，请使用 yyyy-MM-dd HH:mm:ss 格式");
                }
            }

            @Override
            public String print(LocalDateTime dateTime, Locale locale) {
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        });
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 配置静态资源映射 - 允许访问上传的文件
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);

        // 配置静态资源映射
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}