package me.zeph.gradle.database.plugin

import me.zeph.gradle.database.extension.DatabasePluginExtension
import me.zeph.gradle.database.task.CreateDatabaseTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class DatabasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.add('database', DatabasePluginExtension)
        project.task('createDB', type: CreateDatabaseTask)
    }
}
