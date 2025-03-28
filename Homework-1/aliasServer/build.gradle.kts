// aliasServer/build.gradle.kts

group = rootProject.group
version = rootProject.version

// --- THIS IS THE STRUCTURE YOU NEED ---
plugins {
    java
    id("org.jetbrains.kotlin.jvm")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlin.plugin.spring")

    // === ADD THIS LINE TO APPLY THE PLUGIN ===
    id("org.jetbrains.kotlin.plugin.serialization")
    // === END ADDITION ===
}

dependencies {
    implementation(project(":common"))
    implementation(project(":utils"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")

    // === ADD THIS LINE FOR THE JSON LIBRARY ===
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1") // <-- CHANGE VERSION HERE
    // === END ADDITION ===
}
// --- END NEEDED STRUCTURE ---


// --- Optional but recommended additions/checks from previous steps ---
tasks.named("processResources") {
    dependsOn(":aliasFrontend:build") // Adjust if your frontend module name is different
}

sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("main").resources.srcDirs("src/main/resources")
    getByName("test").java.srcDirs("src/test/kotlin")
    getByName("test").resources.srcDirs("src/test/resources")
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    mainClass.set("jetbrains.kotlin.course.alias.AliasApplicationKt") // Adjust main class if needed
}
// --- End optional additions ---
