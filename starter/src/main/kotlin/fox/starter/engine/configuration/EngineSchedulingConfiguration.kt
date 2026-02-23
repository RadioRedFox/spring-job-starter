package fox.starter.engine.configuration

import fox.starter.engine.dao.EngineTaskDao
import fox.starter.engine.service.scheduling.EngineTaskPoller
import fox.starter.engine.service.scheduling.impl.EngineTaskPollerImpl
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler


@Configuration
@EnableScheduling
class EngineSchedulingConfiguration {

    @Bean
    fun engineTaskScheduler(): ThreadPoolTaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = 1
        scheduler.threadNamePrefix = "engine-"
        scheduler.initialize()
        return scheduler
    }



    private val engineTaskExecutor = ThreadPoolTaskExecutor().apply {
        corePoolSize = 3
        maxPoolSize = 3
        threadNamePrefix = "coroutine-pool-"
        initialize()
    }

    private val dispatcher = engineTaskExecutor.asCoroutineDispatcher()

    @Bean
    internal fun engineCoroutineDispatcher(): CoroutineDispatcher = dispatcher

    @Bean
    internal fun engineCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + dispatcher)

    @Bean
    internal fun engineTaskPoller(engineCoroutineScope: CoroutineScope, engineTaskDao: EngineTaskDao): EngineTaskPoller {
        return EngineTaskPollerImpl(engineCoroutineScope, engineTaskDao)
    }

    @PreDestroy
    fun shutdown() {
        engineTaskExecutor.shutdown()
        println("Coroutine executor shut down")
    }
}