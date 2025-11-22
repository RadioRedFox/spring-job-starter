package mainpack.main

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
class MyServiceApplication




fun main(args: Array<String>) {
    runApplication<MyServiceApplication>(*args)
}