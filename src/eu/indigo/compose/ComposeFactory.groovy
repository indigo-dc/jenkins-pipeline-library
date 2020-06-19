package eu.indigo.compose

/**
 * Definitions for Docker Compose integration in Jenkins
 * @see: https://docs.docker.com/compose/compose-file/
 */
class ComposeFactory implements Serializable {

    private static final long serialVersionUID = 0L

    def static factory
    def static tox
    def static processStages(projectConfig) { return factory.processStages(projectConfig) }

}
