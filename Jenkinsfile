@Library(['github.com/indigo-dc/jenkins-pipeline-library@fix/detached_head']) _

def projectConfig

pipeline {
    agent any

    stages {
        stage('SQA baseline dynamic stages') {
            steps {
                script {
                    projectConfig = pipelineConfig(
                        './.sqa/config.yml',
                        null,
                        null,
                        null,
                        'eoscsynergy/jpl-validator:jib-with-jpl',
			[ localBranch: true ]
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
                    branch 'release/*'
                    branch 'feature/*'
                    branch 'fix/*'
                }
            }
            steps {
                build 'eosc-synergy-org/sqaaas-api-spec/prototype                                                                                                                                                                                                                                                    0.0000001.0'
            }
            post {
                cleanup {
                    cleanWs()
                }
            }
        }
    }
}