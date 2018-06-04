pipeline {
  agent any
  stages {
    stage('Init Build') {
      steps {
        sh './gradlew setupCiWorkspace'
      }
    }
    stage('Build') {
      steps {
        sh './gradlew build'
      }
    }
    stage('') {
      steps {
        archiveArtifacts 'build/libs/*'
      }
    }
  }
}