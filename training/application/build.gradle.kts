/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

dependencies {
    implementation(project(":training:api"))
    implementation(project(":training:repository-jdbc"))
    runtimeOnly("com.h2database:h2")

    integrationTestImplementation("org.testcontainers:mysql")
    integrationTestRuntimeOnly("com.mysql:mysql-connector-j") {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
}
