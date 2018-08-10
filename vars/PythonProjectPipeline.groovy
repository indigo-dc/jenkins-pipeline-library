#!/usr/bin/groovy
def call(body) {
    def params= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = params
    body()

    pipeline {
        agent {
            label 'python'
        }
        
        stages {
            stage('Code fetching') {
                steps {
                    checkout scm
                }
            }

            stage('Environment setup') {
                steps {
                    PipRequirements(params.pip_test_reqs, 'test-requirements.txt')
                    PipRequirements(params.pip_reqs, 'requirements.txt')
                    ToxConfig(params.tox_envs)
                }
                post {
                    always {
                        archiveArtifacts artifacts: '*requirements.txt,*tox*.ini'
                    }
                }
            }

            stage('Style analysis') {
                steps {
                    ToxEnvRun('pep8')
                }
                post {
                    always {
                        WarningsReport('Pep8')
                    }
                }
            }

            stage('Unit testing coverage') {
                steps {
                    ToxEnvRun('unit')
                }
                post {
                    success {
                        CoberturaReport()
                    }
                }
            }

            stage('Functional testing') {
                steps {
                    ToxEnvRun('functional')
                }
            }

            stage('Security scanner') {
                steps {
                    ToxEnvRun('bandit')
                    script {
                        if (currentBuild.result == 'FAILURE') {
                            currentBuild.result = 'UNSTABLE'
                        }
                    }
                }
                post {
                    always {
                        HTMLReport("/tmp/bandit", 'index.html', 'Bandit report')
                    }
                }
            }

            stage('DockerHub delivery') {
                when {
                    anyOf {
                        branch 'master'
                        buildingTag()
                    }
                }
                agent {
                    label 'docker-build'
                }
                steps {
                    checkout scm
                    script {
                        image_id = DockerBuild(dockerhub_repo, env.BRANCH_NAME)
                    }
                }
                post {
                    success {
                        echo "Pushing Docker image ${image_id}.."
                        DockerPush(image_id)
                    }
                    failure {
                        echo 'Docker image building failed, removing dangling images..'
                        DockerClean()
                    }
                    always {
                        cleanWs()
                    }
                }
            }
        }
    }
}
