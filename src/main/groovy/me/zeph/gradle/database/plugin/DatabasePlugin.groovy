package me.zeph.gradle.database.plugin

import me.zeph.gradle.database.extension.DatabasePluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class DatabasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.add('database', DatabasePluginExtension)
        project.task('createDB') << {
            println project.hello.message
        }
        project.task('')
    }
}
