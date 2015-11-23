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

			project.task('junit5Test', group: 'verification', type: org.gradle.api.tasks.JavaExec) { task ->

				task.outputs.file testReport

				task.description = 'Runs JUnit 5 tests.'

				task.dependsOn project.tasks.getByName('testClasses')

				task.classpath = project.sourceSets.test.runtimeClasspath
				task.main = 'org.junit.gen5.console.ConsoleRunner'



				List args = buildArgs(project, junit5)

				task.args args

				doLast {

					reportsDir.mkdirs()
					testReport.withPrintWriter { writer ->
						writer.println "JUnit 5 tests run at " + new Date().toString()
					}

				}
			}
		}
	}

	private ArrayList<String> buildArgs(project, junit5) {

		def args = ['--enable-exit-code', '--hide-details', '--all']

		if (junit5.classNameFilter) {
			args.add('-n')
			args.add(junit5.classNameFilter)
		}

		//Does not work yet
		if (junit5.includeTags) {
			junit5.includeTags.each { tag ->
				args.add('-t')
				args.add(tag)
			}
		}

		def classpathRoots = project.sourceSets.test.runtimeClasspath.files
		def rootDirs = classpathRoots.findAll { it.isDirectory() && it.exists() }
		rootDirs.each { File root -> args.add(root.getAbsolutePath()) }

		return args
	}
}

