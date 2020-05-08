#!/usr/bin/groovy

import static eu.indigo.Tox.envRun

/**
 * Run Tox's test environment.
 *
 */
def call(String testenv, String filename=null) {
    envRun(testenv, filename)
}
