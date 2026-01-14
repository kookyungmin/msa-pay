version = "0.0.1"
description = "HappyPay API Gateway"

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux")
    runtimeOnly("com.h2database:h2")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
    }
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
        mainClass = "net.happykoo.apigateway.ApiGatewayApplication"
        ports = listOf("8080")
    }
}
