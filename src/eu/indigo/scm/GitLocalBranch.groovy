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
                extensions: steps.scm.extensions + localBranch('**'),
                userRemoteConfigs: userRemoteConfigs(steps.scm.userRemoteConfigs[0].url, 'origin', '+refs/heads/*:refs/remotes/origin/*', steps.scm.userRemoteConfigs[0].credentialsId)
            ])
    }

    @Override
    def checkoutRepository(String repository, String branch='master', String credentialsId) {
        if (_DEBUG_) { steps.echo "** Git.checkoutRepository($repository, $branch, $credentialsId) **" }
        steps.checkout transformGitSCM([
                branches: branches(["*/${branch}"]),
                extensions: steps.scm.extensions + relativeTargetDirectory('.') + localBranch('**'),
                userRemoteConfigs: userRemoteConfigs(repository, 'origin', '+refs/heads/*:refs/remotes/origin/*', credentialsId)
            ])
    }

}
