#!/usr/bin/groovy

def call(String location) {
    build job: "${location}", 
    propagate: true, 
    wait: true
}
