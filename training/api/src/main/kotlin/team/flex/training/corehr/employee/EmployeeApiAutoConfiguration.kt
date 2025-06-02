/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.employee

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Import

@Import(
    EmployeeApiController::class,
)
@AutoConfiguration
class EmployeeApiAutoConfiguration
