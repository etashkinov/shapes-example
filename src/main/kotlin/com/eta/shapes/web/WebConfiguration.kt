package com.eta.shapes.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ResponseBuilder
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket


@Configuration
internal class WebConfiguration : WebMvcConfigurer {
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON)
    }

    /**
     * Swagger defaults
     */
    @Bean
    fun docket(): Docket = Docket(DocumentationType.OAS_30)
        .useDefaultResponseMessages(false)
}