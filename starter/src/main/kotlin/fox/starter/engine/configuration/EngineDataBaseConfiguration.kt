package fox.starter.engine.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import fox.starter.engine.properties.EngineDataBaseProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableConfigurationProperties(EngineDataBaseProperties::class)
@EnableTransactionManagement
class EngineDataBaseConfiguration(
    val dataBaseProperties: EngineDataBaseProperties
) {

    @Bean
    fun engineHikariConfig(): HikariConfig {
        return HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = dataBaseProperties.url
            username = dataBaseProperties.username
            password = dataBaseProperties.password
            maximumPoolSize = dataBaseProperties.maximumPoolSize
            schema = dataBaseProperties.schema
            minimumIdle = dataBaseProperties.minimumIdle
            poolName = dataBaseProperties.poolName
        }
    }

    @Bean
    fun engineDataSource(@Qualifier("engineHikariConfig") engineHikariConfig: HikariConfig): DataSource =
        HikariDataSource(engineHikariConfig)

    @Bean
    fun engineNamedParameterJdbcTemplate(
        @Qualifier("engineDataSource") dataSource: DataSource
    ): NamedParameterJdbcTemplate =
        NamedParameterJdbcTemplate(dataSource)

    @Bean
    fun engineTransactionManager(
        @Qualifier("engineDataSource") dataSource: DataSource
    ): PlatformTransactionManager =
        DataSourceTransactionManager(dataSource)
}


