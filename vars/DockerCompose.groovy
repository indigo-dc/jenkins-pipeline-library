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
def composeUp(String service_ids, String compose_file='', String registry_url='https://hub.docker.com/') {
    docker.withRegistry(registry_url) {
        if (testString(compose_file)) {
            sh "docker-compose –f $compose_file up $service_id"
        }
        else {
            sh "docker-compose up $service_id"
        }
    }
}

/**
 * Run docker compose down
 *
 * @param purge Boolean value. If true docker compose will erase all images and containers.
 * @see https://docs.docker.com/compose/reference/down/
 * @see https://vsupalov.com/cleaning-up-after-docker/
 */
def composeDown(Boolean purge=false) {
    if (purge) {
        sh 'docker-compose down -v --rmi all --remove-orphans'
    }
    else {
        sh 'docker-compose down -v'
    }
}

/**
 * Run the pipeline within Docker Compose
 */
def call(String service_id, String compose_file='', String registry_url='https://hub.docker.com/', Boolean purge=true, Closure stages) {
    stages {
        stage('Docker Compose UP') {
            steps {
                composeUp(service_id, compose_file, registry_url)
            }
        }

        stage('Pipeline inside Docker Compose') {
            stages.call()
        }

        stage('Docker Compose DOWN') {
            steps {
                composeDown(purge)
            }
        }
    }
}
