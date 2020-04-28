#!/usr/bin/groovy
package es.ifca.sqa

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




return this

