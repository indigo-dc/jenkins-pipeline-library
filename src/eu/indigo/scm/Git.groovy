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
    static transformGitSCM(config) {
        [ $class: 'GitSCM' ] + config
    }

    def checkoutRepository() {
        steps.checkout transformGitSCM(config)
    }

    def checkoutRepository(String repository, String branch='master', String credentialsId) {
        config = [
                branches: [[name: "*/${branch}"]],
                extensions: steps.scm.extensions + [$class: 'RelativeTargetDirectory', relativeTargetDir: '.'],
                userRemoteConfigs: steps.scm.userRemoteConfigs + [url: repository, credentialsId: credentialsId]
            ]
        checkoutRepository()
    }

}
