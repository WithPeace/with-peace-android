plugins {
    id("convention.feature")
}

android {
    namespace = "com.withpeace.withpeace.feature.signup"
}

dependencies {
    implementation(project(":core:permission"))
    implementation(libs.skydoves.landscapist.bom)
    implementation(libs.skydoves.landscapist.glide)
}