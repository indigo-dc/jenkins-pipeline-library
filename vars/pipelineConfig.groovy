import org.codehaus.groovy.control.CompilationFailedException

import eu.indigo.compose.parser.ConfigParser
import eu.indigo.compose.ProjectConfiguration
import eu.indigo.compose.ComposeFactory
import eu.indigo.compose.ComposeFactoryBuilder
import eu.indigo.compose.DockerCompose
import eu.indigo.Tox

def call(
    String configFile='./.sqa/config.yml',
    String baseRepository=null,
    String baseBranch=null,
    String credentialsId=null,
    String validatorDockerImage='eoscsynergy/jpl-validator:1.1.0') {

    checkoutRepository(baseRepository, baseBranch, credentialsId)
    def yaml = readYaml file: configFile
    def buildNumber = Integer.parseInt(env.BUILD_ID)
    ProjectConfiguration projectConfig = null

    try {
        invalidMessages = validate(configFile, validatorDockerImage)
    } catch (GroovyRuntimeException e) {
        error "ConfigValidation have a runtime exception with status:\n$e"
    }
    if (invalidMessages) {
        error("Validation exit code): $invalidMessages")
    }

    projectConfig = new ConfigParser(this).parse(yaml, env)
    switch (projectConfig.nodeAgentAux) {
        case 'DockerCompose':
            projectConfig.nodeAgent = new ComposeFactoryBuilder()
                .setFactory(new DockerCompose(this))
                .setTox(new Tox(this))
                .build()
            break
        default:
            error "pipelineConfig: Node agent ${projectConfig.nodeAgentAux} not defined!"
            break
    }
    return projectConfig
}

def validate(String configFile, String validatorDockerImage) {
    def cmd = "docker pull $validatorDockerImage &&" +
              'docker run --rm -v "$PWD:/sqa" ' + "$validatorDockerImage /sqa/${configFile}"
    return sh(returnStatus: true, script: cmd)
}

def checkoutRepository(String repository, String branch='master', String credentialsId) {
    if (repository) {
        checkout([
            $class: 'GitSCM',
            branches: [[name: "*/${branch}"]],
            extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: '.']],
            userRemoteConfigs: [[url: repository, credentialsId: credentialsId]]])
    }
    else {
        // Use 'LocalBranch' class to avoid detached HEAD state
		checkout([
		     $class: 'GitSCM',
		     branches: scm.branches,
		     doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
		     extensions: scm.extensions + [$class: 'LocalBranch', localBranch: '**'],
		     userRemoteConfigs: scm.userRemoteConfigs
		])
    }
}
