/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr

import java.time.Instant

interface AuditProps {
    val createdAt: Instant
    val updatedAt: Instant
}
