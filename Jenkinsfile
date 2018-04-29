pipeline {
  agent { label 'hi-speed' }
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
          junit 'junit5-gradle-consumer/build/test-results/test/*.xml'
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
    stage('junit5-jupiter-extensions') {
      tools {
        jdk 'Oracle JDK 8 (latest)'
      }
      steps {
        sh 'cd junit5-jupiter-extensions && ./gradlew --no-daemon clean build'
      }
      post {
        always {
          junit 'junit5-jupiter-extensions/build/test-results/junit-platform/*.xml'
        }
      }
    }
    stage('junit5-jupiter-starter-gradle') {
      tools {
        jdk 'Oracle JDK 8 (latest)'
      }
      steps {
        sh 'cd junit5-jupiter-starter-gradle && ./gradlew --no-daemon clean build'
      }
      post {
        always {
          junit 'junit5-jupiter-starter-gradle/build/test-results/test/*.xml'
        }
      }
    }
    stage('junit5-jupiter-starter-maven') {
      tools {
        jdk 'Oracle JDK 8 (latest)'
      }
      steps {
        sh 'cd junit5-jupiter-starter-maven && ./mvnw -B clean verify'
      }
      post {
        always {
          junit 'junit5-jupiter-starter-maven/target/surefire-reports/*.xml'
        }
      }
    }
    stage('junit5-modular-world') {
      tools {
        jdk 'Oracle JDK 9'
      }
      steps {
        sh 'cd junit5-modular-world && ./build.jsh'
      }
      post {
        always {
          junit 'junit5-modular-world/bin/test-patch-compile-results/junit-platform/*.xml'
        }
      }
    }
  }
}
