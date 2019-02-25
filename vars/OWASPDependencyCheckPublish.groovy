#!/usr/bin/groovy

/**
 * Publishes OWASP report.
 *
 * @param packages List of required packages [mandatory]
 * @param report Target file [mandatory]
 * @see https://plugins.jenkins.io/dependency-check-jenkins-plugin
 */
def call() {
    dependencyCheckPublisher canComputeNew: false,
                             defaultEncoding: '',
                             healthy: '',
                             pattern: '**/dependency-check*.xml',
                             unHealthy: ''
}
