package eu.indigo.compose

import eu.indigo.compose.docker.DockerConfiguration
import eu.indigo.compose.steps.Steps

/**
 * Project configuration
 */
@CompileDynamic
class ProjectConfiguration implements Serializable {

    private static final long serialVersionUID = 0L

    def node_agent
    def environment
    def services
    Steps steps
    def projectName
    def buildNumber
    DockerConfiguration dockerConfiguration
    def env
    def timeout

}
