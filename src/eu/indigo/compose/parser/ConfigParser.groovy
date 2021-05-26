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
    private final String DEFAULT_AGENT = 'docker_compose'
    Map configToClass = [
        'docker_compose': 'DockerCompose'
    ]
    List supportedCredentialTypes = [
        'string', // 'file' and 'zip' are no different
        'certificate',
        'username_password',
        'ssh_user_private_key'
    ]
    List supportedBuildTools = [
        'tox'
    ]
    Map defaultValues = [
        config: [
            node_agent: DEFAULT_AGENT,
            deploy_template: '.sqa/docker-compose.yml'
        ],
        config_repo: [
            branch: 'master',
            dockertag: 'latest'
        ],
        config_credentials_string: [
            type: 'string',
            variable: 'JPL_SECRET'
        ],
        config_credentials_file: [
            type: 'file',
            variable: 'JPL_SECRET'
        ],
        config_credentials_zip: [
            type: 'zip',
            variable: 'JPL_SECRET'
        ],
        config_credentials_certificate: [
            type: 'certificate',
            keystore_var: 'JPL_KEYSTORE',
            alias_var: 'JPL_ALIAS',
            password_var: 'JPL_PASSWORD'
        ],
        config_credentials_username_password: [
            type: 'username_password',
            username_var: 'JPL_USERNAME',
            password_var: 'JPL_PASSWORD'
        ],
        config_credentials_ssh_user_private_key: [
            type: 'ssh_user_private_key',
            keyfile_var: 'JPL_KEYFILE',
            passphrase_var: 'JPL_PASSPHRASE',
            username_var: 'JPL_USERNAME'
        ],
        tox: [
            container: 'tox',
            tox: [
                tox_file: 'tox.ini',
                testenv: []
            ]
        ]
    ]

    ProjectConfiguration parse(yaml, env) {
        if (_DEBUG_) { steps.echo "** parse(): ${yaml}**" }

        new ProjectConfigurationBuilder()
            .setNodeAgentAux(getNodeAgent(yaml))
            .setConfig(getConfigSetting(yaml.config))
            .setStagesList(formatStages(getSQASetting(yaml['sqa_criteria'])))
            .setBuildNumber(env.BUILD_ID)
            .setEnvironment(parseEnvironment(yaml.environment))
            .setProjectName(parseProjectName(yaml.config))
            .setTimeout(yaml.timeout ?: DEFAULT_TIMEOUT)
            .build()
    }

    Map merge(Map[] sources) {
        if (_DEBUG_) { steps.echo "** merge(): ${sources}**" }
        switch (sources.length) {
            case 0:
                return [:]
            case 1:
                return sources[0]
        }
        Map result = [:]

        (sources[0].entrySet() + sources[1].entrySet()).each { entry ->
            if (_DEBUG_) { steps.echo "result = $result\nkey = ${entry.key}\nvalue = ${entry.value}" }
            result[entry.key] = result.containsKey(entry.key) && result[entry.key].getClass() == Map ?
                [:] << result[entry.key] << entry.value
                : entry.value
        }

        return result
    }

    Map getDefaultValue(String setting) {
        if (_DEBUG_) { steps.echo "** getDefaultValue(): ${setting}**" }

        defaultValues[setting].collectEntries { k, v ->
            v.getClass() == Map ?
                [ k, [:] << v ]
                : [ k, v ]
        }
    }

    def getNodeAgent(yaml) {
        if (_DEBUG_) { steps.echo "** getNodeAgent() **" }
        configToClass[(yaml.config?.node_agent == null) ? DEFAULT_AGENT : yaml.config.node_agent]
    }

    def getCredentialType(Map creds) {
        def cred_type = 'string'
        if (creds.containsKey('type')) {
            cred_type = creds.type
        }
        else {
            def cred_keys = creds.keySet()
            cred_keys.removeAll(['id'] as Object[])
            def cred_type_keys = []
            supportedCredentialTypes.each { type ->
                cred_type_keys = getDefaultValue("config_credentials_${type}").keySet()
                if (cred_type_keys.containsAll(cred_keys)) {
                    cred_type = type
                }
            }
        }
        return cred_type
    }

    Map getConfigSetting(Map config) {
        if (_DEBUG_) { steps.echo "** getConfigSetting() **" }
        def configBase = config ? merge(getDefaultValue('config'), config) : merge(getDefaultValue('config'))
        def configRepos = [
            project_repos: configBase['project_repos']
                ?.collectEntries { id, repo ->
                    [id, merge(getDefaultValue('config_repo'), repo)]
                }
        ]
        def configCredentials = [
            credentials: configBase['credentials'].collect { cred ->
                merge(getDefaultValue("config_credentials_" + getCredentialType(cred)), cred)
            }
        ]

        def configBaseRepos = merge(configBase, configRepos)
        def configMerged = merge(configBaseRepos, configCredentials)

        if (_DEBUG_) { steps.echo 'configBase:\n' + configBase.toString() }
        if (_DEBUG_) { steps.echo 'configRepos:\n' + configRepos.toString() }
        if (_DEBUG_) { steps.echo 'configCredentials:\n' + configCredentials.toString() }
        if (_DEBUG_) { steps.echo 'configBaseRepos:\n' + configBaseRepos.toString() }
        if (_DEBUG_) { steps.echo 'configMerged:\n' + configMerged.toString() }
        return configMerged
    }

    Map getSQASetting(Map criteria) {
        if (_DEBUG_) { steps.echo "** getSQASetting() **" }
        if (_DEBUG_) { steps.echo "criteria:\n$criteria" }
        def sqaCriteria = criteria.each { criterion, data ->
            supportedBuildTools.each { tool ->
                if (_DEBUG_) { steps.echo "tool: $tool" }
                if (_DEBUG_) { steps.echo "data:\n$data" }
                def repoData = data[_repos].collectEntries { id, params ->
                    if (_DEBUG_) { steps.echo "id: $id\nparams:\n$params" }
                    params.containsKey(tool) ? [id, merge(getDefaultValue(tool), params)] : [id, params]
                }
                data[_repos] = repoData
            }
        }
        if (_DEBUG_) { steps.echo "sqaCriteria:\n$sqaCriteria" }
        return sqaCriteria
    }

    List formatStages(Map criteria) {
        if (_DEBUG_) { steps.echo "** formatStages() **" }
        List stagesList = []
        criteria.each { criterion, data ->
            data[_repos].each { repo, params ->
                Map stageMap = [:]
                stageMap['stage'] = "${criterion} ${repo}"
                stageMap['repo'] = repo
                stageMap << params
                stagesList.add(stageMap)
            }
        }
        if (_DEBUG_) { steps.echo "stagesList:\n$stagesList" }
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
