package fox.starter.engine.configuration

import fox.starter.engine.dao.EngineTaskDao
import fox.starter.engine.dao.impl.EngineTaskDaoImpl
import fox.starter.engine.properties.EngineProperties
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableConfigurationProperties(EngineProperties::class)
@EnableTransactionManagement
class EngineDataBaseConfiguration(
    private val engineProperties: EngineProperties,
    private val applicationContext: ApplicationContext
) {

    @Bean
    fun engineDataSource(): DataSource =
        applicationContext.getBean(engineProperties.dataSourceBeanName, DataSource::class.java)

    @Bean
    fun engineNamedParameterJdbcTemplate(): NamedParameterJdbcTemplate =
        applicationContext.getBean(engineProperties.namedParameterJdbcTemplateBeanName, NamedParameterJdbcTemplate::class.java)

    @Bean
    fun engineTaskDao(@Qualifier("engineNamedParameterJdbcTemplate") engineNamedParameterJdbcTemplate: NamedParameterJdbcTemplate): EngineTaskDao {
        return EngineTaskDaoImpl(namedParameterJdbcTemplate = engineNamedParameterJdbcTemplate)
    }
}


