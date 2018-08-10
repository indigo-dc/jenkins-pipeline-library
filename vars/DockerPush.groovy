#!/usr/bin/groovy
def call(image) {
    withDockerRegistry([credentialsId: 'indigobot', url: '']) {
        sh "docker push $image"
    }
}
