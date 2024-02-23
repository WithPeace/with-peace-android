import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

fun getLocalPropertyString(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

plugins {
    id("com.android.library")
    id("convention.android.compose")
    id("convention.android.hilt")
}
val localProperties = Properties()
localProperties.load(project.rootProject.file("local.properties").inputStream())


android {
    namespace = "com.withpeace.google_login"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField(
            "String",
            "GOOGLE_CLIENT_ID",
            getLocalPropertyString("GOOGLE_CLIENT_ID"),
        )
    }
}

dependencies {
    implementation(libs.google.login)
    implementation(libs.multidex)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.service)
}
