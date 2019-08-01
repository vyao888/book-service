pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh '''pipeline {
    agent { docker { image \'maven:3.3.3\' } }
    stages {
        stage(\'build\') {
            steps {
                sh \'mvn --version\'
            }
        }
    }
}'''
        }
      }
    }
    environment {
      dev = 'book-service'
    }
  }