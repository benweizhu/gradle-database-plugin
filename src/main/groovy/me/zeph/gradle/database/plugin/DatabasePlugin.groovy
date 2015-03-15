package me.zeph.gradle.database.plugin

import me.zeph.gradle.database.extension.DatabasePluginExtension
import me.zeph.gradle.database.task.CreateDatabaseTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class DatabasePlugin implements Plugin<Project> {

    static final String EXTENSION_NAME = 'database'
    static final String CREATE_DB = 'createDB'

    @Override
    void apply(Project project) {
        project.extensions.add(EXTENSION_NAME, DatabasePluginExtension)
        project.task(CREATE_DB, type: CreateDatabaseTask)
    }
}
