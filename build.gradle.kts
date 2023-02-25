plugins {
    kotlin("jvm") version "1.8.10"
    application
}

group = "io.github.superjoy0502.tarotbot"
version = "1.0.0a"

repositories {
    mavenCentral()
    maven {
        name = "m2-dv8tion"
        url = uri("https://m2.dv8tion.net/releases")
    }
    maven("https://jitpack.io/")
}

dependencies {
    testImplementation(kotlin("test"))
    // https://github.com/DV8FromTheWorld/JDA
    implementation("net.dv8tion:JDA:5.0.0-beta.4") {
        exclude(module = "opus-java")
    }
    implementation("ch.qos.logback:logback-classic:1.2.9")
    // https://github.com/kotlin/kotlinx.coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    // https://github.com/MinnDevelopment/jda-ktx
    implementation("com.github.minndevelopment:jda-ktx:0.10.0-beta.1")
    // https://github.com/Aallam/openai-kotlin
    implementation("com.aallam.openai:openai-client:2.1.3")
    implementation("io.ktor:ktor-client-okhttp:2.2.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("io.github.superjoy0502.tarotbot.Bot")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}