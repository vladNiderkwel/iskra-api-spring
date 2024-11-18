package com.niderkvel.iskraapispring

import com.niderkvel.iskraapispring.interceptors.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/images/**")
            .addResourceLocations("file:src/main/images/")
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(questionInterceptor())
            .addPathPatterns("/question/**")

        registry
            .addInterceptor(eventInterceptor())
            .addPathPatterns("/event/**")

        registry
            .addInterceptor(taskInterceptor())
            .addPathPatterns("/task/**")

        registry
            .addInterceptor(mapInterceptor())
            .addPathPatterns("/map-mark/**")
    }

    @Bean
    fun questionInterceptor(): QuestionInterceptor = QuestionInterceptor()

    @Bean
    fun eventInterceptor(): EventInterceptor = EventInterceptor()

    @Bean
    fun taskInterceptor(): TaskInterceptor = TaskInterceptor()

    @Bean
    fun mapInterceptor(): MapInterceptor = MapInterceptor()
}