#!/usr/bin/groovy

import static eu.eoscsynergy.DockerCompose.composeUp

/**
 * Run the pipeline within Docker Compose
 * This call defines a step that returns a stage
 *
 * Parameters are already defined in function definition
 */
def call(String service_ids, String compose_file='', String registry_url='https://hub.docker.com/') {
    composeUp(service_ids, compose_file, registry_url)
}
