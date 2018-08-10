#!/usr/bin/groovy
def call() {
    sh "docker rmi \$(docker images -f 'dangling=true' -q)"
}
