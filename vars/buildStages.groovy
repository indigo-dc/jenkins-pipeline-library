@Library(['github.com:WORSICA/jenkins-pipeline-library@docker-compose'])
import eu.indigo.wolox.ProjectConfiguration
import eu.indigo.DockerCompose

call(ProjectConfiguration projectConfig) {

    switch (projectConfig.node_agent) {
        case 'docker-compose':
            run = new DockerCompose(steps)
            run.composeUp()
            run.processStages(projectConfig.stagesMap)
            run.composeDown()
            break
        default:
            println('BuildStages: Node agent not defined')
            break
    }

}
