package eu.indigo.scm

import eu.indigo.JenkinsDefinitions

/**
 * Jenkins workflow SCM step
 * @see: https://www.jenkins.io/doc/pipeline/steps/workflow-scm-step/
 */
@groovy.transform.InheritConstructors
class Git extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L


    /**
    * Define constructor to import definitions from Jenkins context
    * @see https://www.jenkins.io/doc/book/pipeline/shared-libraries/#accessing-steps
    */
    Git(steps) {
        super(steps)
    }

    @NonCPS
    protected def transformGitSCM(config) {
        [ $class: 'GitSCM' ] + config
    }

    def checkoutRepository() {
        if (_DEBUG_) { steps.echo "** Git.checkoutRepository() **" }
        steps.checkout steps.scm
    }

    def checkoutRepository(String repository, String credentialsId, String name='origin', String refspec='+refs/heads/*:refs/remotes/origin/*', String branch='master', String relativeTargetDir='.') {
        if (_DEBUG_) { steps.echo "** Git.checkoutRepository($repository, $branch, $name, $refspec, $relativeTargetDir, $credentialsId) **" }
        steps.checkout transformGitSCM([
                branches: [[name: "*/${branch}"]],
                extensions: steps.scm.extensions + [$class: 'RelativeTargetDirectory', relativeTargetDir: '.'],
                userRemoteConfigs: [[url: repository, credentialsId: credentialsId]]
            ])
    }

}
