package team.flex.training.corehr.support

import org.springframework.jdbc.core.JdbcTemplate

class DbSetUp(
    private val jdbcTemplate: JdbcTemplate,
) {

    fun setUp() {
        jdbcTemplate.batchUpdate(*SET_UP_QUERY)
    }

    companion object {
        private val SET_UP_QUERY = arrayOf(
            "insert into `employee_department_assignment` (id, employee_id, department_id, start_date, end_date, created_at, updated_at) values (1, 5, 7, '2024-01-01', '2024-05-31', now(), now())",
            "insert into `employee_department_assignment` (id, employee_id, department_id, start_date, end_date, created_at, updated_at) values (2, 5, 7, '2024-06-01', '2024-06-30', now(), now())",
            "insert into `employee_department_assignment` (id, employee_id, department_id, start_date, end_date, created_at, updated_at) values (3, 5, 11, '2024-07-01', '2024-11-30', now(), now())",

            "insert into `employee_job_assignment` (id, employee_id, job_role_id, start_date, end_date, created_at, updated_at) values (1, 5, 7, '2024-01-01', '2024-05-31', now(), now())",
            "insert into `employee_job_assignment` (id, employee_id, job_role_id, start_date, end_date, created_at, updated_at) values (2, 5, 9, '2024-06-01', '2024-06-30', now(), now())",
            "insert into `employee_job_assignment` (id, employee_id, job_role_id, start_date, end_date, created_at, updated_at) values (3, 5, 8, '2024-07-01', '2024-11-30', now(), now())",
        )
    }
}
