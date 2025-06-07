/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

dependencies {
    implementation(project(":training:infrastructure"))
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    integrationTestImplementation("org.testcontainers:mysql")
    integrationTestImplementation(project(":training:schema"))
    integrationTestRuntimeOnly("com.mysql:mysql-connector-j") {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
}
