package team.flex.training.corehr.assignment.department

interface EmployeeDepartmentAssignmentIdentity {
    companion object

    val employeeDepartmentAssignmentId: Long
}

internal data class SimpleEmployeeDepartmentAssignmentIdentity(
    override val employeeDepartmentAssignmentId: Long,
) : EmployeeDepartmentAssignmentIdentity

fun EmployeeDepartmentAssignmentIdentity.Companion.of(employeeDepartmentAssignmentId: Long): EmployeeDepartmentAssignmentIdentity =
    SimpleEmployeeDepartmentAssignmentIdentity(employeeDepartmentAssignmentId)
