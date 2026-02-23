package fox.starter.engine.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "starter-engine")
class EngineProperties {
    var dataSourceBeanName: String = "dataSource"
    var namedParameterJdbcTemplateBeanName: String = "namedParameterJdbcTemplate"
}