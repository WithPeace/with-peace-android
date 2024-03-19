plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.test.library")
    id("convention.coroutine")
    id("convention.android.compose")
    id("convention.android.hilt")
}

dependencies{
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    testImplementation(project(":core:testing"))
}
