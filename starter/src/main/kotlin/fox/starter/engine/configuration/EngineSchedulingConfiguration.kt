package fox.starter.engine.configuration

import fox.starter.engine.service.scheduling.EngineTaskPoller
import fox.starter.engine.service.scheduling.impl.EngineTaskPollerImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor
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

    @Bean
//    @ConditionalOnProperty("", matchIfMissing = false)
    internal fun engineTaskPoller(): EngineTaskPoller {
        return EngineTaskPollerImpl()
    }
}