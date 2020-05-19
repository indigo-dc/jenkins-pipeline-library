@Library(['github.com:WORSICA/jenkins-pipeline-library@docker-compose']) _

def projectConfig

pipeline {
    agent any

    options {
        buildDiscarder(logRotator(daysToKeepStr: '7', numToKeepStr: '1'))
    }

    environment {
        CONFIG_FILE = '.sqa/config.yml'
    }

    stages {
        
        stage('Dynamic Stages') {
            steps {
                script {
                    projectConfig = PipelineConfig('.sqa/config.yml')
                    BuildStages(projectConfig)
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
