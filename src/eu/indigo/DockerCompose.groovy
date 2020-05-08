#!/usr/bin/groovy
package eu.indigo

/**
 * Definitions for Docker Compose integration in Jenkins
 * @see: https://docs.docker.com/compose/compose-file/
 */
@CompileDynamic
@groovy.transform.InheritConstructors
class DockerCompose extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    /**
    * Parameters static strings for command parser
    */
    _f = '-f'
    _w = '-w'

    /**
    * Test if argument is not an empty string
    *
    * @param text The variable with the string to test
    */
    def testString(String text) {
        return text && !text.allWhitespace
    }

    /**
    * Receives a tuple with the flag and the value and returns a string
    *
    * @param param Tuple with the flag and value to test
    */
    def parseParam(Tuple2 param) {
        if(testString(param.second)) {
            return param.first + ' ' + param.second
        }
        else {
            return ''
        }
    }

    /**
    * Run docker compose exec
    *
    * @param service Service name
    * @param command Command with arguments to run inside container
    * @param compose_file Docker compose file to override the default docker-compose.yml
    * @param workdir Path to workdir directory for this command
    * @see https://docs.docker.com/compose/reference/exec/
    */
    def composeExec(String service, String command, String compose_file='', String workdir='') {
        cmd = parseParam(new Tuple2(_f, compose_file)) + ' exec ' + \
                parseParam(new Tuple2(_w, workdir)) + " $service $command"

        sh "docker-compose $cmd"
    }

    /**
    * Run docker compose up
    *
    * @param service_ids String with list of Service names separated by spaces to start [default]
    * @param compose_file Docker compose file to override the default docker-compose.yml [default]
    * @param registry_url Docker registry URL [default]
    * @see https://docs.docker.com/compose/reference/up/
    * @see https://docs.docker.com/compose/reference/overview/
    */
    def composeUp(String service_ids, String compose_file='', String registry_url='https://hub.docker.com/') {
        cmd = parseParam(new Tuple2(_f, compose_file)) + " up $service_ids"

        docker.withRegistry(registry_url) {
            sh "docker-compose $cmd"
        }
    }

    /**
    * Run docker compose down
    *
    * @param purge Boolean value. If true docker compose will erase all images and containers.
    * @see https://docs.docker.com/compose/reference/down/
    * @see https://vsupalov.com/cleaning-up-after-docker/
    */
    def composeDown(Boolean purge=false, String compose_file='') {
        cmd = parseParam(new Tuple2(_f, compose_file))

        if (purge) {
            sh 'docker-compose $cmd down -v --rmi all --remove-orphans'
        }
        else {
            sh 'docker-compose $cmd down -v'
        }
    }

    /**
    * Copy file or directory into docker container
    *
    * @param service Service name
    * @param src_path copies the contents of source path
    * @param dest_path copies the contents to the destination path
    * @param compose_file Docker compose file to override the default docker-compose.yml
    * @see https://docs.docker.com/engine/reference/commandline/cp/
    * @see https://blog.dcycle.com/blog/ae67284c/docker-compose-cp
    * @see https://docs.docker.com/compose/reference/ps/
    */
    def composeCP(String service, String src_path, String dest_path, String compose_file='', String workdir='') {
        sh "docker cp $src_path \"\$(docker-compose " + \
            parseParam(new Tuple2(_f, compose_file)) + " ps -q $service)\":$dest_path"
    }

    /**
    * Run docker compose exec
    *
    * @param service Service name
    * @param command Command with arguments to run inside container
    * @param compose_file Docker compose file to override the default docker-compose.yml
    * @param workdir Path to workdir directory for this command
    * @see https://docs.docker.com/compose/reference/exec/
    */
    def composeToxRun(String service, String command, String compose_file='', String workdir='') {
        cmd = parseParam(new Tuple2(_f, compose_file)) + ' exec ' + \
                parseParam(new Tuple2(_w, workdir)) + " $service $command"

        sh "docker-compose $cmd"
    }
}
