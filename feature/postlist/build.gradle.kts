plugins {
   id("convention.feature")
}

android {
    namespace = "com.withpeace.withpeace.feature.postlist"
}

dependencies {
    implementation(libs.skydoves.landscapist.bom)
    implementation(libs.skydoves.landscapist.glide)
}
