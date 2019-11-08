#!/usr/bin/groovy

/**
 * Collects the CheckStyle report from the specified location.
 *
 * @param  report the report location [default]
 * @see https://plugins.jenkins.io/checkstyle
 */
def call(String report='**/target/checkstyle-result.xml') {
    checkstyle canComputeNew: false,
    defaultEncoding: '',
    healthy: '',
    pattern: report,
    unHealthy: ''
}

