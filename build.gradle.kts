import org.gradle.api.JavaVersion.VERSION_17
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.0.20"
    application
    id("info.solidsoft.pitest") version "1.15.0"
    jacoco
}

repositories {
    mavenCentral()
}

pitest {
//    pitestVersion = "1.15.0" // current defined for Gradle plugin PIT version should be used
    pitestVersion = "1.16.1" // Needed for arcmutate 1.3.0
    junit5PluginVersion = "1.2.0" // should support our Jupiter version https://github.com/pitest/pitest-junit5-plugin
    avoidCallsTo.set(setOf("kotlin.jvm.internal"))
    mutators = setOf("ALL")
    targetClasses = setOf("jasondecarvalho.snapshot.testing.*") // by default "${project.group}.*"
    targetTests = setOf("jasondecarvalho.snapshot.testing.*")
    threads = Runtime.getRuntime().availableProcessors()
    outputFormats = setOf("HTML")
    enableDefaultIncrementalAnalysis.set(false)
//    verbose = true
    dependencies {
        pitest("com.arcmutate:pitest-kotlin-plugin:1.3.0")
    }
}

tasks.test {
  useJUnitPlatform()
  environment(properties.filter { it.key == "selfie" }) // optional, see "Overwrite everything" below
  inputs.files(fileTree("src/test") { // optional, improves up-to-date checking
    include("**/*.ss")
  })
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass = "jasondecarvalho.snapshot.testing.SnapshotTestingKt"
}

repositories {
    mavenCentral()
}

tasks {
    withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            allWarningsAsErrors = false
            jvmTarget.set(JVM_17)
            freeCompilerArgs.add("-Xjvm-default=all")
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    java {
        sourceCompatibility = VERSION_17
        targetCompatibility = VERSION_17
    }

    jacocoTestReport {
        reports {
            html.required = true
        }
    }
}

dependencies {
    implementation(platform("org.http4k:http4k-bom:5.32.4.0"))
    implementation(platform("org.http4k:http4k-connect-bom:5.24.1.0"))
    implementation("org.http4k:http4k-connect-storage-jdbc")
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-undertow")
    implementation("org.http4k:http4k-template-handlebars")
    testImplementation("org.http4k:http4k-testing-hamkrest")
    testImplementation("org.http4k:http4k-format-jackson")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("com.diffplug.selfie:selfie-runner-junit5:2.4.1")
    testImplementation("org.mockito:mockito-core:3.0.0")
    testImplementation("org.jsoup:jsoup:1.17.1")
    testImplementation("com.vladsch.flexmark:flexmark-html2md-converter:0.64.8")
}