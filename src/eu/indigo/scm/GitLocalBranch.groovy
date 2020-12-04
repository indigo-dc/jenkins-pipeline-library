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
        steps.checkout transformGitSCM(checkout([
            branches: scm.branches,
            doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
            extensions: scm.extensions + [$class: 'LocalBranch', localBranch: '**'],
            userRemoteConfigs: [[
                credentialsId: scm.userRemoteConfigs[0].credentialsId,
                name: 'origin',
                refspec: '+refs/heads/*:refs/remotes/origin/*',
                url: scm.userRemoteConfigs[0].url
            ]],
        ]))
    }

    @Override
    def checkoutRepository(String repository, String branch='master', String credentialsId) {
        if (_DEBUG_) { steps.echo "** Git.checkoutRepository($repository, $branch, $credentialsId) **" }
        steps.checkout transformGitSCM([
                branches: branch,
                doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
                extensions: scm.extensions + [$class: 'LocalBranch', localBranch: '**'],
                userRemoteConfigs: [[
                    credentialsId: credentialsId,
                    name: 'origin',
                    refspec: '+refs/heads/*:refs/remotes/origin/*',
                    url: repository
                ]],
            ])
    }

}
