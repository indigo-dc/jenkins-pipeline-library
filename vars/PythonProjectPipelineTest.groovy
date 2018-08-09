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
                        HTMLReport("$WORKSPACE/bandit", 'index.html', 'Bandit report')
                    }
                }
            }
        }
    }
}
