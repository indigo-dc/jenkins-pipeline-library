import eu.indigo.compose.ProjectConfiguration
import eu.indigo.compose.ComposeFactory

def call(ProjectConfiguration projectConfig) {
    try {
        if (projectConfig.timeout) {
            timeout(time: projectConfig.timeout, activity: true, unit: 'SECONDS') {
                projectConfig.nodeAgent.processStages(projectConfig)
            }
        }
        else {
            projectConfig.nodeAgent.processStages(projectConfig)
        }
    } catch (Exception e) {
        error("Exception on buildStages(): ${e}")
    }
}
