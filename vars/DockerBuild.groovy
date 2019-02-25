#!/usr/bin/groovy

/**
 * Builds a Docker image.
 *
 * @param repository Docker registry's repository name [mandatory]
 * @param tag Tag for the Docker image (aligned with SCM branch) [named, optional]
 * @param build_args Build args for Docker image creation [named, optional]
 * @param build_dir Dockerfile location [named, optional]
 * @return Docker image ID
 */
def call(Map docker=[:], String repository) {
    if (docker.tag in ['master', 'latest', 'null', null]) {
        id = repository + ':latest'
    }
    else {
        id = repository + ':' + docker.tag
    }
    id = id.toLowerCase()
    
    build_args = ''
    docker.build_args.each {
        build_args += "--build-arg tag=${it} "
    }

    cmd = "docker build --no-cache --force-rm -t $id $build_args"
    cmd = cmd.trim()

    if (docker.build_dir == null) {
        docker.build_dir = "."
    }
    dir(docker.build_dir) {
        sh "$cmd $docker.build_dir"
    }

    return id
}
