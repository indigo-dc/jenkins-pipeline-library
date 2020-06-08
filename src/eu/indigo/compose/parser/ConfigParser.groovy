package eu.indigo.compose.parser

import eu.indigo.compose.ProjectConfiguration

/**
 * Configuration Parser
 */
class ConfigParser implements Serializable {

    private static final long serialVersionUID = 0L

    // Constant literals for this Class
    String _repos = 'repos'

    private static String LATEST = 'latest'
    private static Integer DEFAULT_TIMEOUT = 600   // 600 seconds
    static Map configToClass = [
        'docker-compose': 'DockerCompose'
    ]
    static List supportedBuildTools = [
        'tox'
    ]

    static ProjectConfiguration parse(yaml, env) {

        ProjectConfiguration projectConfiguration = new ProjectConfiguration()
        new Builder(projectConfiguration, 'ProjectConfigurationBuilder')
            .setNodeAgentAux(configToClass[yaml.config.node_agent])
            .setConfig(getConfigSetting(yaml.config))
            .setStagesList(formatStages(getSQASetting(yaml['sqa-criteria'])))
            .setBuildNumber(env.BUILD_ID)
            .setEnvironment(parseEnvironment(yaml.environment))
            .setProjectName(parseProjectName(yaml.config))
            .setTimeout(yaml.timeout ?: DEFAULT_TIMEOUT)
            .build()
    }

    static Map merge(Map[] sources) {
        if (sources.length == 0) return [:]
        if (sources.length == 1) return sources[0]

        sources.inject([:]) { result, source ->
            source.each { k, v ->
                result[k] = result[k] instanceof Map ? (result[k] << v) : v
            }
            result
        }
    }

    static Map getDefaultValue(String setting) {
        def result = [:]
        switch(setting) {
            case 'config':
                result = [
                    node_agent: 'docker-compose',
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
                        testenv: ''
                    ]
                ]
                break
        }
        return result
    }

    static Map getConfigSetting(Map config) {
        def configBase = merge(getDefaultValue('config'), config)
        def configRepos = [
            project_repos: configBase['project_repos']
                .collectEntries { id, repo ->
                    [id, merge(getDefaultValue('config_repo'), repo)]
                }
        ]
        return merge(configRepos, configBase)
    }

    static Map getSQASetting(Map criteria) {
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

    static List formatStages(Map criteria) {
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

    static def parseEnvironment(def environment) {
        if (!environment) {
            return ''
        }

        return environment.collect { k, v -> "${k}=${v}"}
    }

    static def parseProjectName(def config) {
        if (!config || !config["project_name"]) {
            return "composeci-project"
        }

        return config["project_name"]
    }

}
