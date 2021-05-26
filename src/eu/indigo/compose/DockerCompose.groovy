package eu.indigo.compose

import eu.indigo.JenkinsDefinitions
import eu.indigo.compose.ProjectConfiguration
import eu.indigo.Tox

/**
 * Definitions for Docker Compose integration in Jenkins
 * @see: https://docs.docker.com/compose/compose-file/
 */
@groovy.transform.InheritConstructors
class DockerCompose extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    private List<String> credentialVariablesNames = []

    /**
    * Parameters static strings for command parser
    */
    String _f = '-f'
    String _w = '--project-directory'
    String _ipf = '--ignore-push-failures'
    String _b = '--build'


    /**
    * Methods
    */

    /**
    * Apply withCredentials step
    *
    * @param credentials Credentials to be loaded to the environment as secrets
    * @param block The expected logical block to be executed
    */
    def withCredentialsClosure(List credentials, Closure block) {
        if (_DEBUG_) { steps.echo "** withCredentialsClosure() **" }
        if (credentials) {
            if (_DEBUG_) { steps.echo "credentials:\n${credentials}" }
            List credentialsStatements = credentialsToStep(credentials)
            if (_DEBUG_) { steps.echo 'credentialsToStep: ' + credentialsStatements }
            steps.withCredentials(credentialsStatements) {
                block()
            }
        } else {
            block()
        }
    }

    /**
    * Return the expected list of arguments for withCredentials step
    *
    * @param credentials A map with the expected argument names and values
    */
    List credentialsToStep(List credentials) {
        credentialVariablesNames = []
        if (_DEBUG_) { steps.echo "** credentialsToStep() **" }
        if (_DEBUG_) { steps.echo "credentialVariablesNames(start):\n${credentialVariablesNames}" }

        credentials.collect { credential ->
            def credType = credential.type
            if (_DEBUG_) { steps.echo "credential: $credential\ncredType: $credType" }
            def credValue
            switch (credType) {
                case 'string':
                    credentialVariablesNames << credential.variable
                    credValue = steps.string(credentialsId: credential.id, variable: credential.variable)
                    break
                case 'file':
                    credentialVariablesNames << credential.variable
                    credValue = steps.file(credentialsId: credential.id, variable: credential.variable)
                    break
                case 'zip':
                    credentialVariablesNames << credential.variable
                    credValue = steps.zip(credentialsId: credential.id, variable: credential.variable)
                    break
                case 'certificate':
                    credentialVariablesNames << credential.keystore_var
                    credentialVariablesNames << credential.alias_var
                    credentialVariablesNames << credential.password_var
                    credValue = steps.certificate(credentialsId: credential.id,
                                keystoreVariable: credential.keystore_var,
                                aliasVariable: credential.alias_var,
                                passwordVariable: credential.password_var)
                    break
                case 'username_password':
                    credentialVariablesNames << credential.username_var
                    credentialVariablesNames << credential.password_var
                    credValue = steps.usernamePassword(credentialsId: credential.id,
                                     usernameVariable: credential.username_var,
                                     passwordVariable: credential.password_var)
                    break
                case 'ssh_user_private_key':
                    credentialVariablesNames << credential.keyfile_var
                    credentialVariablesNames << credential.passphrase_var
                    credentialVariablesNames << credential.username_var
                    credValue = steps.sshUserPrivateKey(credentialsId: credential.id,
                                      keyFileVariable: credential.keyfile_var,
                                      passphraseVariable: credential.passphrase_var,
                                      usernameVariable: credential.username_var)
                    break
            }

            if (_DEBUG_) { steps.echo "credentialVariablesNames(end):\n${credentialVariablesNames}" }
            if (_DEBUG_) { steps.echo "credValue: $credValue" }
            credValue
        }
    }

    /**
    * Escape whitespaces in a path string
    *
    * @param path The variable with the path string to escape
    */
    def escapeWhitespace(String path) {
        return path.replaceAll(' ', '\\\\ ')
    }

    /**
    * Test if argument is not an empty string
    *
    * @param text The variable with the string to test
    */
    Boolean testString(String text) {
        text && !text.allWhitespace
    }

    /**
    * Receives two strings with the flag and the value and returns the concatenated by a space string
    *
    * @param first String with the flag
    * @param second String with the value to test
    */
    String parseParam(String first, String second) {
        if (testString(second)) {
            first + ' ' + second
        }
        else {
            ''
        }
    }

    /**
    * Returns the environment variables to docker-compose exec
    *
    */
    String getCredsVars() {
        String res = ''

        if (_DEBUG_) { steps.echo "** getCredsVars() **" }
        if (_DEBUG_) { steps.echo "credentialVariablesNames:\n${credentialVariablesNames}" }

        if (! credentialVariablesNames?.isEmpty()) {
            credentialVariablesNames.each { v ->
                res += "-e ${v} "
            }
        }

        res
    }

    /**
    * Run docker compose exec
    *
    * @param service Service name
    * @param command Command with arguments to run inside container
    * @param args.composeFile Docker compose file to override the default docker-compose.yml
    * @param args.workdir Path to workdir directory for this command
    * @see https://docs.docker.com/compose/reference/exec/
    */
    def composeExec(Map args, String service, String command) {
        String cmd = parseParam(_f, escapeWhitespace(args.composeFile)) + ' ' + parseParam(_w, escapeWhitespace(args.workdir)) +
                     ' exec -T ' + getCredsVars() + " $service $command"
        steps.sh "docker-compose $cmd"
    }

    /**
    * Run docker compose up
    *
    * @param serviceIds String with list of Service names separated by spaces to start [default]
    * @param args.forceBuild If defined will force the build of all images in docker-compose.yml
    * @param args.composeFile Docker compose file to override the default docker-compose.yml [default]
    * @param args.workdir Path to workdir directory for this command
    * @see https://docs.docker.com/compose/reference/up/
    * @see https://docs.docker.com/compose/reference/overview/
    */
    def composeUp(Map args, String serviceIds='') {
        String buildFlag = testString(args.forceBuild) ? _b : ''
        String cmd = parseParam(_f, escapeWhitespace(args.composeFile)) + ' ' + parseParam(_w, escapeWhitespace(args.workdir)) + " up $buildFlag -d $serviceIds"

        steps.sh "docker-compose $cmd"
    }

    /**
    * Run docker compose push
    *
    * @param serviceIds String with list of Service names separated by spaces to start [default]
    * @param ignoreFailures Will ignore push failures if a string is defined.
    * @param args.registryServer Define the server name and port to connect (example: localhost:8080)
    * @param args.username Docker registry username
    * @param args.password Docker registry password
    * @param args.composeFile Docker compose file to override the default docker-compose.yml [default]
    * @param args.workdir Path to workdir directory for this command
    * @see https://docs.docker.com/compose/reference/up/
    */
    def composePush(Map args, String serviceIds, String ignoreFailures='') {
        String registryServer = testString(args.registryServer) ? args.registryServer : ''
        String serviceCmd = serviceIds.equalsIgnoreCase('all') ? '' : serviceIds
        String failuresFlag = testString(ignoreFailures) ? _ipf : ''
        String cmd = parseParam(_f, escapeWhitespace(args.composeFile)) + ' ' + parseParam(_w, escapeWhitespace(args.workdir)) + " push $failuresFlag $serviceCmd"

        steps.sh "docker login -u \"${args?.username}\" -p \"${args?.password}\" ${registryServer}"
        steps.sh "docker-compose $cmd"
        steps.sh "docker logout ${registryServer}"
    }

    /**
    * Run docker compose down
    *
    * @param purge Boolean value. If true docker compose will erase all images and containers.
    * @param args.composeFile Docker compose file to override the default docker-compose.yml [default]
    * @param args.workdir Path to workdir directory for this command
    * @see https://docs.docker.com/compose/reference/down/
    * @see https://vsupalov.com/cleaning-up-after-docker/
    */
    def composeDown(Map args, Boolean purge=false) {
        String cmd = parseParam(_f, escapeWhitespace(args.composeFile)) + ' ' + parseParam(_w, escapeWhitespace(args.workdir))

        if (purge) {
            steps.sh "docker-compose $cmd down -v --rmi all --remove-orphans"
        }
        else {
            steps.sh "docker-compose $cmd down -v"
        }
    }

    /**
    * Copy file or directory into docker container
    *
    * @param service Service name
    * @param srcPath copies the contents of source path
    * @param destPath copies the contents to the destination path
    * @param args.composeFile Docker compose file to override the default docker-compose.yml
    * @param args.workdir Path to workdir directory for this command
    * @see https://docs.docker.com/engine/reference/commandline/cp/
    * @see https://blog.dcycle.com/blog/ae67284c/docker-compose-cp
    * @see https://docs.docker.com/compose/reference/ps/
    */
    def composeCP(Map args, String service, String srcPath, String destPath) {
        steps.sh "docker cp " + escapeWhitespace(srcPath) + " \"\$(docker-compose " +
            parseParam(_f, escapeWhitespace(args.composeFile)) + ' ' + parseParam(_w, escapeWhitespace(args.workdir)) +
            " ps -q $service)\":" + escapeWhitespace(destPath)
    }

    /**
    * Run docker compose exec
    *
    * @param service Service name
    * @param testenv Test environment to run with tox
    * @param args.composeFile configuration file to override the default docker-compose.yml
    * @param tox Tox object with implementations for python virtenv orquestration
    * @param args.toxFile Tox configuration file to override the default tox.ini
    * @param args.workdir Path to workdir directory for this command
    * @see https://docs.docker.com/compose/reference/exec/
    */
    def composeToxRun(Map args, String service, String testenv, Tox tox) {
        if (_DEBUG_) { steps.echo "** composeToxRun() **" }

        String credsVars = getCredsVars()
        if (_DEBUG_) { steps.echo "service: ${service}\ntestenv: ${testenv}\ntoxFile: " + escapeWhitespace(args.toxFile) + "\ncredsVars: $credsVars" }
        if (_DEBUG_) { steps.echo "tox command: " + tox.runEnv(testenv, toxFile: escapeWhitespace(args.toxFile)) }
        String cmd = parseParam(_f, escapeWhitespace(args.composeFile)) + ' ' + parseParam(_w, escapeWhitespace(args.workdir)) + ' exec -T ' +
                     " $credsVars $service " + tox.runEnv(testenv, toxFile: escapeWhitespace(args.toxFile))
        steps.sh "docker-compose $cmd"
    }

    /**
     * Run tools and command within the containers
     *
     * @param stageMap Map with all required stages to be run
     * @param projectConfiguration Complete project configuration source
     * @param workspace Path to workdir directory for the command
     */
    def runExecSteps(Map stageMap, ProjectConfiguration projectConfig, String workspace) {
        steps.stage(stageMap.stage) {
            if (stageMap.tox) {
                stageMap.tox.testenv.each { testenv ->
                    composeToxRun(stageMap.container, testenv, projectConfig.nodeAgent.tox,
                                  composeFile: projectConfig.config.deploy_template,
                                  toxFile: stageMap.tox.tox_file, workdir: workspace)
                }
            }
            if (stageMap.commands) {
                stageMap.commands.each { command ->
                    composeExec(stageMap.container, command,
                                composeFile: projectConfig.config.deploy_template, workdir: workspace)
                }
            }
        }
    }

    /**
     * Process stages from config.yml
     */
    def processStages(projectConfig) {
        String workspace = steps.env.WORKSPACE + '/'
        if (_DEBUG_) { steps.echo "** processStages() **" }
        if (_DEBUG_) { steps.echo "workspace path: $workspace" }
        if (_DEBUG_) { steps.sh 'echo "before loading credentials:\n$(env)"' }

        // Load debug settings defined in JenkinsDefinitions before starting the scripted pipeline
        debugSettings()

        // Environment setup
        steps.stage("Environment Setup") {
            // Checkout repositories to workspace with defined repository name
            projectConfig.config.project_repos?.each { repo_name, repo_confs ->
                steps.checkout scm: [$class: 'GitSCM', userRemoteConfigs: [[url: repo_confs.repo, credentialsId: repo_confs.credentials_id]],
                               branches: [[name: repo_confs.branch]],
                               extensions: [[$class: 'CleanCheckout', deleteUntrackedNestedRepositories: true],
                                            [$class: 'GitLFSPull'],
                                            [$class: 'RelativeTargetDirectory', relativeTargetDir: repo_name],
                                            [$class: 'ScmName', name: repo_name]] ],
                               changelog: false, poll: false
            }
        }

        try {
            // Run SQA stages
            List credentials = projectConfig.config.credentials
            withCredentialsClosure(credentials) {
                // Deploy the environment services using docker-compose
                composeUp(composeFile: projectConfig.config.deploy_template, workdir: workspace, forceBuild: steps.env.JPL_DOCKERFORCEBUILD)
                
                if (_DEBUG_) { steps.sh 'echo "after loading credentials:\n$(env)"' }

                projectConfig.stagesList.each { stageMap ->
                    // Run the defined steps
                    runExecSteps(stageMap, projectConfig, workspace)
                }

                // Push docker images to registry
                if (steps.env.JPL_DOCKERPUSH) {
                    steps.stage('Push Images to Docker Registry') {
                        composePush(composeFile: projectConfig.config.deploy_template, workdir: workspace, registryServer: steps.env.JPL_DOCKERSERVER, username: steps.env.JPL_DOCKERUSER, password: steps.env.JPL_DOCKERPASS, steps.env.JPL_DOCKERPUSH, steps.env.JPL_IGNOREFAILURES)
                    }
                }
            }
        } finally {
            // Review execution before exit if debug mode enabled
            if (_WORKSPACEDEBUG_) {
                try {
                    steps.timeout(time: _WORKSPACEDEBUGTIMEOUT_, unit: _WORKSPACEDEBUGUNIT_) {
                        steps.input message: "Click finish after reviewing the current job (will automatically finish in ${_WORKSPACEDEBUGTIMEOUT_} ${_WORKSPACEDEBUGUNIT_}).", ok: 'finish'
                    }
                } catch(org.jenkinsci.plugins.workflow.steps.FlowInterruptedException ex) {
                    steps.echo "Cleaning workspace after timeout expired..."
                }
            }

            // Clean docker-compose deployed environment
            steps.stage('Docker Compose cleanup') {
                composeDown(composeFile: projectConfig.config.deploy_template, workdir: workspace)
            }
        }
    }

}
