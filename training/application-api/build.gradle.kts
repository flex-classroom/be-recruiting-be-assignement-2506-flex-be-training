/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

dependencies {
    implementation(project(":training:schema"))
    implementation(project(":training:api"))
    implementation(project(":training:repository-jdbc"))

    implementation("org.testcontainers:mysql")
    integrationTestImplementation(project(":training:infrastructure"))
    runtimeOnly("com.mysql:mysql-connector-j") {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
}
