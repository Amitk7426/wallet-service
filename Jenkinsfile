pipeline {
  environment {
    registry = "gustavoapolinario/docker-test"
    registryCredential = 'dockerhub'
  }
  agent any
  stages {
    stage('checkout project') {
      steps {
        checkout scm
      }
    }
	stage('Building image') {
      steps{
        script {
          docker.build registry + ":$BUILD_NUMBER"
        }
      }
    }
    stage('Deploy Image') {
	  steps{    
		script {
			docker.withRegistry( '', registryCredential ) {
			dockerImage.push()
		}
	  }
    }
  }
    stage('report') {
      parallel {
        stage('report') {
          steps {
            junit 'target/surefire-reports/*.xml'
          }
        }
        stage('coverage') {
          steps {
            cobertura(coberturaReportFile: 'target/site/cobertura/coverage.xml')
          }
        }
      }
    }
    stage('package') {
      steps {
        sh 'docker-compose run package'
      }
    }
    stage('archive') {
      steps {
        archiveArtifacts 'target/spring-boot-sample-data-rest-0.1.0.jar'
      }
    }
    stage('deploy') {
      steps {
        sh '''make build-docker-prod-image make deploy-production-ssh '''
      }
    }
  }
  post {
    always {
      sh 'docker-compose run clean'
      echo 'I will always say Hello again!'
    }

    success {
      echo 'success!'

    }

    failure {
      echo 'failure!'

    }

  }
}