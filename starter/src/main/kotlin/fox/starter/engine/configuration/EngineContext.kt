package fox.starter.engine.configuration

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.env.ConfigurableEnvironment


internal object EngineContext {
    var context: AnnotationConfigApplicationContext? = null
        private set

    fun init(parent: ApplicationContext) {
        if (context != null) return

        val child = AnnotationConfigApplicationContext()
        child.parent = parent
        child.environment = parent.environment as ConfigurableEnvironment
        child.register(EngineSchedulingConfiguration::class.java)
        child.register(EngineDataBaseConfiguration::class.java)
        child.register(EngineLiquibaseConfiguration::class.java)
        child.refresh()

        context = child
    }
}