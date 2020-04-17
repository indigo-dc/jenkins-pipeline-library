#!/usr/bin/groovy

import static eu.eosc-synergy.DockerCompose

/**
 * Run the pipeline within Docker Compose
 * This call defines a step that returns a stage
 *
 * Parameters are already defined in above functions
 */
def call(Boolean purge, String compose_file='') {
    composeDown(purge, compose_file)
}
