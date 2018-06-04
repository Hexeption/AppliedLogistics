pipeline {
  agent any
  stages {
    stage('Init Build') {
      steps {
        sh 'chmod +x gradlew'
        sh './gradlew setupCiWorkspace'
      }
    }
    stage('Build') {
      steps {
        sh './gradlew build'
      }
    }
    stage('error') {
      steps {
        archiveArtifacts 'build/libs/*'
      }
    }
  }
}