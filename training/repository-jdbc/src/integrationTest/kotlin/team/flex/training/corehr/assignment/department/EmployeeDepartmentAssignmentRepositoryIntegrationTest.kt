package team.flex.training.corehr.assignment.department

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestConstructor
import team.flex.training.corehr.assignment.department.repository.EmployeeDepartmentAssignmentJdbcRepository
import team.flex.training.corehr.assignment.department.repository.EmployeeDepartmentAssignmentRepositoryAutoConfiguration
import team.flex.training.corehr.company.department.repository.DepartmentJdbcRepository
import team.flex.training.corehr.company.department.repository.DepartmentRepositoryAutoConfiguration
import team.flex.training.corehr.employee.EmployeeIdentity
import team.flex.training.corehr.employee.of
import team.flex.training.corehr.global.config.RowMapperConfiguration
import team.flex.training.corehr.support.fixture.assignment.EmployeeDepartmentAssignmentEntityFixture
import team.flex.training.corehr.support.fixture.company.DepartmentFixture
import java.time.LocalDate

@Import(
    EmployeeDepartmentAssignmentRepositoryAutoConfiguration::class,
    DepartmentRepositoryAutoConfiguration::class,
    RowMapperConfiguration::class,
)
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class EmployeeDepartmentAssignmentRepositoryIntegrationTest(
    private val cut: EmployeeDepartmentAssignmentRepository,
    private val jdbcRepository: EmployeeDepartmentAssignmentJdbcRepository,
    private val departmentRepository: DepartmentJdbcRepository,
) {

    @Nested
    @DisplayName("구성원 ID 가 일치하면서 기간이 겹치는 부서 발령이 있는지 조회할 때")
    inner class existsByEmployeeIdAndPeriod {

        @ParameterizedTest
        @CsvSource(
            "'2024-12-31', '2025-01-01'",
            "'2025-01-31', '2025-02-01'",
        )
        fun `겹치는 기간의 발령 정보가 있다면 true 를 반환 한다`(startDateString: String, endDateString: String) {
            // given
            jdbcRepository.save(EmployeeDepartmentAssignmentEntityFixture.기본.toEntity())

            // when
            val actual = cut.existsByEmployeeIdAndPeriod(
                EmployeeIdentity.of(0),
                LocalDate.parse(startDateString),
                LocalDate.parse(endDateString),
            )

            // then
            assertThat(actual).isTrue
        }

        @ParameterizedTest
        @CsvSource(
            "'2024-12-30', '2024-12-31'",
            "'2025-02-01', '2025-02-02'",
        )
        fun `겹치는 기간의 발령 정보가 없다면 false 를 반환 한다`(startDateString: String, endDateString: String) {
            // given
            jdbcRepository.save(EmployeeDepartmentAssignmentEntityFixture.기본.toEntity())

            // when
            val actual = cut.existsByEmployeeIdAndPeriod(
                EmployeeIdentity.of(0),
                LocalDate.parse(startDateString),
                LocalDate.parse(endDateString),
            )

            // then
            assertThat(actual).isFalse
        }

        @ParameterizedTest
        @CsvSource(
            "'2024-12-31', '2025-01-01'",
            "'2025-01-31', '2025-02-01'",
        )
        fun `다른 구성원의 겹치는 기간의 발령 정보가 있다면 false 를 반환 한다`(startDateString: String, endDateString: String) {
            // given
            jdbcRepository.save(EmployeeDepartmentAssignmentEntityFixture.기본.toEntity())

            // when
            val actual = cut.existsByEmployeeIdAndPeriod(
                EmployeeIdentity.of(1),
                LocalDate.parse(startDateString),
                LocalDate.parse(endDateString),
            )

            // then
            assertThat(actual).isFalse
        }
    }

    @Test
    fun `부서 발령 정보를 저장할 수 있다`() {
        // given
        val departmentAssignment = EmployeeDepartmentAssignmentEntityFixture.기본

        // when
        cut.save(departmentAssignment)

        // then
        val actual = jdbcRepository.findAll()
        assertThat(actual).hasSize(1)
            .extracting(
                "employeeDepartmentAssignmentId",
                "employeeId",
                "departmentId",
                "startDate",
                "endDate",
            ).containsExactly(
                Tuple.tuple(
                    1L,
                    0L,
                    0L,
                    LocalDate.parse("2025-01-01"),
                    LocalDate.parse("2025-01-31"),
                ),
            )
    }

    @Nested
    @DisplayName("구성원 ID 가 일치하면서 특정 날짜에 부서 발령 정보를 조회할 때")
    inner class findByEmployeeIdAndDateBetween {

        @ParameterizedTest
        @ValueSource(strings = ["2025-01-01", "2025-01-31"])
        fun `구성원의 발령 정보가 존재하면 반환 한다`(targetDateString: String) {
            // given
            val department = departmentRepository.save(DepartmentFixture.기본.toEntity())
            jdbcRepository.save(EmployeeDepartmentAssignmentEntityFixture.기본.toEntity(department.id))
            val employeeIdentity = EmployeeIdentity.of(0)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = cut.findByEmployeeIdAndDateBetween(employeeIdentity, targetDate)

            // then
            assertThat(actual).isNotNull
                .extracting(
                    "startDate",
                    "endDate",
                    "departmentId",
                    "departmentName",
                ).containsExactly(
                    LocalDate.parse("2025-01-01"),
                    LocalDate.parse("2025-01-31"),
                    department.id,
                    "부서 1",
                )
        }

        @ParameterizedTest
        @ValueSource(strings = ["2024-12-31", "2025-02-01"])
        fun `구성원의 발령 정보가 없다면 null 을 반환 한다`(targetDateString: String) {
            // given
            val department = departmentRepository.save(DepartmentFixture.기본.toEntity())
            jdbcRepository.save(EmployeeDepartmentAssignmentEntityFixture.기본.toEntity(department.id))
            val employeeIdentity = EmployeeIdentity.of(0)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = cut.findByEmployeeIdAndDateBetween(employeeIdentity, targetDate)

            // then
            assertThat(actual).isNull()
        }
    }

    @Nested
    @DisplayName("구성원 ID 로 해당 구성원의 모든 부서 변경 이력을 조회할 때")
    inner class findByEmployeeId {

        @Test
        fun `부서 변경 이력이 존재하면 모든 이력을 반환 한다`() {
            // given
            val department = departmentRepository.save(DepartmentFixture.기본.toEntity())
            jdbcRepository.save(EmployeeDepartmentAssignmentEntityFixture.기본.toEntity(department.id))
            val employeeIdentity = EmployeeIdentity.of(0)

            // when
            val actual = cut.findByEmployeeId(employeeIdentity)

            // then
            assertThat(actual).hasSize(1)
                .extracting(
                    "startDate",
                    "endDate",
                    "departmentId",
                    "departmentName",
                ).containsExactly(
                    Tuple.tuple(
                        LocalDate.parse("2025-01-01"),
                        LocalDate.parse("2025-01-31"),
                        department.id,
                        "부서 1",
                    ),
                )
        }

        @Test
        fun `부서 변경 이력이 없다면 빈 List 를 반환 한다`() {
            // given
            val employeeIdentity = EmployeeIdentity.of(0)

            // when
            val actual = cut.findByEmployeeId(employeeIdentity)

            // then
            assertThat(actual).isEmpty()
        }
    }
}
