#!/usr/bin/groovy

/**
 * Dangling Docker image pruning.
 *
 * @param image_id Docker image ID [mandatory]
 */
def call(String image_id) {
    def dangling_images = sh(
		returnStdout: true,
		script: "docker images -f 'dangling=true' -q"
	)
    if (dangling_images) {
        sh(script: "docker rmi --force $dangling_images")
    }
    sh(script: "docker rmi --force $image_id")
}
