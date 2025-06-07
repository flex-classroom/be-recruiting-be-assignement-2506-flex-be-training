package team.flex.training.corehr.assignment.job.repository

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentRepository

@AutoConfiguration
@EnableJdbcRepositories
class EmployeeJobAssignmentRepositoryAutoConfiguration {

    @Bean
    fun employeeJobAssignmentRepository(
        employeeJobAssignmentJdbcRepository: EmployeeJobAssignmentJdbcRepository,
    ): EmployeeJobAssignmentRepository =
        EmployeeJobAssignmentRepositoryImpl(
            employeeJobAssignmentJdbcRepository,
        )
}
