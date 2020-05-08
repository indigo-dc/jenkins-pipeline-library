#!/usr/bin/groovy

import static eu.indigo.DockerCompose.composeDown

/**
 * Run the pipeline within Docker Compose
 * This call defines a step that returns a stage
 *
 * Parameters are already defined in function definition
 */
def call(Boolean purge, String compose_file='') {
    composeDown(purge, compose_file)
}
