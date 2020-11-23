package eu.indigo.scm

import eu.indigo.scm.Git

/**
 * Jenkins workflow SCM step
 * Use 'LocalBranch' class to avoid detached HEAD state
 * @see: https://www.jenkins.io/doc/pipeline/steps/workflow-scm-step/
 */
@groovy.transform.InheritConstructors
class GitLocalBranch extends Git implements Serializable {

    private static final long serialVersionUID = 0L

    @Override
    def checkoutRepository() {
        this.config = [
                $class: 'GitSCM',
                branches: steps.scm.branches,
                extensions: steps.scm.extensions + [$class: 'LocalBranch', localBranch: '**'],
                userRemoteConfigs: steps.scm.userRemoteConfigs
            ]
        super.checkoutRepository()
    }

    @Override
    def checkoutRepository(String repository, String branch='master', String credentialsId) {
        this.config = [
                $class: 'GitSCM',
                branches: [[name: "*/${branch}"]],
                extensions: steps.scm.extensions +
                            [$class: 'RelativeTargetDirectory', relativeTargetDir: '.'] +
                            [$class: 'LocalBranch', localBranch: '**'],
                userRemoteConfigs: steps.scm.userRemoteConfigs + [url: repository, credentialsId: credentialsId]
            ]
        super.checkoutRepository()
    }

}