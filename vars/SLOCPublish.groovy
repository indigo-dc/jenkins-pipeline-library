#!/usr/bin/groovy

/**
 * Publishes SLOC report.
 *
 * @see https://plugins.jenkins.io/sloccount
 */
def call() {
    sloccountPublish encoding: '', pattern: '**/cloc.xml'
}
