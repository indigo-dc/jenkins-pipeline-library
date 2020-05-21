@Library(['github.com:WORSICA/jenkins-pipeline-library@docker-compose'])
import eu.indigo.compose.parser.ConfigParser
import eu.indigo.compose.ProjectConfiguration
import eu.indigo.compose.parser.ConfigValidation

def call(String yamlFile='./.sqa/config.yml') {
    def yamlContent = readFile file: yamlName
    def yaml = readYaml file: yamlFile
    def schema = libraryResource('eu/indigo/compose/parser/schema.json')
    def buildNumber = Integer.parseInt(env.BUILD_ID)
    ProjectConfiguration projectConfig = null

    // validate config.yml
    validator = new ConfigValidation(this)
    if ( validator.validate(yamlContent, schema) ) {
        projectConfig = ConfigParser.parse(yaml, env)
    }
    return projectConfig

}
