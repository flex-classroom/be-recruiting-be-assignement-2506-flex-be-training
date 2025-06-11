package team.flex.training.corehr.support

import java.time.Clock
import java.time.Instant
import java.util.TimeZone

object TimeUtils {
    fun clock(instant: Instant = Instant.parse("2025-06-01T00:00:00Z")) = Clock.fixed(
        instant,
        TimeZone.getTimeZone("UTC").toZoneId(),
    )
}
