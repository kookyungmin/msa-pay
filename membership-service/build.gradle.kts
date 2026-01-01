version = "1.0-SNAPSHOT"
description = "HappyPay Membership"

dependencies {
    implementation(project(":common"))
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
}
