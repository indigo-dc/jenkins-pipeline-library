#!/usr/bin/groovy

def call(String report='**/target/surefire-reports/*.xml') {
    junit report
}
