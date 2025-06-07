/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.employee

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.flex.training.corehr.assignment.AssignmentCommandService
import team.flex.training.corehr.assignment.command.EmployeeAssignmentCreateCommand
import team.flex.training.corehr.employee.dto.EmployeeAssignmentCreateRequest
import team.flex.training.corehr.employee.dto.EmployeeAssignmentHistoryResponse
import team.flex.training.corehr.employee.dto.EmployeeAssignmentResponse
import java.time.LocalDate

@RestController
@RequestMapping("/api/v2/corehr")
class EmployeeApiController(
    private val assignmentCommandService: AssignmentCommandService,
) {

    @PostMapping("/companies/{companyId}/employees/{employeeId}/employee-assignment")
    @Operation(
        summary = "구성원의 인사 정보 생성 (직무 발령 / 부서 발령) API",
        operationId = "createEmployeeAssignment",
    )
    fun createEmployeeAssignment(
        @PathVariable companyId: Long,
        @PathVariable employeeId: Long,
        @RequestBody request: EmployeeAssignmentCreateRequest,
    ) = assignmentCommandService.appointJob(
        EmployeeAssignmentCreateCommand(
            companyId,
            employeeId,
            request.startDate,
            request.endDate,
            request.jobRoleId,
            request.departmentId,
        ),
    )

    @GetMapping("/companies/{companyId}/employees/{employeeId}/employee-assignment")
    @Operation(
        summary = "요청일 기준의 구성원의 인사 정보를 조회하는 API",
        operationId = "getEmployeeAssignmentByTargetDate",
    )
    fun getEmployeeAssignment(
        @PathVariable companyId: Long,
        @PathVariable employeeId: Long,
        @RequestParam targetDate: LocalDate,
    ): EmployeeAssignmentResponse {
        TODO("구현해주세요")
    }

    @GetMapping("/companies/{companyId}/employees/{employeeId}/employee-assignment-history")
    @Operation(
        summary = "구성원의 인사 정보 이력을 조회하는 API",
        operationId = "getEmployeeAssignmentHistory",
    )
    fun getEmployeeAssignmentHistory(
        @PathVariable companyId: Long,
        @PathVariable employeeId: Long,
    ): EmployeeAssignmentHistoryResponse {
        TODO("구현해주세요")
    }
}
