plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.compose")
}

android {
    namespace = "com.withpeace.withpeace.core.permission"
}

dependencies{
    implementation(project(":core:designsystem"))
}
