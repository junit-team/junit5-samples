plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileJava {
        options.release.set(11)
    }
}
