#!/usr/bin/groovy
import eu.indigo.sqa.*
import groovy.json.JsonSlurper 


/**
 * Checks the license of a GitHub repository.
 *
 * @param owner The owner of the repository [mandatory]
 * @param repository [mandatory]
 */
def call(String owner, String repository) {
    String url = "${GitHub.repository_url}/${owner}/${repository}/license"
    try {
        def jsonText = url.toURL().getText()
        def json = new JsonSlurper().parseText(jsonText)
        def license = new License().retrieveFromSpdx(json.license.spdx_id)
        println "License ${license.name}. Is OSI approved ${license.isOsiApproved}"
    } catch(FileNotFoundException e) {
        println "License not found!"
    }
}
