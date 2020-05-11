@Library(['github.com:WORSICA/jenkins-pipeline-library@docker-compose'])
import eu.indigo.wolox.ProjectConfiguration
import eu.indigo.wolox.steps.Step

def call(ProjectConfiguration projectConfig) {
    return { variables ->
        List<Step> stepsA = projectConfig.steps.steps
        stepsA.each { step ->
            stage(step.name) {
                step.commands.each { command ->
                    sh command
                }
            }
        }
    }
}
