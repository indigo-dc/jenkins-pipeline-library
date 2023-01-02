@Library(['github.com/indigo-dc/jenkins-pipeline-library@jenkins/release/2.2.0']) _

def projectConfig

pipeline {
    agent any

    stages {
        stage('SQA baseline dynamic stages') {
            when {
                anyOf {
                    branch 'jenkins/release/**'
                    branch 'jenkins/stable/**'
                    branch 'jenkins/feature/**'
                    branch 'jenkins/fix/**'
                    branch 'jenkins/docs/**'
                }
            }
            steps {
                script {
                    projectConfig = pipelineConfig(
                        [ validatorDockerImage: 'eoscsynergy/jpl-validator:2.2.0', scmConfigs: [ localBranch: true ] ]
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
                    branch 'jenkins/release/**'
                    branch 'jenkins/stable/**'
                    branch 'jenkins/feature/**'
                    branch 'jenkins/fix/**'
                }
            }
            steps {
                build 'eosc-synergy-org/sqaaas-api-spec/prototype%252F1.1-no-polymorphism'
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
                    branch 'jenkins/release/**'
                    branch 'jenkins/stable/**'
                    branch 'jenkins/feature/**'
                    branch 'jenkins/fix/**'
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