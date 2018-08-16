#!/usr/bin/groovy

def call(repository, scm_branch, image_id=null) {
    if (image_id != null) {
        id = image_id
    }
    else {
        if (scm_branch == 'master') {
            id = repository + ':latest'
        }
        else {
            id = repository + ':' + scm_branch
        }
    }
    
    sh "docker build --force-rm -t $id ."
    return id
}
