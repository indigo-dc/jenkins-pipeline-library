#!/usr/bin/groovy
def call(testenv) {
    sh(script: 'tox -e'+testenv)
}
