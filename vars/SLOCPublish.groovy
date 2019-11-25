#!/usr/bin/groovy

/**
 * Publishes SLOC report.
 * Looks for a cloc generated report.
 * @see https://plugins.jenkins.io/sloccount
 */
def call() {
    sloccountPublish encoding: '', pattern: '**/cloc.xml'
}

