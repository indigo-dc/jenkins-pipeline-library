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
    def checkoutRepository(config) {
        config = transformGitSCM([
                branches: steps.scm.branches,
                extensions: steps.scm.extensions + [$class: 'LocalBranch', localBranch: '**'],
                userRemoteConfigs: steps.scm.userRemoteConfigs
            ])
        super.checkoutRepository(config)
    }

    @Override
    def checkoutRepository(config, String repository, String branch='master', String credentialsId) {
        config = transformGitSCM([
                branches: [[name: "*/${branch}"]],
                extensions: steps.scm.extensions +
                            [$class: 'RelativeTargetDirectory', relativeTargetDir: '.'] +
                            [$class: 'LocalBranch', localBranch: '**'],
                userRemoteConfigs: steps.scm.userRemoteConfigs + [url: repository, credentialsId: credentialsId]
            ])
        super.checkoutRepository(config)
    }

}
