plugins {
    id("com.gradle.plugin-publish") version "0.21.0"
    `java-gradle-plugin`
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    `maven-publish`
}

version = "1.0.0"
group = "com.esmaeel"

repositories { mavenCentral() }

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.api-client:google-api-client:1.23.0") {
        exclude(group = "com.google.guava", module = "guava-jdk5")
    }
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.23.0") {
        exclude(group = "com.google.guava", module = "guava-jdk5")
    }
    implementation("com.google.apis:google-api-services-drive:v3-rev110-1.23.0") {
        exclude(group = "com.google.guava", module = "guava-jdk5")
    }

}


gradlePlugin {
    plugins {
        create("FlyNow") {
            id = "com.esmaeel.flyNow"
            implementationClass = "flyNow.FlyNowPlugin"
            displayName = "./gradlew flyNow"
            description = "Painless ci management - build, rename, upload apk and notify slack out of the box"
        }
    }
}

pluginBundle {
    website = "https://github.com/EsmaeelNabil/FlyNow"
    vcsUrl = "https://github.com/EsmaeelNabil/FlyNow"
    description = "Painless ci management - build, rename, upload apk and notify slack out of the box"
    tags = listOf("generateApk", "Apk", "Slack", "GoogleDrive", "ci", "kotlin-dsl")
}

publishing {
    setupAllPublications(project)
}



