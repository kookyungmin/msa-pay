version = "0.0.1"
description = "HappyPay Logging Consumer"

dependencies {
    implementation(project(":common"))
    runtimeOnly("com.h2database:h2")
}

//Docker image build
jib {
    from {
        image = "eclipse-temurin:17-jre"
        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }

    to {
        image = "${rootProject.name}-${project.name}:${project.version}"
    }

    container {
        mainClass = "net.happykoo.loggingconsumer.LoggingConsumerApplication"
        ports = listOf("8080")
    }
}
