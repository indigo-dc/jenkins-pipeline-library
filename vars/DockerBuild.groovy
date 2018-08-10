#!/usr/bin/groovy
def call() {
    if (env.BRANCH_NAME == 'master') {
        IMAGE_ID = env.docker_repo + ':latest'
    }
    else {
        IMAGE_ID = env.docker_repo + ':' + env.TAG_NAME
    }
    // OJO AQUI, hardcoded
    IMAGE_ID = env.docker_repo + ':latest'
    
    sh "docker build --force-rm -t $IMAGE_ID ."
    return IMAGE_ID
}
