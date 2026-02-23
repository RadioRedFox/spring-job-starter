package mainpack.main.conf

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import fox.starter.engine.dao.EngineTaskDao
import fox.starter.engine.dao.impl.EngineTaskDaoImpl
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class DataBaseConfiguration(
    val dataBaseProperties: DataBaseProperties,
) {

//    @Bean
//    fun hikariConfig(): HikariConfig {
//        return HikariConfig().apply {
//            driverClassName = "org.postgresql.Driver"
//            jdbcUrl = dataBaseProperties.url
//            username = dataBaseProperties.username
//            password = dataBaseProperties.password
//            maximumPoolSize = dataBaseProperties.maximumPoolSize
//            schema = dataBaseProperties.schema
//            minimumIdle = dataBaseProperties.minimumIdle
//            poolName = dataBaseProperties.poolName
//        }
//    }

//    @Bean
//    fun dataSource(@Qualifier("hikariConfig") hikariConfig: HikariConfig): DataSource =
//    fun dataSource(hikariConfig: HikariConfig): DataSource =
//        HikariDataSource(hikariConfig)

    @Bean
    fun namedParameterJdbcTemplate(@Qualifier("dataSource") dataSource: DataSource): NamedParameterJdbcTemplate =
        NamedParameterJdbcTemplate(dataSource)

    @Bean
    fun transactionManager(@Qualifier("dataSource") dataSource: DataSource): PlatformTransactionManager =
        DataSourceTransactionManager(dataSource)

}


