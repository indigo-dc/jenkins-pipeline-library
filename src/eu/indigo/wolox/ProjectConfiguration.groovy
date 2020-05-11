package eu.indigo.wolox

import eu.indigo.wolox.docker.DockerConfiguration
import eu.indigo.wolox.steps.Steps

class ProjectConfiguration implements Serializable {
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
