/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

rootProject.name = "flex-be-training"

include(":training:api")
include(":training:application-api")
include(":training:exception")
include(":training:infrastructure")
include(":training:model")
include(":training:repository-jdbc")
include(":training:schema")
include(":training:service")

pluginManagement {
    buildscript {
        repositories {
            gradlePluginPortal()
        }
    }

    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
