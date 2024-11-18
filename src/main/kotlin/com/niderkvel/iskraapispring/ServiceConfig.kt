package com.niderkvel.iskraapispring

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "services")
class ServiceConfig {

    var questions: Boolean = true
    var tasks: Boolean = true
    var events: Boolean = true
    var map: Boolean = true
}

