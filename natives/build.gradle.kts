plugins {
    `cpp-library`
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
            null
    })
    dependsOn(":compileJava")
    includes(project.rootProject.tasks.named<JavaCompile>("compileJava").flatMap {
        it.options.headerOutputDirectory
    })
}
