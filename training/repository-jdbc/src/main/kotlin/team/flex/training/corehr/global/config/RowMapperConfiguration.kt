package team.flex.training.corehr.global.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.jdbc.repository.config.DefaultQueryMappingConfiguration
import team.flex.training.corehr.assignment.department.dto.DepartmentAssignmentDto
import team.flex.training.corehr.assignment.department.repository.DepartmentAssignmentRowMapper
import team.flex.training.corehr.assignment.job.dto.JobRoleAssignmentDto
import team.flex.training.corehr.assignment.job.repository.JobRoleAssignmentRowMapper

@AutoConfiguration
class RowMapperConfiguration {

    @Bean
    fun rowMappers(): DefaultQueryMappingConfiguration {
        return DefaultQueryMappingConfiguration()
            .registerRowMapper(DepartmentAssignmentDto::class.java, DepartmentAssignmentRowMapper())
            .registerRowMapper(JobRoleAssignmentDto::class.java, JobRoleAssignmentRowMapper())
    }
}