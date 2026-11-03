/*
package com.smartexam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileStorageWebConfig
        implements WebMvcConfigurer {

    @Value("${app.file-storage.logo-directory}")
    private String logoDirectory;


    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        registry
                .addResourceHandler(
                        "/uploads/organization-logos/**"
                )
                .addResourceLocations(
                        "file:" + logoDirectory + "/"
                );

    }

}*/
