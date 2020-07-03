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

    /**
    * Parameters static strings for command parser
    */
    String _f = '-f'
    String _w = '--project-directory'


    /**
    * Methods
    */

    /**
    * Test if argument is not an empty string
    *
    * @param text The variable with the string to test
    */
    def testString(String text) {
        return text && !text.allWhitespace
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
    * Receives two strings with the flag and the value and returns the concatenated by a space string
    *
    * @param first String with the flag
    * @param second String with the value to test
    */
    def parseParam(String first, String second) {
        if(testString(second)) {
            return first + ' ' + escapeWhitespace(second)
        }
        else {
            return ''
        }
    }

    /**
    * Parse environment variables into a string list for withEnv step
    *
    * @param env Map with the variable name as key and the expected value
    */
    List<String> envToStep(Map env) {
        env.collect { e, v ->
            "$e=\"$v\""
        }
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
        String cmd = parseParam(_f, args.composeFile) + ' ' + parseParam(_w, args.workdir) + \
                     ' exec -T ' + " $service \"$command\""
        steps.sh "docker-compose $cmd"
    }

    /**
    * Run docker compose up
    *
    * @param serviceIds String with list of Service names separated by spaces to start [default]
    * @param args.composeFile Docker compose file to override the default docker-compose.yml [default]
    * @param args.workdir Path to workdir directory for this command
    * @see https://docs.docker.com/compose/reference/up/
    * @see https://docs.docker.com/compose/reference/overview/
    */
    def composeUp(Map args, String serviceIds='') {
        String cmd = parseParam(_f, args.composeFile) + ' ' + parseParam(_w, args.workdir) + " up -d $serviceIds"

        steps.sh "docker-compose  $cmd"
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
        String cmd = parseParam(_f, args.composeFile) + ' ' + parseParam(_w, args.workdir)

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
        steps.sh "docker cp $srcPath \"\$(docker-compose " + \
            parseParam(_f, args.composeFile) + ' ' + parseParam(_w, args.workdir) + " ps -q $service)\":$destPath"
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
        String env = args.environment.each { e ->
            env += ' ' + parseParam(_e, e) + "=\${$e}"
        }
        String cmd = parseParam(_f, args.composeFile) + ' ' + parseParam(_w, args.workdir) + ' exec -T ' +
                     " $service " + tox.runEnv(testenv, toxFile: args.toxFile)
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
        if (_DEBUG_) { steps.echo "workspace path: $workspace" }

        // Environment setup
        steps.stage("Environment Setup") {
            // Checkout repositories to workspace with defined repository name
            projectConfig.config.project_repos.each { repo_name, repo_confs ->
                steps.checkout scm: [$class: 'GitSCM', userRemoteConfigs: [[url: repo_confs.repo]], \
                               branches: [[name: repo_confs.branch]], \
                               extensions: [[$class: 'CleanCheckout', deleteUntrackedNestedRepositories: true], \
                                            [$class: 'GitLFSPull'], \
                                            [$class: 'RelativeTargetDirectory', relativeTargetDir: repo_name], \
                                            [$class: 'ScmName', name: repo_name]] ], \
                               changelog: false, poll: false
            }

            // Deploy the environment services using docker-compose
            composeUp(composeFile: projectConfig.config.deploy_template, workdir: workspace)
        }

        try {
            // Run SQA stages
            projectConfig.stagesList.each { stageMap ->
                runExecSteps(stageMap, projectConfig, workspace)
            }
        } finally {
            // Clean docker-compose deployed environment
            steps.stage("Docker Compose cleanup") {
                composeDown(composeFile: projectConfig.config.deploy_template, workdir: workspace)
            }
        }
    }

}
