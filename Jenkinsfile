pipeline {
  agent { label 'hi-speed' }
  stages {
    stage('junit5-migration-gradle') {
      tools {
        jdk 'Oracle JDK 8 (latest)'
      }
      steps {
        sh 'cd junit5-migration-gradle && ./gradlew --no-daemon clean build'
      }
      post {
        always {
          junit 'junit5-migration-gradle/build/test-results/test/*.xml'
        }
      }
    }
    stage('junit5-migration-maven') {
      tools {
        jdk 'Oracle JDK 8 (latest)'
      }
      steps {
        sh 'cd junit5-migration-maven && ./mvnw -B clean verify'
      }
      post {
        always {
          junit 'junit5-migration-maven/target/surefire-reports/*.xml'
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
