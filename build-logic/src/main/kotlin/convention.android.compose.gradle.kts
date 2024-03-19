plugins {
    id("convention.android.base")
}
android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion =
            libs.findVersion("androidxComposeCompiler").get().toString()
    }
}

dependencies {
    val bom = libs.findLibrary("androidx-compose-bom").get()
    "implementation"(platform(bom))
    "androidTestImplementation"(platform(bom))

    "implementation"(libs.findLibrary("androidx.activity.compose").get())
    "implementation"(libs.findLibrary("androidx.compose.material3").get())
    "implementation"(libs.findLibrary("androidx.constraintlayout").get())
    "implementation"(libs.findLibrary("androidx.compose.ui").get())
    "implementation"(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
    "implementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
    "implementation"(libs.findLibrary("androidx.compose.icon").get())
    "implementation"(libs.findLibrary("androidx.compose.navigation").get())

    "androidTestImplementation"(libs.findLibrary("androidx.compose.ui.test").get())
    "androidTestImplementation"(libs.findLibrary("androidx-compose-navigation-test").get())

    "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
    "debugImplementation"(libs.findLibrary("androidx-compose-ui-testManifest").get())
}
