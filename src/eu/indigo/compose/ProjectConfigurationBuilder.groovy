package eu.indigo.compose

/**
 * Project configuration
 */
class ProjectConfigurationBuilder implements Serializable {

    private static final long serialVersionUID = 0L

    def nodeAgentAux
    def config
    def stagesList
    def environment
    def projectName
    def buildNumber
    def timeout

    def setNodeAgentAux(nodeAgentAux) {
        this.nodeAgentAux = nodeAgentAux
        return this
    }

    def setConfig(config) {
        this.config = config
        return this
    }

    def setStagesList(stagesList) {
        this.stagesList = stagesList
        return this
    }

    def setEnvironment(environment) {
        this.environment = environment
        return this
    }

    def setProjectName(projectName) {
        this.projectName = projectName
        return this
    }

    def setBuildNumber(buildNumber) {
        this.buildNumber = buildNumber
        return this
    }

    def setTimeout(timeout) {
        this.timeout = timeout
        return this
    }

    ProjectConfiguration build() {
        new ProjectConfiguration(nodeAgentAux: this.nodeAgentAux,
                                 config: this.config,
                                 stagesList: this.stagesList,
                                 environment: this.environment,
                                 projectName: this.projectName,
                                 buildNumber: this.buildNumber,
                                 timeout: this.timeout)
    }

}
