#!/usr/bin/groovy

def call(String testenv, String filename=null) {
    opts = ['-e '+testenv]
    if (filename) {
        opts += '-c '+filename
    }
    cmd = ['tox'] + opts
    sh(script: cmd.join(' '))
}
