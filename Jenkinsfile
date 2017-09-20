pipeline {
  agent any
  tools {
    jdk 'Oracle JDK 8 (latest)' 
  }
  stages {
    stage('junit5-gradle-consumer') {
      steps {
        sh 'cd junit5-gradle-consumer && ./gradlew --no-daemon clean build'
      }
    }
    stage('junit5-maven-consumer') {
      steps {
        sh 'cd junit5-maven-consumer && ./mvnw -B clean verify'
      }
    }
    stage('junit5-mockito-extension') {
      steps {
        sh 'cd junit5-mockito-extension && ./gradlew --no-daemon clean build'
      }
    }
    stage('junit5-vanilla-gradle') {
      steps {
        sh 'cd junit5-vanilla-gradle && ./gradlew --no-daemon clean build'
      }
    }
    stage('junit5-vanilla-maven') {
      steps {
        sh 'cd junit5-vanilla-maven && ./mvnw -B clean verify'
      }
    }
  }
}
