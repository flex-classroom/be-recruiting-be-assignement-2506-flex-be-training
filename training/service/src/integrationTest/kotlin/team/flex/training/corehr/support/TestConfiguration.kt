package team.flex.training.corehr.support

import org.springframework.context.annotation.Bean
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.jdbc.core.JdbcTemplate
import java.time.Clock
import java.time.Instant
import java.util.TimeZone

@EnableJdbcRepositories(basePackages = ["training.corehr.assignment.**.repository"])
class TestConfiguration {
    @Bean
    fun clock(): Clock = Clock.fixed(
        Instant.parse("2025-06-01T00:00:00Z"),
        TimeZone.getTimeZone("UTC").toZoneId(),
    )

    @Bean
    fun dbCleanUp(jdbcTemplate: JdbcTemplate): DbCleanUp = DbCleanUp(jdbcTemplate)
}
