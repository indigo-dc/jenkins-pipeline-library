#!/usr/bin/groovy

/**
 * Test if argument is not an empty string
 *
 * @param text The variable with the string to test
 */
def testString(String text) {
    return text && !text.allWhitespace
}


/**
 * Run docker compose up
 *
 * @param service_ids List of Services name to start [default]
 * @param compose_file Docker compose file to override the default docker-compose.yml [default]
 * @param registry_url Docker registry URL [default]
 * @see https://docs.docker.com/compose/reference/up/
 * @see https://docs.docker.com/compose/reference/overview/
 */
def up(String service_ids, String compose_file='', String registry_url='https://hub.docker.com/') {
    docker.withRegistry(registry_url) {
        if (testString(compose_file)) {
            sh "docker-compose –f $compose_file up $service_id"
        }
        else {
            sh "docker-compose up $service_id"
        }
    }
}

def call(String service_id, String compose_file='', String registry_url='https://hub.docker.com/') {
    up(service_id, compose_file, registry_url)
}
