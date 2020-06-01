import org.codehaus.groovy.control.CompilationFailedException

import eu.indigo.compose.parser.ConfigParser
import eu.indigo.compose.ProjectConfiguration
import eu.indigo.compose.parser.ConfigValidation
import eu.indigo.compose.ComposeFactory
import eu.indigo.compose.DockerCompose
import eu.indigo.Tox

def call(String configFile='./.sqa/config.yml', String baseRepository=null) {
    checkoutRepository(baseRepository)
    def yaml = readYaml file: configFile
    def schema = libraryResource('eu/indigo/compose/parser/schema.json')
    def buildNumber = Integer.parseInt(env.BUILD_ID)
    ProjectConfiguration projectConfig = null

    try {
        validator = new ConfigValidation()
        invalidMessages = validator.validate(yaml, schema)
    } catch (GroovyRuntimeException e) {
        error 'ConfigValidation have a runtime exception with status:\n' + e \
        + '\nThe complete stack trace is the following:\n' \
        + e.getStackTrace()
    }
    if (invalidMessages) {
        error(invalidMessages.join('\n'))
    }
    projectConfig = ConfigParser.parse(yaml, env)
    try {
        projectConfig.nodeAgent = new ComposeFactory().tap {
            factory = this.getClass().classLoader.loadClass(projectConfig.nodeAgentAux, true, false)?.newInstance(this)
            tox = new Tox(this)
        }
    } catch (ClassNotFoundException | CompilationFailedException e) {
        error 'BuildStages: Node agent not defined'
    }
    return projectConfig
}

def checkoutRepository(String repository) {
    if (repository) {
        checkout([
            $class: 'GitSCM',
            userRemoteConfigs: [[url: repository]]])
    }
    else {
        checkout scm
    }
}
