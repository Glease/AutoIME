import org.gradle.language.cpp.CppSharedLibrary

plugins {
    `cpp-library`
    id("net.jsign") version "6.0"
}

version = rootProject.version

library {
    baseName = "autoime"
    targetMachines.add(machines.windows.x86_64)
    linkage = listOf(Linkage.SHARED)
}

tasks.withType(CppCompile::class) {
    val javaHome =
        project.rootProject.tasks.named<JavaCompile>("compileJava").flatMap {
            it.javaCompiler.map { c -> c.metadata.installationPath }
        }
    includes(javaHome.map { "$it/include" })
    includes(targetPlatform.flatMap {
        if (it.operatingSystem.isWindows)
            javaHome.map { h -> "$h/include/win32" }
        else
            provider { null }
    })
    dependsOn(":compileJava")
    includes(project.rootProject.tasks.named<JavaCompile>("compileJava").flatMap {
        it.options.headerOutputDirectory
    })
}

task("sign") {
    dependsOn("linkRelease")
    onlyIf {
        project.hasProperty("keyStore")
    }
    project.components.named<CppLibrary>("main").invoke {
        binaries.whenElementKnown(CppSharedLibrary::class) {
            if (this.isOptimized) {
                inputs.file(runtimeFile)
            }
        }
    }
    doLast {
        val jsign = project.extensions.getByName("jsign") as groovy.lang.Closure<*>
        val toSign = inputs.files
        for (output in toSign) {
            println("signing $output")
            jsign("file"      to output.absolutePath,
                "name"      to "AutoIME runtime library",
                "url"       to "https://github.com/Glease/AutoIME",
                "keystore"  to project.property("keyStore"),
                "alias"     to project.property("keyStoreAlias"),
                "storepass" to project.property("keyStoreKeyPass"),
                "tsaurl"    to "http://timestamp.sectigo.com")
        }
    }
}
