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
        if (_DEBUG_) { steps.echo "** GitLocalBranch.checkoutRepository() **" }
        steps.checkout transformGitSCM([
                branches: steps.scm.branches,
                extensions: steps.scm.extensions + [$class: 'LocalBranch', localBranch: '**'],
                userRemoteConfigs: steps.scm.userRemoteConfigs
            ])
    }

    @Override
    def checkoutRepository(String repository, String branch='master', String credentialsId) {
        if (_DEBUG_) { steps.echo "** Git.checkoutRepository($repository, $branch, $credentialsId) **" }
        steps.checkout transformGitSCM([
                branches: [[name: "*/${branch}"]],
                extensions: steps.scm.extensions +
                            [$class: 'RelativeTargetDirectory', relativeTargetDir: '.'] +
                            [$class: 'LocalBranch', localBranch: '**'],
                userRemoteConfigs: [[url: repository, credentialsId: credentialsId]]
            ])
    }

}
