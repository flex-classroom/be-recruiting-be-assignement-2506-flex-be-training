package team.flex.training.corehr.assignment.department.repository

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentRepository

@AutoConfiguration
@EnableJdbcRepositories
class EmployeeDepartmentAssignmentRepositoryAutoConfiguration {

    @Bean
    fun employeeDepartmentAssignmentRepository(
        employeeDepartmentAssignmentJdbcRepository: EmployeeDepartmentAssignmentJdbcRepository,
    ): EmployeeDepartmentAssignmentRepository =
        EmployeeDepartmentAssignmentRepositoryImpl(
            employeeDepartmentAssignmentJdbcRepository,
        )
}
