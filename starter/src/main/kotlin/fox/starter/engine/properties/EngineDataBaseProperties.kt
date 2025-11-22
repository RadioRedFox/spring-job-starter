package fox.starter.engine.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "starter-engine")
class EngineDataBaseProperties {
    lateinit var username: String
    lateinit var password: String
    lateinit var url: String
    var maximumPoolSize = 3
    var poolName = "HikariPool-Business-Engine"
    var minimumIdle = 1
    var schema: String? = null
}
