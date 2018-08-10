#!/usr/bin/groovy
def call(image_id) {
    withDockerRegistry([credentialsId: 'indigobot', url: '']) {
        sh "docker push $image_id"
    }
}
