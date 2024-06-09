plugins {
    id("java")
}

group = "ru.itmo.game"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.projectlombok:lombok:1.18.30")
    implementation("commons-cli:commons-cli:1.6.0")
    implementation("com.googlecode.lanterna:lanterna:3.1.2")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("javax.validation:validation-api:1.1.0.CR2")
    implementation("jakarta.validation:jakarta.validation-api:3.0.0")
    //implementation 'com.google.code.gson:gson:2.10.1'
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.0.1")
}


tasks.test {
    useJUnitPlatform()
}