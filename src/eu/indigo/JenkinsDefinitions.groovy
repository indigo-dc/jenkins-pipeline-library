package eu.indigo

/**
 * Definitions for Docker Compose integration in Jenkins
 * @see: https://docs.docker.com/compose/compose-file/
 */
@CompileDynamic
class JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    /**
    * Define constructor to import definitions from Jenkins context
    * @see https://www.jenkins.io/doc/book/pipeline/shared-libraries/#accessing-steps
    */
    def steps
    JenkinsDefinitions(steps) {
        this.steps = steps
    }
}
