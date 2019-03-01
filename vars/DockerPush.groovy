#!/usr/bin/groovy

/**
 * Pushes Docker images to Docker Hub registry.
 *
 * @param image_ids List of Docker image IDs [mandatory]
 * @param url Docker registry URL [default]
 * @see https://plugins.jenkins.io/docker-workflow
 */
def call(List image_ids, String url='') {
    withDockerRegistry([credentialsId: 'indigobot', url: url]) {
        image_ids.each {
            sh "docker push ${it}"
        }
    }
}
