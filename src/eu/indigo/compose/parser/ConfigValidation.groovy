package eu.indigo.compose.parser

/**
 * Configuration validation for yaml provided file
 * @see: https://docs.docker.com/compose/compose-file/
 */
@CompileDynamic
@groovy.transform.InheritConstructors
class ConfigValidation extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    /**
    * Run gradle
    */
    def gradleBuild() {
        steps.sh './gradlew build'
    }

}
