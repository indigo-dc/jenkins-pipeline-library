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
            _DEBUG_ ? steps.echo "result = $result\nkey = ${entry.key}\nvalue = ${entry.value}"
            result[entry.key] = result.containsKey(entry.key) && result[entry.key].getClass() == Map ?
                [:] << result[entry.key] << entry.value
                : entry.value
        }

        return result
    }

    Map getDefaultValue(String setting) {
        _DEBUG_ ? steps.echo"** getDefaultValue(): ${setting}**"
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
        _DEBUG_ ? steps.echo "** getNodeAgent() **"
        configToClass[(yaml.config.node_agent == null) ? DEFAULT_AGENT : yaml.config.node_agent]
    }

    Map getConfigSetting(Map config) {
        _DEBUG_ ? steps.echo "** getConfigSetting() **"
        def configBase = merge(getDefaultValue('config'), config)
        def configRepos = [
            project_repos: configBase['project_repos']
                .collectEntries { id, repo ->
                    [id, merge(getDefaultValue('config_repo'), repo)]
                }
        ]
        _DEBUG_ ? steps.echo 'configRepos:\n' + configRepos.toString()
        _DEBUG_ ? steps.echo 'configBase:\n' + configBase.toString()
        return merge(configBase, configRepos)
    }

    Map getSQASetting(Map criteria) {
        _DEBUG_ ? steps.echo "** getSQASetting() **"
        def sqaCriteria = criteria.each { criterion, data ->
            supportedBuildTools.each { tool ->
                def repoData = data[_repos].collectEntries { id, params ->
                    params.containsKey(tool) ? [id, merge(getDefaultValue(tool), params)] : null
                }
                data[_repos] = repoData
            }
        }
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
