#!/usr/bin/groovy
package eu.indigo.sqa

import groovy.json.JsonSlurper 
import com.cloudbees.groovy.cps.NonCPS

/**
 * Returns an array of strings relying on newline character.
 *
 * @param multline_str Multiline string [mandatory]
 */
@NonCPS
def multilineToArray(String multiline_str) {
    return multiline_str.split('\n').toList()
}


def urlStringToJson(String url) {
    def text = url.toURL().getText()
    def json = new JsonSlurper().parseText(text)
    return json;
}

return this

