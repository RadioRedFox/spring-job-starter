package fox.starter.engine.service.scheduling.impl


import fox.starter.engine.service.scheduling.EngineTaskPoller
import org.springframework.scheduling.annotation.Scheduled

internal class EngineTaskPollerImpl: EngineTaskPoller {

    @Scheduled(fixedRate = 5000)
    override fun pollEngineTask() {
//        println("pupupu")
        val thread = Thread.currentThread()
        println("Running child on thread: ${thread.name}, is daemon: ${thread.isDaemon}")
    }
}