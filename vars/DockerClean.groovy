#!/usr/bin/groovy

/**
 * Dangling Docker image pruning.
 *
 */
def call() {
    sh "docker rmi --force \$(docker images -f 'dangling=true' -q)"
}
