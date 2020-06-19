@Library(['github.com/WORSICA/jenkins-pipeline-library@docker-compose']) _

def projectConfig

pipeline {
    agent any

    options {
        buildDiscarder(logRotator(daysToKeepStr: '7', numToKeepStr: '1'))
    }

    stages {
        
        stage('Dynamic Stages') {
            steps {
                script {
                    projectConfig = pipelineConfig('.sqa/config.yml')
                    buildStages(projectConfig)
                }
            }
            post {
                cleanup {
                    cleanWs()
                }
            }
        }

    }

}
