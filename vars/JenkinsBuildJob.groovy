#!/usr/bin/groovy

/**
 * Builds Jenkins jobs from external pipelines in the same Jenkins instance.
 *
 * @param  location the location of the job in Jenkins [mandatory]
 * @see https://plugins.jenkins.io/pipeline-build-step
 */
def call(String location) {
    build job: "${location}", 
    propagate: true, 
    wait: true
}

