package eu.indigo.scm

import eu.indigo.JenkinsDefinitions

/**
 * Jenkins workflow SCM step
 * @see: https://www.jenkins.io/doc/pipeline/steps/workflow-scm-step/
 */
@groovy.transform.InheritConstructors
class Git extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    protected GitProperties properties
    protected Git gitObject

    /**
    * Define constructor to import definitions from Jenkins context
    * @see https://www.jenkins.io/doc/book/pipeline/shared-libraries/#accessing-steps
    */
    Git(steps, properties) {
        super(steps)
        this.properties = properties
    }

    Git(steps, properties, gitObject) {
        this.Git(steps, properties)
        this.gitObject = gitObject
    }

    protected def checkoutScm() {
        gitObject ? gitObject.checkoutRepository() : checkoutRepository()
    }

    protected def checkoutScm(
            String repository,
            String credentialsId,
            String name,
            String refspec,
            String branch,
            String targetDirectory) {
        if (gitObject) {
            gitObject.checkoutRepository(repository, credentialsId, name, refspec, branch, targetDirectory)
        }
        else {
            checkoutRepository(repository, credentialsId, name, refspec, branch, targetDirectory)
        }
    }

    @NonCPS
    protected def transformGitSCM(config) {
        [ $class: 'GitSCM' ] + config
    }

    @NonCPS
    protected def userRemoteConfigs(url, name, refspec, credentialsId) {
        properties.remoteConfigs += [[url: url, name: name, refspec: refspec, credentialsId: credentialsId]]
    }

    @NonCPS
    protected def branches(names) {
        properties.branches = names.collect { name ->
            [name: name]
        }
    }

    @NonCPS
    protected def relativeTargetDirectory(relativeTargetDir) {
        [$class: 'RelativeTargetDirectory', relativeTargetDir: relativeTargetDir]
    }

    @NonCPS
    protected def localBranch(localBranch) {
        [$class: 'LocalBranch', localBranch: localBranch]
    }

    @NonCPS
    protected def extensionsLoader(extension) {
        properties.extensions += extension
    }

    def setDefaults(settings) {
        Map defaultSettings = [
            baseRepository: null,
            credentialsId: null,
            baseBranch: 'master',
            relativeTargetDir: '.',
            remoteName: '',
            refspec: '',
            ]
        settings ? defaultSettings + settings :
                   defaultSettings
    }

    def checkoutRepository() {
        if (_DEBUG_) { steps.echo "** Git.checkoutRepository() **" }
        if (properties.remoteConfigs == []) { properties.remoteConfigs = steps.scm.userRemoteConfigs }
        steps.checkout transformGitSCM([
                branches: properties.branches,
                extensions: properties.extensions,
                userRemoteConfigs: properties.remoteConfigs,
            ])
    }

    def checkoutRepository(Map settings) {
        settings = setDefaults(settings)
        if (_DEBUG_) { steps.echo "** Git.checkoutRepository(${settings.baseRepository}, ${settings.credentialsId}, ${settings.baseBranch}, ${settings.remoteName}, ${settings.refspec}, ${settings.relativeTargetDir}) **" }
        userRemoteConfigs(settings.baseRepository, settings.remoteName, settings.refspec, settings.credentialsId)
        branches(["*/${settings.baseBranch}"])
        extensionsLoader(relativeTargetDirectory(settings.relativeTargetDir))
        checkoutRepository()
    }

}
