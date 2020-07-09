package eu.indigo

/**
 * Definitions for Docker Compose integration in Jenkins
 * @see: https://docs.docker.com/compose/compose-file/
 */
class JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    def steps

    protected final boolean _DEBUG_ = true
    protected int logLevel = 0

    /**
    * Define constructor to import definitions from Jenkins context
    * @see https://www.jenkins.io/doc/book/pipeline/shared-libraries/#accessing-steps
    */
    JenkinsDefinitions(steps) {
        this.steps = steps
    }

    void setLogLevel(int level) {
        if(_DEBUG_) {
            logLevel = -1
        }
        else steps.env.JPL_DEBUG ? logLevel = steps.env.JPL_DEBUG
    }

    boolean logTest(int level) {
        logLevel >= level || logLevel == -1
    }

}
