package team.flex.training.corehr.support

import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestConstructor
import team.flex.training.corehr.assignment.department.repository.EmployeeDepartmentAssignmentEntity
import team.flex.training.corehr.assignment.department.repository.EmployeeDepartmentAssignmentJdbcRepository
import team.flex.training.corehr.assignment.job.repository.EmployeeJobAssignmentEntity
import team.flex.training.corehr.assignment.job.repository.EmployeeJobAssignmentJdbcRepository
import team.flex.training.corehr.company.department.repository.DepartmentEntity
import team.flex.training.corehr.company.department.repository.DepartmentJdbcRepository
import team.flex.training.corehr.company.jobrole.repository.JobRoleEntity
import team.flex.training.corehr.company.jobrole.repository.JobRoleJdbcRepository
import team.flex.training.corehr.company.repository.CompanyEntity
import team.flex.training.corehr.company.repository.CompanyJdbcRepository
import team.flex.training.corehr.employee.repository.EmployeeEntity
import team.flex.training.corehr.employee.repository.EmployeeJdbcRepository
import team.flex.training.corehr.support.fixture.assignment.EmployeeDepartmentAssignmentFixture
import team.flex.training.corehr.support.fixture.assignment.EmployeeJobAssignmentFixture
import team.flex.training.corehr.support.fixture.company.CompanyFixture
import team.flex.training.corehr.support.fixture.company.DepartmentFixture
import team.flex.training.corehr.support.fixture.company.JobRoleFixture
import team.flex.training.corehr.support.fixture.employee.EmployeeFixture

@Import(TestConfiguration::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
abstract class IntegrationTestSupport {

    @Autowired
    private lateinit var dbCleanUp: DbCleanUp

    @Autowired
    protected lateinit var companyRepository: CompanyJdbcRepository

    @Autowired
    protected lateinit var departmentRepository: DepartmentJdbcRepository

    @Autowired
    protected lateinit var jobRoleRepository: JobRoleJdbcRepository

    @Autowired
    protected lateinit var employeeRepository: EmployeeJdbcRepository

    @Autowired
    protected lateinit var employeeDepartmentAssignmentRepository: EmployeeDepartmentAssignmentJdbcRepository

    @Autowired
    protected lateinit var employeeJobAssignmentRepository: EmployeeJobAssignmentJdbcRepository

    @AfterEach
    fun tearDown() {
        dbCleanUp.cleanUp()
    }

    protected fun `회사 생성`(company: CompanyEntity = CompanyFixture.기본.toEntity()) =
        companyRepository.save(company)

    protected fun `부서 생성`(department: DepartmentEntity = DepartmentFixture.기본.toEntity(`회사 생성`().id)) =
        departmentRepository.save(department)

    protected fun `직무 생성`(jobRole: JobRoleEntity = JobRoleFixture.기본.toEntity(`회사 생성`().id)) =
        jobRoleRepository.save(jobRole)

    protected fun `사원 생성`(employee: EmployeeEntity = EmployeeFixture.기본.toEntity(`회사 생성`().id)) =
        employeeRepository.save(employee)

    protected fun `부서 발령 생성`(
        assignment: EmployeeDepartmentAssignmentEntity = EmployeeDepartmentAssignmentFixture.기본.toEntity(
            `사원 생성`().id,
            `부서 생성`().id,
        ),
    ) = employeeDepartmentAssignmentRepository.save(assignment)

    protected fun `직무 발령 생성`(
        assignment: EmployeeJobAssignmentEntity = EmployeeJobAssignmentFixture.기본.toEntity(
            `사원 생성`().id,
            `직무 생성`().id,
        ),
    ) = employeeJobAssignmentRepository.save(assignment)
}
