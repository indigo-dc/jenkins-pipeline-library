import org.codehaus.groovy.control.CompilationFailedException

import eu.indigo.compose.parser.ConfigParser
import eu.indigo.compose.ProjectConfiguration
import eu.indigo.compose.ComposeFactory
import eu.indigo.compose.ComposeFactoryBuilder
import eu.indigo.compose.DockerCompose
import eu.indigo.Tox
import eu.indigo.scm.Git
import eu.indigo.scm.GitLocalBranch

def call(
    String configFile='./.sqa/config.yml',
    String baseRepository=null,
    String baseBranch=null,
    String credentialsId=null,
    String validatorDockerImage='eoscsynergy/jpl-validator:1.1.0',
    String... args) {

    def scmCheckout = { config ->
        if (baseRepository) {
            checkoutRepository(baseRepository, baseBranch, credentialsId)
        }
        else {
            checkoutRepository()
        }
    }
    scmCheckout.resolveStrategy = Closure.DELEGATE_FIRST

    if (args.length > 0 && args.localBranch) {
        scmCheckout.delegate = new GitLocalBranch(this)
    }
    else {
        scmCheckout.delegate = new Git(this)
    }
    scmCheckout(scm)

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
