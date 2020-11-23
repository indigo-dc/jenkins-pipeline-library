package eu.indigo.scm

import eu.indigo.JenkinsDefinitions

/**
 * Jenkins workflow SCM step
 * @see: https://www.jenkins.io/doc/pipeline/steps/workflow-scm-step/
 */
@groovy.transform.InheritConstructors
class Git extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    protected def config


    /**
    * Define constructor to import definitions from Jenkins context
    * @see https://www.jenkins.io/doc/book/pipeline/shared-libraries/#accessing-steps
    */
    Git(steps) {
        this.config = steps.scm
    }

    def checkoutRepository() {
        steps.checkout this.config
    }

    def checkoutRepository(String repository, String branch='master', String credentialsId) {
        this.config = [
                $class: 'GitSCM',
                branches: [[name: "*/${branch}"]],
                extensions: steps.scm.extensions + [$class: 'RelativeTargetDirectory', relativeTargetDir: '.'],
                userRemoteConfigs: steps.scm.userRemoteConfigs + [url: repository, credentialsId: credentialsId]
            ]
        checkoutRepository()
    }

}
