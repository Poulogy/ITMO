plugins {
    war
    java
}

group = "com.lab3"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {

    compileOnly("jakarta.platform:jakarta.jakartaee-web-api:10.0.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    implementation("jakarta.xml.ws:jakarta.xml.ws-api:4.0.0")
    implementation("jakarta.jws:jakarta.jws-api:3.0.0")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")

    implementation("com.sun.xml.ws:jaxws-rt:4.0.1")
    implementation("com.sun.xml.ws:jaxws-ri:4.0.1")

    implementation("com.sun.xml.bind:jaxb-impl:4.0.2")
    implementation("com.sun.xml.bind:jaxb-core:4.0.2")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("at.favre.lib:bcrypt:0.10.2")

    implementation("com.influxdb:influxdb-client-java:6.10.0")

}

tasks.war {
    archiveFileName.set("backend.war")
}