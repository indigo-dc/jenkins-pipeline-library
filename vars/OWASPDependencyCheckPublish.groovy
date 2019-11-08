#!/usr/bin/groovy

/**
 * Publishes OWASP report.
 *
 * @param  report Target file [mandatory]
 * @see https://plugins.jenkins.io/dependency-check-jenkins-plugin
 */
def call(String report='**/dependency-check*.xml') {
    dependencyCheckPublisher pattern: report
}

