package fox.starter.engine.configuration

import jakarta.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration

@Configuration
class EngineContextConfiguration(
    private val parent: ApplicationContext
) {

    @PostConstruct
    fun initContext() {
        println("StarterAutoConfiguration print")
        EngineContext.init(parent)
    }
}