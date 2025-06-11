package team.flex.training.corehr.support

import org.junit.jupiter.api.BeforeEach
import java.util.TimeZone

interface UTCTimeZoneSupport {

    @BeforeEach
    fun setUpTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }
}
