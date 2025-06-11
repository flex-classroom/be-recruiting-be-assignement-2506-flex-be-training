package team.flex.training.corehr.assignment.job

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
import team.flex.training.corehr.assignment.job.repository.EmployeeJobAssignmentJdbcRepository
import team.flex.training.corehr.assignment.job.repository.EmployeeJobAssignmentRepositoryAutoConfiguration
import team.flex.training.corehr.company.jobrole.repository.JobRoleJdbcRepository
import team.flex.training.corehr.company.jobrole.repository.JobRoleRepositoryAutoConfiguration
import team.flex.training.corehr.employee.EmployeeIdentity
import team.flex.training.corehr.employee.of
import team.flex.training.corehr.global.config.RowMapperConfiguration
import team.flex.training.corehr.support.fixture.assignment.EmployeeJobAssignmentEntityFixture
import team.flex.training.corehr.support.fixture.company.JobRoleFixture
import java.time.LocalDate

@Import(
    EmployeeJobAssignmentRepositoryAutoConfiguration::class,
    JobRoleRepositoryAutoConfiguration::class,
    RowMapperConfiguration::class,
)
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class EmployeeJobAssignmentRepositoryIntegrationTest(
    private val cut: EmployeeJobAssignmentRepository,
    private val jdbcRepository: EmployeeJobAssignmentJdbcRepository,
    private val jobRoleRepository: JobRoleJdbcRepository,
) {

    @Nested
    @DisplayName("구성원 ID 가 일치하면서 기간이 겹치는 직무 발령이 있는지 조회할 때")
    inner class existsByEmployeeIdAndPeriod {

        @ParameterizedTest
        @CsvSource(
            "'2024-12-31', '2025-01-01'",
            "'2025-01-31', '2025-02-01'",
        )
        fun `겹치는 기간의 발령 정보가 있다면 true 를 반환 한다`(startDateString: String, endDateString: String) {
            // given
            jdbcRepository.save(EmployeeJobAssignmentEntityFixture.기본.toEntity())

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
            jdbcRepository.save(EmployeeJobAssignmentEntityFixture.기본.toEntity())

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
            jdbcRepository.save(EmployeeJobAssignmentEntityFixture.기본.toEntity())

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
    fun `직무 발령 정보를 저장할 수 있다`() {
        // given
        val jobAssignment = EmployeeJobAssignmentEntityFixture.기본

        // when
        cut.save(jobAssignment)

        // then
        val actual = jdbcRepository.findAll()
        assertThat(actual).hasSize(1)
            .extracting(
                "employeeJobAssignmentId",
                "employeeId",
                "jobRoleId",
                "startDate",
                "endDate",
            ).containsExactly(
                Tuple.tuple(
                    1L,
                    0L,
                    1L,
                    LocalDate.parse("2025-01-01"),
                    LocalDate.parse("2025-01-31"),
                ),
            )
    }

    @Nested
    @DisplayName("구성원 ID 가 일치하면서 특정 날짜에 직무 발령 정보를 조회할 때")
    inner class findByEmployeeIdAndDateBetween {

        @ParameterizedTest
        @ValueSource(strings = ["2025-01-01", "2025-01-31"])
        fun `구성원의 발령 정보가 존재하면 반환 한다`(targetDateString: String) {
            // given
            val jobRole = jobRoleRepository.save(JobRoleFixture.기본.toEntity())
            jdbcRepository.save(EmployeeJobAssignmentEntityFixture.기본.toEntity(jobRole.id))
            val employeeIdentity = EmployeeIdentity.of(0)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = cut.findByEmployeeIdAndDateBetween(employeeIdentity, targetDate)

            // then
            assertThat(actual).isNotNull
                .extracting(
                    "startDate",
                    "endDate",
                    "jobRoleId",
                    "jobName",
                ).containsExactly(
                    LocalDate.parse("2025-01-01"),
                    LocalDate.parse("2025-01-31"),
                    jobRole.id,
                    "직무 1",
                )
        }

        @ParameterizedTest
        @ValueSource(strings = ["2024-12-31", "2025-02-01"])
        fun `구성원의 발령 정보가 없다면 null 을 반환 한다`(targetDateString: String) {
            // given
            jobRoleRepository.save(JobRoleFixture.기본.toEntity())
            jdbcRepository.save(EmployeeJobAssignmentEntityFixture.기본.toEntity())
            val employeeIdentity = EmployeeIdentity.of(0)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = cut.findByEmployeeIdAndDateBetween(employeeIdentity, targetDate)

            // then
            assertThat(actual).isNull()
        }
    }

    @Nested
    inner class `구성원 ID 로 해당 구성원의 모든 직무 변경 이력을 조회할 때` {

        @Test
        fun `직무 변경 이력이 존재하면 모든 이력을 반환 한다`() {
            // given
            val jobRole = jobRoleRepository.save(JobRoleFixture.기본.toEntity())
            jdbcRepository.save(EmployeeJobAssignmentEntityFixture.기본.toEntity(jobRole.id))
            val employeeIdentity = EmployeeIdentity.of(0)

            // when
            val actual = cut.findByEmployeeId(employeeIdentity)

            // then
            assertThat(actual).hasSize(1)
                .extracting(
                    "startDate",
                    "endDate",
                    "jobRoleId",
                    "jobName",
                ).containsExactly(
                    Tuple.tuple(
                        LocalDate.parse("2025-01-01"),
                        LocalDate.parse("2025-01-31"),
                        jobRole.id,
                        "직무 1",
                    ),
                )
        }

        @Test
        fun `직무 변경 이력이 없다면 빈 List 를 반환 한다`() {
            // given
            val employeeIdentity = EmployeeIdentity.of(0)

            // when
            val actual = cut.findByEmployeeId(employeeIdentity)

            // then
            assertThat(actual).isEmpty()
        }
    }
}
