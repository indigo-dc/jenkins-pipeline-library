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

    GitLocalBranch(steps, properties, gitObject, localBranch) {
        super(steps, properties, gitObject)
        super.properties.localBranch = localBranch
    }

    @Override
    def checkoutRepository() {
        if (_DEBUG_) { steps.echo "** GitLocalBranch.checkoutRepository() **" }
        userRemoteConfigs(steps.scm.userRemoteConfigs[0].url, 'origin', '+refs/heads/*:refs/remotes/origin/*', steps.scm.userRemoteConfigs[0].credentialsId)
        extensionsLoader(localBranch(properties.localBranch))
        checkoutScm()
    }

    @Override
    def checkoutRepository(
            String repository,
            String credentialsId,
            String name='',
            String refspec='',
            String branch='master',
            String targetDirectory='.') {
        if (_DEBUG_) { steps.echo "** GitLocalBranch.checkoutRepository($repository, $credentialsId, $name, $refspec, $branch, $targetDirectory) **" }
        extensionsLoader(localBranch(properties.localBranch))
        checkoutScm(repository, credentialsId, name, refspec, branch, targetDirectory)
    }

}