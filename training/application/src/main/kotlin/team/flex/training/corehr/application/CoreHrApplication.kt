/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.Clock

@SpringBootApplication
class CoreHrApplication {
    @Bean
    fun clock() = Clock.systemUTC()
}

fun main(args: Array<String>) {
    runApplication<CoreHrApplication>(*args)
}
