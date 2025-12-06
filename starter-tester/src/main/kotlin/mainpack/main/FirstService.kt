package mainpack.main

import jakarta.annotation.PostConstruct
import fox.starter.engine.configuration.TestService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
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

    @Scheduled(fixedRate = 5000)
    fun testSheduling(){
        val thread = Thread.currentThread()
        println("Running parent on thread: ${thread.name}, is daemon: ${thread.isDaemon}")
    }
}