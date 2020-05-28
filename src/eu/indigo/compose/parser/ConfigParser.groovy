package eu.indigo.compose.parser

import eu.indigo.compose.ProjectConfiguration

/**
 * Configuration Parser
 */
class ConfigParser implements Serializable {

    private static final long serialVersionUID = 0L

    private static String LATEST = 'latest'
    private static Integer DEFAULT_TIMEOUT = 600   // 600 seconds
    static List supportedBuildTools = [
        'tox'
    ]

    static ProjectConfiguration parse(def yaml, def env) {

        ProjectConfiguration projectConfiguration = new ProjectConfiguration()

        projectConfiguration.buildNumber = env.BUILD_ID

        projectConfiguration.environment = parseEnvironment(yaml.environment)

        projectConfiguration.projectName = parseProjectName(yaml.config)

        projectConfiguration.timeout = yaml.timeout ?: DEFAULT_TIMEOUT

        return projectConfiguration
    }

    static Map merge(Map[] sources) {
        if (sources.length == 0) return [:]
        if (sources.length == 1) return sources[0]

        sources.inject([:]) { result, source ->
            source.each { k, v ->
                result[k] = result[k] instanceof Map ? merge(result[k], v) : v
            }
            result
        }
    }
    
    static Map getDefaultValue(String setting) {
        def result = [:]
        switch(setting) {
            case 'config':
                result = [
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
                        toxfile: 'tox.ini',
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
                def repoData = data['repos'].collectEntries { id, params ->
                    params.containsKey(tool) ? [id, merge(getDefaultValue(tool), params)] : null
                }
                data['repos'] = repoData
            }
        }
        return sqaCriteria
    }
    
    static List formatStages(Map criteria) {
        def stagesList = []
        criteria.each { criterion, data ->
            def stageMap = [:]
            data['repos'].each { repo, params ->
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
            return ""
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
