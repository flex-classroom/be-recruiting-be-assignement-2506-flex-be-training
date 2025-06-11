package team.flex.training.corehr.support

import org.springframework.jdbc.core.JdbcTemplate

class DbCleanUp(
    private val jdbcTemplate: JdbcTemplate,
) {

    fun cleanUp() {
        jdbcTemplate.execute("set foreign_key_checks = 0;")
        TABLES.forEach {
            jdbcTemplate.execute("truncate table `$it`;")
        }
        jdbcTemplate.execute("set foreign_key_checks = 1;")
    }

    companion object {
        private val TABLES = listOf(
            "employee_department_assignment",
            "employee_job_assignment",
        )
    }
}
