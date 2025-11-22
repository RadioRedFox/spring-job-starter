package mainpack.main

import jakarta.annotation.PostConstruct
import fox.starter.engine.configuration.TestService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class FirstService(
    @Qualifier("testService")
    val testService: TestService
) {

    @PostConstruct
    fun testInit() {
        testService.test()
    }
}