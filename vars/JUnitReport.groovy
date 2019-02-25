#!/usr/bin/groovy

/**
 * Publishes JUnit report.
 *
 * @param report JUnit report [default]
 */
def call(String report='**/target/surefire-reports/*.xml') {
    junit report
}
