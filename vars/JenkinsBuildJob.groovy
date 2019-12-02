#!/usr/bin/groovy

/**
 * Builds Jenkins jobs from external pipelines in the same Jenkins instance.
 *
 * @param location Jenkins' location of the job [mandatory]
 * @see https://plugins.jenkins.io/pipeline-build-step
 */
def call(String location, List parameters=[]) {
    build job: "${location}", 
    parameters: parameters, 
    propagate: true, 
    wait: true
}
