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
                    //checkout scm
                    sh 'git clone https://github.com/indigo-dc/im'
                }
            }

            stage('Environment setup') {
                steps {
                    dir("$WORKSPACE/im") {
	    			    PipRequirements(params.pip_test_reqs, 'test-requirements.txt')
                	    PipRequirements(params.pip_reqs, 'requirements.txt')
                	    ToxConfig(params.tox_envs)
                    }
                }
                post {
                    always {
                        dir("$WORKSPACE/im") {
                            archiveArtifacts artifacts: '*requirements.txt,*tox*.ini'
                        }
                    }
                }
            }

            stage('Style analysis') {
                steps {
                    dir("$WORKSPACE/im") {
                        ToxEnvRun('pep8')
                    }
                }
                post {
                    always {
                        dir("$WORKSPACE/im") {
                            WarningsReport('pep8')
                        }
                    }
                }
            }

            stage('Unit testing coverage') {
                steps {
                    dir("$WORKSPACE/im") {
                        ToxEnvRun('unit')
                    }
                }
                post {
                    success {
                        dir("$WORKSPACE/im") {
                            CoberturaReport()
                        }
                    }
                }
            }

            stage('Functional testing') {
                steps {
                    dir("$WORKSPACE/im") {
                        ToxEnvRun('functional')
                    }
                }
            }

            stage('Security scanner') {
                steps {
                    dir("$WORKSPACE/im") {
                        ToxEnvRun('bandit')
                        script {
                            if (currentBuild.result == 'FAILURE') {
                                currentBuild.result = 'UNSTABLE'
                            }
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
                /*
                when {
                    anyOf {
                        branch 'master'
                        buildingTag()
                    }
                }*/
                agent {
                    label 'docker-build'
                }
                steps {
                    //checkout scm
                    sh 'git clone https://github.com/indigo-dc/DEEPaaS'
                    dir("$WORKSPACE/DEEPaaS") {
                        DockerBuild() 
                    }
                }
                post {
                    success {
                        echo "Pushing Docker image ${IMAGE_ID}.."
                        withDockerServer([credentialsId: '', uri: "tcp://127.0.0.1:2376"]) {
                            withDockerRegistry([credentialsId: 'indigobot', url: '']) {
                                sh "${docker_alias} push $IMAGE_ID"
                            }
                        }
                    }
                    failure {
                        echo 'Docker image building failed, removing dangling images..'
                        sh '${docker_alias} rmi \$(\${docker_alias} images -f "dangling=true" -q)'
                    }
                    always {
                        cleanWs()
                    }
                }
        }
    }
}
