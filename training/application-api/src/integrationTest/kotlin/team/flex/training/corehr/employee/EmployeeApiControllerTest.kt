package team.flex.training.corehr.employee

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.ServletException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl
import team.flex.training.corehr.application.CoreHrApplication
import team.flex.training.corehr.assignment.department.repository.EmployeeDepartmentAssignmentJdbcRepository
import team.flex.training.corehr.assignment.job.repository.EmployeeJobAssignmentJdbcRepository
import team.flex.training.corehr.employee.dto.EmployeeAssignmentCreateRequest
import team.flex.training.corehr.support.DbCleanUp
import team.flex.training.corehr.support.DbSetUp
import java.time.LocalDate
import java.util.TimeZone

@Import(
    DbSetUp::class,
    DbCleanUp::class,
)
@SpringBootTest(classes = [CoreHrApplication::class])
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class EmployeeApiControllerTest(
    private val mockMvc: MockMvc,
    private val dbSetUp: DbSetUp,
    private val dbCleanUp: DbCleanUp,
    private val departmentAssignmentRepository: EmployeeDepartmentAssignmentJdbcRepository,
    private val jobAssignmentRepository: EmployeeJobAssignmentJdbcRepository,
) {
    private val objectMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())

    @BeforeEach
    fun setUp() {
        dbSetUp.setUp()
    }

    @AfterEach
    fun tearDown() {
        dbCleanUp.cleanUp()
    }

    @Nested
    inner class `인사 발령 테스트` {

        @Test
        fun `시나리오 1`() {
            // given
            val request = EmployeeAssignmentCreateRequest(
                LocalDate.parse("2024-12-01"),
                LocalDate.parse("2025-03-31"),
                null,
                8,
            )

            // when
            val actual = mockMvc.post(
                "/api/v2/corehr/companies/{companyId}/employees/{employeeId}/employee-assignment",
                2,
                5,
            ) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

            // then
            actual.andExpect {
                status(StatusResultMatchersDsl::isOk)
            }

            val departmentAssignments = departmentAssignmentRepository.findAll()
            assertThat(departmentAssignments).hasSize(4)
            val newDepartmentAssignment = departmentAssignments.find {
                it.startDate == LocalDate.parse("2024-12-01") && it.endDate == LocalDate.parse("2025-03-31")
            }
            assertThat(newDepartmentAssignment).isNotNull

            val jobAssignments = jobAssignmentRepository.findAll()
            assertThat(jobAssignments).hasSize(3)
            val `24년 11월 30일 이후 직무` = jobAssignments.filter {
                it.startDate.isAfter(LocalDate.parse("2024-11-30"))
            }
            assertThat(`24년 11월 30일 이후 직무`).isEmpty()
        }

        @Test
        fun `시나리오 2`() {
            // given
            val request = EmployeeAssignmentCreateRequest(
                LocalDate.parse("2024-11-29"),
                LocalDate.parse("2024-12-31"),
                null,
                10,
            )

            // when then
            assertThatThrownBy {
                mockMvc.post(
                    "/api/v2/corehr/companies/{companyId}/employees/{employeeId}/employee-assignment",
                    2,
                    5,
                ) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
            }.isInstanceOf(ServletException::class.java)
        }

        @Test
        fun `시나리오 3`() {
            // given
            val request = EmployeeAssignmentCreateRequest(
                LocalDate.parse("2025-01-01"),
                LocalDate.parse("2025-03-31"),
                5,
                8,
            )

            // when
            val actual = mockMvc.post(
                "/api/v2/corehr/companies/{companyId}/employees/{employeeId}/employee-assignment",
                2,
                5,
            ) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

            // then
            actual.andExpect {
                status(StatusResultMatchersDsl::isOk)
            }

            val departmentAssignments = departmentAssignmentRepository.findAll()
            assertThat(departmentAssignments).hasSize(4)
            val newDepartmentAssignment = departmentAssignments.find {
                it.startDate == LocalDate.parse("2025-01-01") && it.endDate == LocalDate.parse("2025-03-31")
            }
            assertThat(newDepartmentAssignment).isNotNull
            val `24년 11월 30일 이후 25년 1월 1일 이전 부서` = departmentAssignments.filter {
                it.startDate.isAfter(LocalDate.parse("2024-11-30")) && it.endDate.isBefore(LocalDate.parse("2025-01-01"))
            }
            assertThat(`24년 11월 30일 이후 25년 1월 1일 이전 부서`).isEmpty()

            val jobAssignments = jobAssignmentRepository.findAll()
            assertThat(jobAssignments).hasSize(4)
            val newJobAssignment = jobAssignments.find {
                it.startDate == LocalDate.parse("2025-01-01") && it.endDate == LocalDate.parse("2025-03-31")
            }
            assertThat(newJobAssignment).isNotNull
            val `24년 11월 30일 이후 25년 1월 1일 이전 직무` = jobAssignments.filter {
                it.startDate.isAfter(LocalDate.parse("2024-11-30")) && it.endDate.isBefore(LocalDate.parse("2025-01-01"))
            }
            assertThat(`24년 11월 30일 이후 25년 1월 1일 이전 직무`).isEmpty()
        }
    }

    @Nested
    inner class `요청일 기준 인사 정보 조회 테스트` {

        @Test
        fun `시나리오 1`() {
            // when then
            mockMvc.get(
                "/api/v2/corehr/companies/{companyId}/employees/{employeeId}/employee-assignment",
                2,
                5,
            ) {
                queryParam("targetDate", "2024-02-01")
            }.andExpectAll {
                status(StatusResultMatchersDsl::isOk)
                jsonPath("$.assignment.departmentName") { value("서버 개발1팀") }
                jsonPath("$.assignment.jobRoleName") { value("Product Engineer") }
            }
        }

        @Test
        fun `시나리오 2`() {
            // when then
            mockMvc.get(
                "/api/v2/corehr/companies/{companyId}/employees/{employeeId}/employee-assignment",
                2,
                5,
            ) {
                queryParam("targetDate", "2024-06-15")
            }.andExpectAll {
                status(StatusResultMatchersDsl::isOk)
                jsonPath("$.assignment.departmentName") { value("서버 개발1팀") }
                jsonPath("$.assignment.jobRoleName") { value("Backend Engineer") }
            }
        }

        @ParameterizedTest
        @ValueSource(strings = ["2024-07-01", "2024-11-30"])
        fun `시나리오 3`(targetDate: String) {
            // when then
            mockMvc.get(
                "/api/v2/corehr/companies/{companyId}/employees/{employeeId}/employee-assignment",
                2,
                5,
            ) {
                queryParam("targetDate", targetDate)
            }.andExpectAll {
                status(StatusResultMatchersDsl::isOk)
                jsonPath("$.assignment.departmentName") { value("기획팀") }
                jsonPath("$.assignment.jobRoleName") { value("Product Manager") }
            }
        }
    }

    @Test
    fun `구성원의 인사 정보 이력 조회 테스트`() {
        // when then
        mockMvc.get(
            "/api/v2/corehr/companies/{companyId}/employees/{employeeId}/employee-assignment-history",
            2,
            5,
        )
            .andExpectAll {
                status(StatusResultMatchersDsl::isOk)
                jsonPath("$.assignments.size()") { value(6) }
                jsonPath("$.assignments[0].departmentName") { value("기획팀") }
                jsonPath("$.assignments[1].jobRoleName") { value("Product Manager") }
                jsonPath("$.assignments[2].departmentName") { value("서버 개발1팀") }
                jsonPath("$.assignments[3].jobRoleName") { value("Backend Engineer") }
                jsonPath("$.assignments[4].departmentName") { value("서버 개발1팀") }
                jsonPath("$.assignments[5].jobRoleName") { value("Product Engineer") }
            }
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun setUpTimeZone() {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        }
    }
}
