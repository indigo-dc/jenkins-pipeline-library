#!/usr/bin/groovy

def call() {
    dependencyCheckPublisher canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '**/dependency-check*.xml', unHealthy: ''
}
