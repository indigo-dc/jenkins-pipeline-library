package eu.indigo

import eu.indigo.JenkinsDefinitions

/**
 * Definitions for Maven automation project
 * @see: https://www.baeldung.com/maven-goals-phases
 */
@groovy.transform.InheritConstructors
class Maven extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    /**
     * Run Maven environment
     *
     * @param args.mavenFile Maven configuration file to override the default pom.xml
     * @param args.options List of options for mvn command
     * @param goals List of phases and/or goals separated by spaces to run with Maven
     */
    def runEnv(Map args, String goals) {
        return "maven -f \"${args.mavenFile}\" $options $goals"
    }

}
