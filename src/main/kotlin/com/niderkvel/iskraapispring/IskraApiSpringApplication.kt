package com.niderkvel.iskraapispring

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan
@EnableAutoConfiguration
class IskraApiSpringApplication

fun main(args: Array<String>) {
	runApplication<IskraApiSpringApplication>(*args)
}