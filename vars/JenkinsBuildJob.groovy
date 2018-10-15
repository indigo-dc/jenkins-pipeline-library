#!/usr/bin/groovy
def call(location) {
    build job: "${location}", 
    propagate: true, 
    wait: true
}
