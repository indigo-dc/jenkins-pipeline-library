#!/usr/bin/groovy

def call(repository, scm_branch, dockerfile_args=[], dockerfile_dir='.') {
    if (scm_branch in ['master', 'null', null]) {
        id = repository + ':latest'
    }
    else {
        id = repository + ':' + scm_branch
    }
    id = id.toLowerCase()
    
    build_args = ''
    dockerfile_args.each {
        build_args += "--build-arg tag=${it} "
    }

    cmd = "docker build --no-cache --force-rm -t $id $build_args"
    cmd = cmd.trim()

    dir(dockerfile_dir) {
        sh "$cmd ."
    }

    return id
}
