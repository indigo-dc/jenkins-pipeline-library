package eu.indigo.compose

/**
 * Project configuration
 */
class ProjectConfiguration implements Serializable {

    private static final long serialVersionUID = 0L

    def nodeAgent
    def nodeAgentAux
    def config
    def stagesList
    def environment
    def projectName
    def buildNumber
    def timeout

}
