pipeline {
  agent any
  stages {
    stage('junit5-gradle-consumer') {
      tools {
        jdk 'Oracle JDK 8 (latest)'
      }
      steps {
        sh 'cd junit5-gradle-consumer && ./gradlew --no-daemon clean build'
      }
      post {
        always {
          junit 'junit5-gradle-consumer/build/test-results/junit-platform/*.xml'
        }
      }
    }
    stage('junit5-java9-engine') {
      tools {
        jdk 'Oracle JDK 9'
      }
      steps {
        sh 'cd junit5-java9-engine && ./gradlew --no-daemon clean build'
      }
      post {
        always {
          junit 'junit5-java9-engine/build/test-results/junit-platform/*.xml'
        }
      }
    }
    stage('junit5-maven-consumer') {
      tools {
        jdk 'Oracle JDK 8 (latest)'
      }
      steps {
        sh 'cd junit5-maven-consumer && ./mvnw -B clean verify'
      }
      post {
        always {
          junit 'junit5-maven-consumer/target/surefire-reports/*.xml'
        }
      }
    }
    stage('junit5-mockito-extension') {
      tools {
        jdk 'Oracle JDK 8 (latest)'
      }
      steps {
        sh 'cd junit5-mockito-extension && ./gradlew --no-daemon clean build'
      }
      post {
        always {
          junit 'junit5-mockito-extension/build/test-results/junit-platform/*.xml'
        }
      }
    }
    stage('junit5-vanilla-gradle') {
      tools {
        jdk 'Oracle JDK 8 (latest)'
      }
      steps {
        sh 'cd junit5-vanilla-gradle && ./gradlew --no-daemon clean build'
      }
      post {
        always {
          junit 'junit5-vanilla-gradle/build/test-results/junit-platform/*.xml'
        }
      }
    }
    stage('junit5-vanilla-maven') {
      tools {
        jdk 'Oracle JDK 8 (latest)'
      }
      steps {
        sh 'cd junit5-vanilla-maven && ./mvnw -B clean verify'
      }
      post {
        always {
          junit 'junit5-vanilla-maven/target/surefire-reports/*.xml'
        }
      }
    }
  }
}
