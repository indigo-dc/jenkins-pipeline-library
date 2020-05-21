#!/usr/bin/groovy
import groovy.json.JsonSlurper 

class License {
    String reference
    Boolean  isDeprecatedLicenseId
    String  detailsUrl
    String  referenceNumber
    String  name
    String  licenseId
    String[]  seeAlso
    Boolean isOsiApproved
    Boolean isFsfLibre
}


License getLicenseData(String licenseId) {
    def jsonSlurper = new JsonSlurper()
    String url = "https://raw.githubusercontent.com/spdx/license-list-data/master/json/licenses.json"
    def jsonText = url.toURL().getText()
    def data = new JsonSlurper().parseText(jsonText)
    return data.licenses.findAll { it.licenseId == licenseId } as License
}


/**
 * Checks the license of a GitHub repository.
 *
 * @param owner The owner of the repository [mandatory]
 * @param repository [mandatory]
 */
def call(String owner, String repository) {
    String GITHUB_API = 'https://api.github.com/repos'
    String url = "${GITHUB_API}/${owner}/${repository}/license"
    try {
       def jsonText = url.toURL().getText()
       def json = new JsonSlurper().parseText(jsonText)
       def license = getLicenseData(json.license.spdx_id)
       println "Is OSI approved ${license.isOsiApproved}"
    } catch(FileNotFoundException e) {
      println "License not found!"
   }
    
}


