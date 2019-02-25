#!/usr/bin/groovy

/**
 * Pushes Docker images to Docker Hub registry.
 *
 * @param image Image ID [mandatory]
 * @param url Docker registry URL [default]
 * @see https://plugins.jenkins.io/docker-workflow
 */
def call(String image, String url='') {
    withDockerRegistry([credentialsId: 'indigobot', url: url]) {
        sh "docker push $image"
    }
}
