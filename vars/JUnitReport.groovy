#!/usr/bin/groovy
def call(report='**/target/surefire-reports/*.xml') {
    junit report
}
