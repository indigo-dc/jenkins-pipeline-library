#!/usr/bin/groovy

/**
 * Run Tox's test environment.
 *
 */
def call(String testenv, String filename=null) {
    opts = ['-e '+testenv]
    if (filename) {
        opts += '-c '+filename
    }
    cmd = ['tox'] + opts
    sh(script: cmd.join(' '))
}
