package team.flex.training.corehr.support.fake.assignment

import team.flex.training.corehr.assignment.job.EmployeeJobAssignment
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentModel
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentRepository
import team.flex.training.corehr.assignment.job.dto.JobRoleAssignmentDto
import team.flex.training.corehr.company.jobrole.JobRoleModel
import team.flex.training.corehr.employee.EmployeeIdentity
import team.flex.training.corehr.employee.EmployeeModel
import team.flex.training.corehr.support.fixture.assignment.EmployeeJobAssignmentEntityFixture
import team.flex.training.corehr.support.fixture.company.JobRoleFixture
import team.flex.training.corehr.support.fixture.employee.EmployeeFixture
import java.time.LocalDate

class FakeEmployeeJobAssignmentRepository : EmployeeJobAssignmentRepository {
    val db: MutableMap<Long, EmployeeJobAssignmentModel> = mutableMapOf()

    override fun existsByEmployeeIdAndPeriod(
        employeeIdentity: EmployeeIdentity,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean {
        return db.values.filter { it.employeeId == employeeIdentity.employeeId }
            .any {
                it.startDate <= endDate && it.endDate >= startDate
            }
    }

    override fun save(jobAssignment: EmployeeJobAssignmentModel): EmployeeJobAssignmentModel {
        val model = EmployeeJobAssignment(
            db.keys.size.toLong() + 1,
            jobAssignment.employeeId,
            jobAssignment.jobRoleId,
            jobAssignment.startDate,
            jobAssignment.endDate,
            jobAssignment.createdAt,
            jobAssignment.updatedAt,
        )

        db[model.employeeJobAssignmentId] = model

        return model
    }

    override fun findByEmployeeIdAndDateBetween(
        employeeIdentity: EmployeeIdentity,
        targetDate: LocalDate,
    ): JobRoleAssignmentDto? {
        return db.values.filter { it.employeeId == employeeIdentity.employeeId }
            .map {
                JobRoleAssignmentDto(
                    it.employeeJobAssignmentId,
                    it.startDate,
                    it.endDate,
                    it.jobRoleId,
                    "직무",
                )
            }.find {
                (targetDate.isEqual(it.startDate) || targetDate.isAfter(it.startDate)) &&
                    (targetDate.isEqual(it.endDate) || targetDate.isBefore(it.endDate))
            }
    }

    override fun findByEmployeeId(employeeIdentity: EmployeeIdentity): List<JobRoleAssignmentDto> {
        return db.values.filter { it.employeeId == employeeIdentity.employeeId }
            .map {
                JobRoleAssignmentDto(
                    it.employeeJobAssignmentId,
                    it.startDate,
                    it.endDate,
                    it.jobRoleId,
                    "직무",
                )
            }
    }

    fun `직무 발령 생성`(
        employee: EmployeeModel = EmployeeFixture.기본,
        job: JobRoleModel = JobRoleFixture.기본,
        jobAssignment: EmployeeJobAssignmentModel = EmployeeJobAssignmentEntityFixture.기본,
    ): EmployeeJobAssignmentModel {
        val fixture = EmployeeJobAssignment(
            0,
            employee.employeeId,
            job.jobRoleId,
            jobAssignment.startDate,
            jobAssignment.endDate,
            jobAssignment.createdAt,
            jobAssignment.updatedAt,
        )

        return save(fixture)
    }

    fun cleanUp() {
        db.clear()
    }
}
