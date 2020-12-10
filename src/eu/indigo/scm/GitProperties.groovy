package eu.indigo.scm

import eu.indigo.JenkinsDefinitions

/**
 * Jenkins workflow SCM step
 * @see: https://www.jenkins.io/doc/pipeline/steps/workflow-scm-step/
 */
@groovy.transform.InheritConstructors
class GitProperties extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    def remoteConfigs
    def extensions
    def branches
    def localBranch

    /**
    * Define constructor to import definitions from Jenkins context
    * @see https://www.jenkins.io/doc/book/pipeline/shared-libraries/#accessing-steps
    */
    GitProperties(steps) {
        super(steps)
        this.remoteConfigs = []
        this.extensions = steps.scm.extensions
        this.branches = steps.scm.branches
        this.localBranch = ''
    }

}