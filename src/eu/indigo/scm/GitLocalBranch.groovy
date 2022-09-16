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
        if (logTest(1)) { steps.echo "** GitLocalBranch.checkoutRepository() default values: \n credentialsId: $steps.scm.userRemoteConfigs[0].credentialsId \n url: steps.scm.userRemoteConfigs[0].url \n name: steps.scm.userRemoteConfigs[0].name \n refspec: steps.scm.userRemoteConfigs[0].refspec **" }
        steps.checkout transformGitSCM([
                branches: steps.scm.branches,
                extensions: steps.scm.extensions + [$class: 'LocalBranch', localBranch: '**'],
                userRemoteConfigs: [[credentialsId: steps.scm.userRemoteConfigs[0].credentialsId, url: steps.scm.userRemoteConfigs[0].url, name: steps.scm.userRemoteConfigs[0].name, refspec: steps.scm.userRemoteConfigs[0].refspec]]
            ])
    }

    @Override
    def checkoutRepository(String repository, String credentialsId, String name='origin', String refspec='+refs/heads/*:refs/remotes/origin/*', String branch='master', String relativeTargetDir='.') {
        if (logTest(1)) { steps.echo "** GitLocalBranch.checkoutRepository($repository, $branch, $name, $refspec, $relativeTargetDir, $credentialsId) **" }
        steps.checkout transformGitSCM([
                branches: [[name: "*/${branch}"]],
                extensions: steps.scm.extensions +
                            [$class: 'RelativeTargetDirectory', relativeTargetDir: relativeTargetDir] +
                            [$class: 'LocalBranch', localBranch: '**'],
                userRemoteConfigs: [[url: repository, credentialsId: credentialsId, name: name, refspec: refspec]]
            ])
    }

}
