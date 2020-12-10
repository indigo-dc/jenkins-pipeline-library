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
    def checkoutRepository(Map settings) {
        settings = setDefaults(settings)
        if (_DEBUG_) { steps.echo "** GitLocalBranch.checkoutRepository(${settings.baseRepository}, ${settings.credentialsId}, ${settings.baseBranch}, ${settings.remoteName}, ${settings.refspec}, ${settings.relativeTargetDir}) **" }
        extensionsLoader(localBranch(properties.localBranch))
        checkoutScm(baseRepository: settings.baseRepository, credentialsId: settings.credentialsId, baseBranch: settings.baseBranch, relativeTargetDir: settings.relativeTargetDir, remoteName: settings.remoteName, refspec: settings.refspec)
    }

}
