package org.junit.gen5.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class JUnit5Plugin implements Plugin<Project> {
	void apply(Project project) {
		def junit5 = project.extensions.create('junit5', JUnit5Extension)

		project.afterEvaluate {

			def junit5Version = junit5.version
			project.dependencies.add("testCompile", "org.junit.prototype:junit5-api:${junit5Version}")
			project.dependencies.add("testRuntime", "org.junit.prototype:junit-console:${junit5Version}")
			project.dependencies.add("testRuntime", "org.junit.prototype:junit5-engine:${junit5Version}")

			if (junit5.runJunit4) {
				project.dependencies.add("testRuntime", "org.junit.prototype:junit4-engine:${junit5Version}")
			}

			def reportsDir = new File("build/test-results")
			def testReport = new File(reportsDir, "junit5-report.txt")

			project.task('junit5Test', group: 'verification', type: org.gradle.api.tasks.JavaExec, overwrite: true) { task ->

				//				def launcher = new org.junit.gen5.launcher.Launcher()
				//				println "LAUNCHER: " + launcher
				//def classpathRoots = project.sourceSets.test.runtimeClasspath.files

				task.outputs.file testReport

				task.description = 'Runs JUnit 5 tests.'
				task.classpath = project.sourceSets.test.runtimeClasspath
				task.main = 'org.junit.gen5.console.ConsoleRunner'
				task.args '--enable-exit-code', '--hide-details', '--all'

				// task.dependsOn project.tasks.getByName('testClasses')

				doLast {
					reportsDir.mkdirs()
					testReport.text = "Tests executed at ${new Date()}"
					//throw new GradleException('tests failed')
				}
			}
		}
	}
}

