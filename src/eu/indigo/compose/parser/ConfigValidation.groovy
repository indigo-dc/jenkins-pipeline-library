package eu.indigo.compose.parser

import eu.indigo.JenkinsDefinitions

@Grab('com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.10.1')
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

@Grab('com.networknt:json-schema-validator:1.0.29')
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import com.networknt.schema.JsonSchema

/**
 * Configuration validation for yaml provided file
 * @see: https://docs.docker.com/compose/compose-file/
 */
class ConfigValidation implements Serializable {

    private static final long serialVersionUID = 0L

    def validate(yml, schema) {

        ObjectMapper objMapper = new ObjectMapper(new YAMLFactory())

        JsonSchemaFactory factory = JsonSchemaFactory.builder(
                JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7))
                .objectMapper(objMapper).build()

        Set invalidMessages = factory.getSchema(schema)
                .validate(objMapper.readTree(yml))
                .message
        return invalidMessages
    }

}
