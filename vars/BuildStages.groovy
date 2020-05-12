@Library(['github.com:WORSICA/jenkins-pipeline-library@docker-compose'])
import eu.indigo.wolox.ProjectConfiguration
import eu.indigo.wolox.steps.Step

def call(ProjectConfiguration projectConfig) {
    def stagesMap = projectConfig.stagesMap

    stagesList.each { stageLabel, commands ->
        stage(stageLabel) {
            commands.each { container, command ->
                switch (projectConfig.node_agent) {
                    case 'docker-compose':
                        DockerComposeExec(container, command)
                        break
                    default:
                        println('BuildStages: Node agent not defined')
                        break
                }
            }
        }
    }
}
