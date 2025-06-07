package team.flex.training.corehr.assignment.department.repository

import org.springframework.jdbc.core.RowMapper
import team.flex.training.corehr.assignment.department.dto.DepartmentAssignmentDto
import java.sql.ResultSet

class DepartmentAssignmentRowMapper : RowMapper<DepartmentAssignmentDto> {
    override fun mapRow(rs: ResultSet, rowNum: Int): DepartmentAssignmentDto {
        return DepartmentAssignmentDto(
            rs.getLong("id"),
            rs.getDate("start_date").toLocalDate(),
            rs.getDate("end_date").toLocalDate(),
            rs.getLong("department_id"),
            rs.getString("department_name"),
        )
    }
}