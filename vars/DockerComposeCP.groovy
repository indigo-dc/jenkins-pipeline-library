@Library(['github.com:WORSICA/jenkins-pipeline-library@docker-compose'])
import static eu.indigo.DockerCompose.composeCP

/**
 * Run the pipeline within Docker Compose
 * This call defines a step that returns a stage
 *
 * Parameters are already defined in function definition
 */
call(String service, String command, String compose_file='', String workdir='') {
    composeCP(service, command, compose_file, workdir)
}
