package org.junit.gen5.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

import org.junit.gen5.launcher.listeners.*
import org.junit.gen5.launcher.*

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

			project.task('junit5Test', group: 'verification') { task -> //}, type: org.gradle.api.tasks.JavaExec, overwrite: true) { task ->

				def classpathRoots = project.sourceSets.test.runtimeClasspath.files

				classpathRoots.each { file ->
					task.inputs.file(file)
				}
				task.outputs.file testReport

				task.description = 'Runs JUnit 5 tests.'

				task.dependsOn project.tasks.getByName('testClasses')

				doLast {
					def summary = new TestExecutionSummary();
					def listener = new SummaryCreatingTestListener(summary);

					def roots = classpathRoots.findAll { file -> file.isDirectory() && file.exists() }
					def specification = TestPlanSpecification.build(TestPlanSpecification.allTests(roots))

					reportsDir.mkdirs()
					testReport.withPrintWriter {writer ->
						summary.printFailuresOn(writer)
						summary.printOn(writer)
					}
					//throw new GradleException('tests failed')
				}
			}
		}
	}
}

