#!/usr/bin/groovy
package eu.indigo.sqa

import groovy.json.JsonSlurper 

class License {
    String  licenseId
    String  name
    String reference
    Boolean  isDeprecatedLicenseId
    String  detailsUrl
    String  referenceNumber  
    String[]  seeAlso
    Boolean isOsiApproved
    Boolean isFsfLibre

    License retrieveFromSpdx(java.lang.String licenseId) {
        String url = "https://raw.githubusercontent.com/spdx/license-list-data/master/json/licenses.json"
        def jsonText = url.toURL().getText()
        def data = new JsonSlurper().parseText(jsonText)
        return  data.licenses.findAll { it.licenseId == licenseId } as License
    }   
}

