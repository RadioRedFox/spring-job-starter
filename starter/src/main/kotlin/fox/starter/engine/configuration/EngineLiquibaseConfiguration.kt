package fox.starter.engine.configuration

import javax.sql.DataSource
import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class EngineLiquibaseConfiguration(
    @Qualifier("engineDataSource")
    val engineDataSource: DataSource,
) {
    @Bean
    @Qualifier("engineLiquibase")
    fun engineLiquibase(): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.dataSource = engineDataSource
        liquibase.changeLog = "classpath:/changelog/changelog-starter-engine.xml"
        liquibase.setShouldRun(true)
        return liquibase
    }
}