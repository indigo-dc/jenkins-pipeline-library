package eu.indigo.compose

/**
 * Project configuration
 */
@CompileDynamic
class ProjectConfiguration implements Serializable {

    private static final long serialVersionUID = 0L

    def node_agent
    def environment
    def services
    def projectName
    def buildNumber
    def env
    def timeout

}
