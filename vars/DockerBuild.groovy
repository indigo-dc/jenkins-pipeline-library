#!/usr/bin/groovy

def call(repository, scm_branch, dockerfile_dir='.') {
    if (scm_branch in ['master', 'null', null]) {
        id = repository + ':latest'
    }
    else {
        id = repository + ':' + scm_branch
    }
    id = id.toLowerCase()
    
    dir(dockerfile_dir) {
        sh "docker build --no-cache --force-rm -t $id ."
    }

    return id
}
