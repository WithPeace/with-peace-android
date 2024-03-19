plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.compose")
}

android {
    namespace = "com.withpeace.withpeace.core.ui"
}

dependencies {
    implementation(project(":core:domain"))
}
