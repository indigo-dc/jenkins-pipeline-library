#!/usr/bin/groovy

/**
 * Builds a Docker image.
 *
 * @param repository Docker registry's repository name [mandatory]
 * @param tag Tag for the Docker image (aligned with SCM branch) [List, String] [named, optional]
 * @param build_args Build args for Docker image creation [List] [named, optional]
 * @param build_dir Dockerfile location [String] [named, optional]
 * @return Docker image ID
 */
def call(Map docker=[:], String repository) {
    ids = [] 
    if (docker.tag instanceof List) {
        docker.tag.each {
            ids.push((repository + ':' + it).toLowerCase())
        }
    }
    else if (docker.tag in ['master', 'latest', 'null', null]) {
        ids.push((repository + ':latest').toLowerCase())
    }
    else {
        ids.push((repository + ':' + docker.tag).toLowerCase())
    }
    id_str = ids.join(' -t ')

    build_args = ''
    docker.build_args.each {
        build_args += "--build-arg ${it} "
    }

    cmd = "docker build --no-cache --force-rm -t $id_str $build_args"
    cmd = cmd.trim()

    if (docker.build_dir == null) {
        docker.build_dir = "."
    }
    dir(docker.build_dir) {
        sh "$cmd $docker.build_dir"
    }

    return id
}
