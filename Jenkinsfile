pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning the repository...'
                git branch: 'main', url: 'https://github.com/junit-team/junit5-samples.git'
            }
        }
        stage('Build') {
            steps {
                echo 'Building the project...'
                sh './gradlew build'
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
                sh './gradlew test'
            }
        }
        stage('Archive Results') {
            steps {
                echo 'Archiving test results...'
                junit '**/build/test-results/**/*.xml'
            }
        }
    }
    post {
        always {
            echo 'Pipeline execution completed.'
        }
        success {
            echo 'Build and tests were successful!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
