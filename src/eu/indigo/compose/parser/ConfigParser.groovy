package eu.indigo.compose.parser

import eu.indigo.compose.ProjectConfiguration
import eu.indigo.compose.ProjectConfigurationBuilder

import eu.indigo.JenkinsDefinitions

/**
 * Configuration Parser
 */
@groovy.transform.InheritConstructors
class ConfigParser extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    // Constant literals for this Class
    String _repos = 'repos'

    private final String LATEST = 'latest'
    private final Integer DEFAULT_TIMEOUT = 600   // 600 seconds
    private final String DEFAULT_AGENT = 'docker-compose'
    Map configToClass = [
        'docker-compose': 'DockerCompose'
    ]
    List supportedBuildTools = [
        'tox'
    ]

    ProjectConfiguration parse(yaml, env) {
        if (DEBUG) { steps.echo "** parse(): ${setting}**" }

        new ProjectConfigurationBuilder()
            .setNodeAgentAux(getNodeAgent(yaml))
            .setConfig(getConfigSetting(yaml.config))
            .setStagesList(formatStages(getSQASetting(yaml['sqa-criteria'])))
            .setBuildNumber(env.BUILD_ID)
            .setEnvironment(parseEnvironment(yaml.environment))
            .setProjectName(parseProjectName(yaml.config))
            .setTimeout(yaml.timeout ?: DEFAULT_TIMEOUT)
            .build()
    }

    Map merge(Map[] sources) {
        switch (sources.length) {
            case 0:
                return [:]
            case 1:
                return sources[0]
        }
        Map result = [:]

        (sources[0].entrySet() + sources[1].entrySet()).each { entry ->
            if (DEBUG) { steps.echo "result = $result\nkey = ${entry.key}\nvalue = ${entry.value}" }
            result[entry.key] = result.containsKey(entry.key) && result[entry.key].getClass() == Map ?
                [:] << result[entry.key] << entry.value
                : entry.value
        }

        return result
    }

    Map getDefaultValue(String setting) {
        if (DEBUG) { steps.echo "** getDefaultValue(): ${setting}**" }
        def result = [:]
        switch (setting) {
            case 'config':
                result = [
                    node_agent: DEFAULT_AGENT,
                    deploy_template: '.sqa/docker-compose.yml'
                ]
                break
            case 'config_repo':
                result = [
                    branch: 'master',
                    dockertag: 'latest'
                ]
                break
            case 'tox':
                result = [
                    container: 'tox',
                    tox: [
                        tox_file: 'tox.ini',
                        testenv: []
                    ]
                ]
                break
        }
        return result
    }

    def getNodeAgent(yaml) {
        if (DEBUG) { steps.echo "** getNodeAgent() **" }
        configToClass[(yaml.config.node_agent == null) ? DEFAULT_AGENT : yaml.config.node_agent]
    }

    Map getConfigSetting(Map config) {
        if (DEBUG) { steps.echo "** getConfigSetting() **" }
        def configBase = merge(getDefaultValue('config'), config)
        def configRepos = [
            project_repos: configBase['project_repos']
                .collectEntries { id, repo ->
                    [id, merge(getDefaultValue('config_repo'), repo)]
                }
        ]
        if (DEBUG) { steps.echo 'configRepos:\n' + configRepos.toString() }
        if (DEBUG) { steps.echo 'configBase:\n' + configBase.toString() }
        return merge(configBase, configRepos)
    }

    Map getSQASetting(Map criteria) {
        if (DEBUG) { steps.echo "** getSQASetting() **" }
        if (DEBUG) { steps.echo "criteria:\n$criteria" }
        def sqaCriteria = criteria.each { criterion, data ->
            supportedBuildTools.each { tool ->
                def repoData = data[_repos].collectEntries { id, params ->
                    params.containsKey(tool) ? [id, merge(getDefaultValue(tool), params)] : null
                }
                data[_repos] = repoData
            }
        }
        if (DEBUG) { steps.echo "sqaCriteria:\n$sqaCriteria" }
        return sqaCriteria
    }

    List formatStages(Map criteria) {
        def stagesList = []
        criteria.each { criterion, data ->
            def stageMap = [:]
            data[_repos].each { repo, params ->
                stageMap['stage'] = "${criterion} ${repo}"
                stageMap['repo'] = repo
                params.each { k, v ->
                    stageMap[k] = v
                }
            }
            stagesList.add(stageMap)
        }
        return stagesList
    }

    def parseEnvironment(def environment) {
        if (!environment) {
            return ''
        }

        return environment.collect { k, v -> "${k}=${v}"}
    }

    def parseProjectName(def config) {
        if (!config || !config["project_name"]) {
            return "composeci-project"
        }

        return config["project_name"]
    }

}
