package eu.indigo.wolox

import eu.indigo.wolox.docker.DockerConfiguration
import eu.indigo.wolox.steps.Steps

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
