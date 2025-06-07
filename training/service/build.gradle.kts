/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

allOpen {
    annotation("team.flex.training.corehr.global.annotation.Transactional")
}

dependencies {
    api(project(":training:model"))
    implementation(project(":training:exception"))
    implementation(project(":training:infrastructure"))

    integrationTestImplementation(project(":training:repository-jdbc"))
    integrationTestImplementation("org.testcontainers:mysql")
    integrationTestImplementation(project(":training:schema"))
    integrationTestRuntimeOnly("com.mysql:mysql-connector-j") {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
}
