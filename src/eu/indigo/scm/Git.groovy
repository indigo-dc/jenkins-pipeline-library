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

    /**
    * Define constructor to import definitions from Jenkins context
    * @see https://www.jenkins.io/doc/book/pipeline/shared-libraries/#accessing-steps
    */
    Git(steps) {
        super(steps)
        this.remoteConfigs = []
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
        names.collect { name ->
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

    def checkoutRepository() {
        if (_DEBUG_) { steps.echo "** Git.checkoutRepository() **" }
        steps.checkout steps.scm
    }

    def checkoutRepository(String repository, String branch='master', String credentialsId) {
        if (_DEBUG_) { steps.echo "** Git.checkoutRepository($repository, $branch, $credentialsId) **" }
        userRemoteConfigs(repository, '', '', credentialsId)
        steps.checkout transformGitSCM([
                branches: branches(["*/${branch}"]),
                extensions: steps.scm.extensions + relativeTargetDirectory('.'),
                userRemoteConfigs: remoteConfigs
            ])
    }

}
