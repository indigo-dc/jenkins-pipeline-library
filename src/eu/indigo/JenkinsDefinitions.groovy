package eu.indigo

/**
 * Definitions for Docker Compose integration in Jenkins
 * @see: https://docs.docker.com/compose/compose-file/
 */
class JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    def steps

    final Boolean DEBUG = true

    /**
    * Define constructor to import definitions from Jenkins context
    * @see https://www.jenkins.io/doc/book/pipeline/shared-libraries/#accessing-steps
    */
    JenkinsDefinitions(steps) {
        this.steps = steps
    }

}
