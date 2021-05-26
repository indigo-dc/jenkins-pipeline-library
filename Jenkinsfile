@Library(['github.com/indigo-dc/jenkins-pipeline-library@%s']) _

def projectConfig

pipeline {
    agent any

    stages {
        stage('SQA baseline dynamic stages') {
            when {
                anyOf {
                    branch 'jenkins/release/*'
                    branch 'jenkins/feature/*'
                    branch 'jenkins/fix/*'
                    branch 'jenkins/docs/*'
                }
            }
            steps {
                script {
                    projectConfig = pipelineConfig(
                        [ scmConfigs: [ localBranch: true ] ]
                    )
                    buildStages(projectConfig)
                }
            }
            post {
                cleanup {
                    cleanWs()
                }
            }
        }
        stage('Use case validation: <sqaaas-api-spec>') {
            when {
                anyOf {
                    branch 'jenkins/release/*'
                    branch 'jenkins/feature/*'
                    branch 'jenkins/fix/*'
                }
            }
            steps {
                build 'eosc-synergy-org/sqaaas-api-spec/prototype%%252F1.0'
            }
            post {
                cleanup {
                    cleanWs()
                }
            }
        }
        stage('Use case validation: <jpl-validator>') {
            when {
                anyOf {
                    branch 'jenkins/release/*'
                    branch 'jenkins/feature/*'
                    branch 'jenkins/fix/*'
                }
            }
            steps {
                build 'eosc-synergy-org/jpl-validator/master'
            }
            post {
                cleanup {
                    cleanWs()
                }
            }
        }
    }
}
