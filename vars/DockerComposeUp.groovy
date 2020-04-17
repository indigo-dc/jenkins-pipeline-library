#!/usr/bin/groovy

import static eu.eosc-synergy.DockerCompose

/**
 * Run the pipeline within Docker Compose
 * This call defines a step that returns a stage
 *
 * Parameters are already defined in above functions
 */
def call(String service_ids, String compose_file='', String registry_url='https://hub.docker.com/') {
    composeUp(service_ids, compose_file, registry_url)
}
