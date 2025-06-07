package team.flex.training.corehr.assignment.job.repository

import org.springframework.jdbc.core.RowMapper
import team.flex.training.corehr.assignment.job.dto.JobRoleAssignmentDto
import java.sql.ResultSet

class JobRoleAssignmentRowMapper : RowMapper<JobRoleAssignmentDto> {
    override fun mapRow(rs: ResultSet, rowNum: Int): JobRoleAssignmentDto {
        return JobRoleAssignmentDto(
            rs.getLong("id"),
            rs.getDate("start_date").toLocalDate(),
            rs.getDate("end_date").toLocalDate(),
            rs.getLong("job_role_id"),
            rs.getString("job_name"),
        )
    }
}