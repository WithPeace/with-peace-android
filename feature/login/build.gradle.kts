plugins {
    id("convention.feature")
}

android {
    namespace = "com.withpeace.withpeace.feature.login"
}

dependencies {
    implementation(project(":google-login"))
}