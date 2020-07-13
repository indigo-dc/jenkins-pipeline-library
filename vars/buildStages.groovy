import eu.indigo.compose.ProjectConfiguration
import eu.indigo.compose.ComposeFactory

def timeoutClosure(ProjectConfiguration projectConfig, Closure block) {
    if (projectConfig.timeout) {
        timeout(time: projectConfig.timeout, activity: true, unit: 'SECONDS') {
            block()
        }
    } else {
        block()
    }
}

def environmentClosure(ProjectConfiguration projectConfig, Closure block) {
    if (projectConfig.environment) {
        withEnv(projectConfig.environment) {
            block()
        }
    } else {
        block()
    }
}


def call(ProjectConfiguration projectConfig) {
    timeoutClosure(projectConfig) {
        environmentClosure(projectConfig) {
            projectConfig.nodeAgent.processStages(projectConfig)
        }
    }
}
