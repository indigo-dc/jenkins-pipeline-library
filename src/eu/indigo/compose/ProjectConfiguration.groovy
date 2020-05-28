package eu.indigo.compose

/**
 * Project configuration
 */
class ProjectConfiguration implements Serializable {

    private static final long serialVersionUID = 0L

    def nodeAgent
    def config = [
        node_agent: 'docker-compose',
        deploy_template: '.sqa/docker-compose.yml',
        project_repos: [
            [
                deepaas: [
                    repo: 'https://github.com/indigo-dc/DEEPaaS'
                ]
            ]
        ]
    ]
    def stagesList = [
        [
            stage: 'qc-style deepaas',
            repo: 'deepaas',
            container: 'deepaas',
            tox: [
                testenv: ['pep8']
            ]
        ],
        [
            stage: 'qc-coverage deepaas',
            repo: 'deepaas',
            container: 'deepaas',
            tox: [
                testenv: ['cover', 'cobertura']
            ]
        ],
        [
            stage: 'qc-security deepaas',
            repo: 'deepaas',
            container: 'deepaas',
            tox: [
                testenv: ['bandit']
            ]
        ]
    ]
    def environment
    def projectName
    def buildNumber
    def timeout

}
