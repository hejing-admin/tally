plugins {
    id("com.android.application")
}

android {
    namespace = "com.hejing.tally"
    // 指定编译工具的版本号
    compileSdk = 33

    defaultConfig {
        // 指定该模块的应用编号，也就是App的别名
        applicationId = "com.hejing.tally"
        // 指定App适合运行的最小的sdk版本号
        minSdk = 24
        // 指定目标设备的sdk版本号，表示app最希望在哪个版本的Android上运行
        targetSdk = 33
        // 指定app的应用版本号
        versionCode = 1
        // 指定app的应用版本名称
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// 指定app编译的依赖信息
dependencies {
    // 添加MPAndroidChart 依赖
    implementation("com.github.PhilJay:MPAndroidChart:v3.0.3")
    // 指定编译android的高版本支持库。如: AppcompatActivity必须指定编译appcompat库
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}





















