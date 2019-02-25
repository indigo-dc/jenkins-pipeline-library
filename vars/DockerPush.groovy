#!/usr/bin/groovy

def call(String image) {
    withDockerRegistry([credentialsId: 'indigobot', url: '']) {
        sh "docker push $image"
    }
}
