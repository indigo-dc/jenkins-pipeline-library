import org.codehaus.groovy.control.CompilationFailedException

import eu.indigo.compose.parser.ConfigParser
import eu.indigo.compose.ProjectConfiguration
import eu.indigo.compose.ComposeFactory
import eu.indigo.compose.ComposeFactoryBuilder
import eu.indigo.compose.DockerCompose
import eu.indigo.Tox
import eu.indigo.scm.Git
import eu.indigo.scm.GitProperties
import eu.indigo.scm.GitLocalBranch

def call(Map configs) {

    Map scmConfigsDefault = [
        localBranch: null,
        ]
    Map scmConfigs = configs?.scmConfigs ? scmConfigsDefault + configs?.scmConfigs :
                                           scmConfigsDefault

    Map configsDefault = [
        configFile: './.sqa/config.yml',
        baseRepository: null,
        baseBranch: null,
        credentialsId: null,
        validatorDockerImage: 'eoscsynergy/jpl-validator:1.1.0',
        scmConfigs: scmConfigs,
        ]
    configs = configs ? configsDefault + configs :
                        configsDefault

    def scmCheckout = { ->
        if (configs?.baseRepository) {
            checkoutRepository(baseRepository: configs?.baseRepository, credentialsId: configs?.credentialsId, baseBranch: configs?.baseBranch)
        }
        else {
            checkoutRepository()
        }
    }
    scmCheckout.resolveStrategy = Closure.DELEGATE_FIRST

    props = new GitProperties(this)
    gitObj = new Git(this, props)
    if (configs?.scmConfigs?.localBranch) {
        scmCheckout.delegate = new GitLocalBranch(this, props, gitObj, configs.scmConfigs.localBranch)
    }
    scmCheckout.delegate = gitObj
    scmCheckout()

    def yaml = readYaml file: configs.configFile
    def buildNumber = Integer.parseInt(env.BUILD_ID)
    ProjectConfiguration projectConfig = null

    try {
        invalidMessages = validate(configs?.configFile, configs?.validatorDockerImage)
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
