package net.justmili.dslprops

import org.gradle.api.Project

private fun Project.prop(key: String): String =
    property(key) as? String ?: error("Missing or non-string Gradle property '$key'")

val Project.root: Project
    get() = rootProject

val Project.modId: String
    get() = prop("mod.id")

val Project.modName: String
    get() = prop("mod.name")

val Project.modVersion: String
    get() = prop("mod.version")

val Project.modDesc: String
    get() = prop("mod.description")

val Project.modLicense: String
    get() = prop("mod.license")

val Project.modAuthor: String
    get() = prop("mod.authors")

val Project.baseName: String
    get() = prop("mod.archives_base_name")

val Project.mcVersion: String
    get() = name

fun Project.readChangelogFromBranch(branch: String, path: String): String {
    return try {
        val output = providers.exec {
            workingDir = rootProject.projectDir // `git show <branch>:<path>` resolves the path from the repo root
            commandLine("git", "show", "$branch:$path")
            isIgnoreExitValue = true
        }

        if (output.result.get().exitValue == 0) {
            output.standardOutput.asText.get().trim()
        } else {
            "No changelog found"
        }
    } catch (e: Exception) {
        "No changelog found"
    }
}

/** Shortcut for the Fabric-only layout: `.Changelogs/<mod.version>-Fabric.md` on the given branch. */
fun Project.readChangelog(branch: String = "origin/rep-info"): String =
    readChangelogFromBranch(branch, ".Changelogs/$modVersion-Fabric.md")