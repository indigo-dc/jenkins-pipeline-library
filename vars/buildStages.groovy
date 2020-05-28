import eu.indigo.compose.ProjectConfiguration
import eu.indigo.compose.ComposeFactory
import eu.indigo.compose.

def call(ProjectConfiguration projectConfig) {

    if (projectConfig.timeout) {
        timeout(time: projectConfig.timeout, activity: true, unit: 'SECONDS') {
            projectConfig.nodeAgent.processStages(projectConfig.stagesList)
        }
    }
    else {
        projectConfig.nodeAgent.processStages(projectConfig.stagesList)
    }

}
