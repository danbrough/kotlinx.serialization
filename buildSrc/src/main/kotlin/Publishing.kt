/*
 * Copyright 2016-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("UnstableApiUsage")

import org.gradle.api.*
import org.gradle.api.artifacts.dsl.*
import org.gradle.api.provider.*
import org.gradle.api.publish.maven.*
import org.gradle.plugins.signing.*
import java.net.*

// Pom configuration

infix fun <T> Property<T>.by(value: T) {
    set(value)
}

fun MavenPom.configureMavenCentralMetadata(project: Project) {
    name by project.name
    description by "Kotlin multiplatform serialization runtime library"
    url by "https://github.com/Kotlin/kotlinx.serialization"

    licenses {
        license {
            name by "The Apache Software License, Version 2.0"
            url by "https://www.apache.org/licenses/LICENSE-2.0.txt"
            distribution by "repo"
        }
    }

    developers {
        developer {
            id by "JetBrains"
            name by "JetBrains Team"
            organization by "JetBrains"
            organizationUrl by "https://www.jetbrains.com"
        }
    }

    scm {
        url by "https://github.com/Kotlin/kotlinx.serialization"
    }
}

fun mavenRepositoryUri(): URI {
    return URI("https://s01.oss.sonatype.org/service/local/staging/deployByRepositoryId/${System.getenv("SONATYPE_REPO_ID")}")
}

fun configureMavenPublication(rh: RepositoryHandler, project: Project) {
    rh.maven {
        url = mavenRepositoryUri()
        credentials {
            username = project.getSensitiveProperty("sonatypeUsername")
            password = project.getSensitiveProperty("sonatypePassword")
        }
    }
}

fun signPublicationIfKeyPresent(project: Project, publication: MavenPublication) {

    if (project.hasProperty("signPublications"))
    project.extensions.configure<SigningExtension>("signing") {
      sign(publication)
    }
	/*
    val keyId = project.getSensitiveProperty("libs.sign.key.id")
    val signingKey = project.getSensitiveProperty("libs.sign.key.private")
    val signingKeyPassphrase = project.getSensitiveProperty("libs.sign.passphrase")
    if (!signingKey.isNullOrBlank()) {
        project.extensions.configure<SigningExtension>("signing") {
            useInMemoryPgpKeys(keyId, signingKey, signingKeyPassphrase)
            sign(publication)
        }
    }
    */
}

private fun Project.getSensitiveProperty(name: String): String? {
    return project.findProperty(name) as? String ?: System.getenv(name)
}
