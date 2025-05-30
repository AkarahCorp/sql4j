plugins {
    id("java")
}

group = "net.akarah"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.postgresql:postgresql:42.7.5")
    implementation("com.google.code.gson:gson:2.13.1")
}

tasks.test {
    useJUnitPlatform()
}