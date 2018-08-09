def call(body) {
    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    if (pipelineParams.buildNumber == 0) {
        def file = new File('/tmp/requirements.txt')
        file << 'Simple file writing with groovy.\n'
    }
    pipeline {
        agent any
        stages {
            stage('Unit test') {
                steps {
                    sh 'cat /tmp/requirements.txt'
                }
            }
        }
    }
}
