package eu.indigo.compose

/**
 * Project configuration
 */
class ProjectConfiguration implements Serializable {

    private static final long serialVersionUID = 0L

    def nodeAgent
    def nodeAgentAux
    def config = [
        deploy_template: '.sqa/docker-compose.yml',
        project_repos: [
            deepaas: [
                repo: 'https://github.com/indigo-dc/DEEPaaS.git',
                branch: 'master'
            ]
        ]
    ]
    def stagesList = [
        [
            stage: 'qc-style deepaas',
            repo: 'deepaas',
            container: 'deepaas',
            tox: [
                testenv: ['pep8'],
                tox_file: './tox.ini'
            ]
        ],
        [
            stage: 'qc-coverage deepaas',
            repo: 'deepaas',
            container: 'deepaas',
            tox: [
                testenv: ['cover', 'cobertura'],
                tox_file: './tox.ini'
            ]
        ],
        [
            stage: 'qc-security deepaas',
            repo: 'deepaas',
            container: 'deepaas',
            tox: [
                testenv: ['bandit'],
                tox_file: './tox.ini'
            ]
        ]
    ]
    def environment
    def projectName
    def buildNumber
    def timeout

    static class Builder {

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

    Builder getBuilder(instance) {
        return new Builder(instance, 'ProjectConfigurationBuilder')
    }

}
