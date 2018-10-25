#!/usr/bin/groovy

def call(repository, scm_branch, dockerfile_dir='.', image_id=null) {
    if (image_id != null) {
        id = image_id
    }
    else {
        if (scm_branch in ['master', 'null']) {
            id = repository + ':latest'
        }
        else {
            id = repository + ':' + scm_branch
        }
    }
    
    dir(dockerfile_dir) {
        sh "docker build --force-rm -t $id ."
    }

    return id
}
