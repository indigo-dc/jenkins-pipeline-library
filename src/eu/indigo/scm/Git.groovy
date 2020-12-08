package eu.indigo.scm

import eu.indigo.JenkinsDefinitions

/**
 * Jenkins workflow SCM step
 * @see: https://www.jenkins.io/doc/pipeline/steps/workflow-scm-step/
 */
@groovy.transform.InheritConstructors
class Git extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    def remoteConfigs
    def extensions
    def branches
    protected def gitObject

    /**
    * Define constructor to import definitions from Jenkins context
    * @see https://www.jenkins.io/doc/book/pipeline/shared-libraries/#accessing-steps
    */
    Git(steps) {
        super(steps)
        this.remoteConfigs = []
        this.extensions = steps.scm.extensions
        this.branches = steps.scm.branches
    }

    Git(steps, gitObject) {
        this.Git(steps)
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
        remoteConfigs += [[url: url, name: name, refspec: refspec, credentialsId: credentialsId]]
    }

    @NonCPS
    protected def branches(names) {
        branches = names.collect { name ->
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
        extensions += extension
    }

    def checkoutRepository() {
        if (_DEBUG_) { steps.echo "** Git.checkoutRepository() **" }
        if (remoteConfigs == []) { remoteConfigs = steps.scm.userRemoteConfigs }
        steps.checkout transformGitSCM([
                branches: branches,
                extensions: extensions,
                userRemoteConfigs: remoteConfigs,
            ])
    }

    def checkoutRepository(
            String repository,
            String credentialsId,
            String name='',
            String refspec='',
            String branch='master',
            String targetDirectory='.') {
        if (_DEBUG_) { steps.echo "** Git.checkoutRepository($repository, $credentialsId, $name, $refspec, $branch, $targetDirectory) **" }
        userRemoteConfigs(repository, name, refspec, credentialsId)
        branches(["*/${branch}"])
        extensionsLoader(relativeTargetDirectory(targetDirectory))
        checkoutRepository()
    }

}
