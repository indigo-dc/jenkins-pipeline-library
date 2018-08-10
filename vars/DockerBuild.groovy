#!/usr/bin/groovy
def call(repository, scm_branch) {
    if (scm_branch == 'master') {
        IMAGE_ID = repository + ':latest'
    }
    else {
        IMAGE_ID = repository + ':' + scm_branch
    }
    // OJO AQUI, hardcoded
    IMAGE_ID = repository + ':latest'
    
    sh "docker build --force-rm -t $IMAGE_ID ."
    return IMAGE_ID
}
