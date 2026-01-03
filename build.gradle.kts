plugins {
    id("java")
    id("org.springframework.boot") version "3.5.8"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.cloud.tools.jib") version "3.4.0"
}

allprojects {
    group = "net.happykoo"
    description = "HappyPay"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "com.google.cloud.tools.jib")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.kafka:spring-kafka")

        //Swagger
        implementation("org.springdoc:springdoc-openapi-starter-common:2.2.0")
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

        //Lombok
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        testRuntimeOnly("com.h2database:h2")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}

tasks {
    bootRun {
        enabled = false
    }

    bootJar {
        enabled = false
    }

    bootBuildImage {
        enabled = false
    }
}