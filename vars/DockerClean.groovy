#!/usr/bin/groovy
def call() {
    sh "docker rmi --force \$(docker images -f 'dangling=true' -q)"
}
