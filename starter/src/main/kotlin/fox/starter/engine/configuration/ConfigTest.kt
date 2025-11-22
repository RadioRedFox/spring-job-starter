package fox.starter.engine.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigTest {

    @Bean("testService")
    @ConditionalOnMissingBean
    fun getTestService(): TestService {
        return TestService()
    }
}


