package team.flex.training.corehr.assignment.department.repository

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentModel
import java.time.Instant
import java.time.LocalDate

@Table("employee_department_assignment")
class EmployeeDepartmentAssignmentEntity(

    @Column
    override val employeeId: Long,

    @Column
    override val departmentId: Long,

    @Column
    override val startDate: LocalDate,

    @Column
    override val endDate: LocalDate,

    @Column
    override val createdAt: Instant,

    @Column
    override val updatedAt: Instant,

) : EmployeeDepartmentAssignmentModel {

    @Id
    var id: Long = 0L

    override val employeeDepartmentAssignmentId: Long
        get() = id
}
