#!/usr/bin/groovy

/**
 * Pushes Docker images to a Docker registry.
 *
 * @param  image_ids List of Docker image IDs [mandatory]
 * @param  url Docker registry URL [default]
 * @see https://plugins.jenkins.io/docker-workflow
 */
def push_docker_images(List image_ids, String url='') {
    withDockerRegistry([credentialsId: 'indigobot', url: url]) {
        image_ids.each {
            sh "docker push ${it}"
        }
    }
}


def call(String image_id, String url='') {
    push_docker_images([image_id], url) 
}


def call(List image_id, String url='') {
    push_docker_images(image_id, url)
}
