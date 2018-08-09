def call(body) {
    def params= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = params
    body()

    pipeline {
        agent any
        stages {
            stage('Environment setup') {
                steps {
					PipRequirements(params.pip_test_reqs, 'test-requirements.txt')
                	PipRequirements(params.pip_reqs, 'requirements.txt')
                	ToxConfig(params.tox_envs)
                }
                post {
                    always {
                        archiveArtifacts artifacts: '*.requirements.txt,*.tox.*ini'
                    }
                }
            }

            stage('Style analysis') {
                steps {
                    ToxEnvRun('pep8')
                }
                post {
                    always {
                        WarningsReport('pep8')
                    }
                }
            }
        }
    }
}
