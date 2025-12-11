package com.example.mywebapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Signifie : "Quand on demande l'URL /pdfs/..., va chercher dans le dossier /app/pdfs/"
        registry.addResourceHandler("/pdfs/**")
                .addResourceLocations("file:/app/pdfs/");
    }
}