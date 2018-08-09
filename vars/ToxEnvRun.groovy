#!/usr/bin/groovy
def call(testenv) {
    sh 'tox -e ${testenv}'	
}
