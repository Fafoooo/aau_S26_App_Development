plugins {
    kotlin("jvm") version "2.1.10"
    application
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("ContactsKt")
}

sourceSets {
    main {
        kotlin {
            setSrcDirs(listOf("."))
            exclude("*.kts")
        }
    }
}
