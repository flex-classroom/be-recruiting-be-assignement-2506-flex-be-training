package team.flex.training.corehr.assignment.job

interface EmployeeJobAssignmentIdentity {
    companion object

    val employeeJobAssignmentId: Long
}

internal data class SimpleEmployeeJobAssignmentIdentity(
    override val employeeJobAssignmentId: Long,
) : EmployeeJobAssignmentIdentity

fun EmployeeJobAssignmentIdentity.Companion.of(employeeJobAssignmentId: Long): EmployeeJobAssignmentIdentity =
    SimpleEmployeeJobAssignmentIdentity(employeeJobAssignmentId)
