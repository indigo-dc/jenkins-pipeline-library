#!/usr/bin/groovy

/**
 * Builds a Docker image.
 *
 * @param repository Docker registry's repository name
 * @param scm_branch Tag for the Docker image (aligned with SCM branch)
 * @param dockerfile_args Build args for Docker image creation
 * @param dockerfile_dir Dockerfile location
 * @return Docker image ID
 */
def call(repository, scm_branch, dockerfile_args=[], dockerfile_dir='.') {
    if (scm_branch in ['master', 'latest', 'null', null]) {
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
