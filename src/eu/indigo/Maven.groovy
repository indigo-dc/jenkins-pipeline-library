package eu.indigo

import eu.indigo.JenkinsDefinitions

/**
 * Definitions for Maven automation project
 * @see: https://maven.readthedocs.io/en/latest/
 */
@groovy.transform.InheritConstructors
class Maven extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    /**
    * Run Maven's test environment.
    */
    def runTest(String goal, String filename=null) {
        String opts = ['-e ' + goal]
        if (filename) {
            opts += '-c '+filename
        }
        String cmd = ['maven'] + opts
        steps.sh(script: cmd.join(' '))
    }

    /**
     * Run Maven environment
     *
     * @param args.MavenFile Maven configuration file to override the default pom.xml
     * @param goal Test environment to run with Maven
     */
    def runEnv(Map args, String goal) {
        return "maven -c \"${args.MavenFile}\" -e $goal"
    }

}
