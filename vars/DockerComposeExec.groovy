#!/usr/bin/groovy

import static eu.eosc-synergy.DockerCompose

/**
 * Run the pipeline within Docker Compose
 * This call defines a step that returns a stage
 *
 * Parameters are already defined in above functions
 */
def call(String service, String command, String compose_file='', String workdir='') {
    composeExec(service, command, compose_file, workdir)
}
