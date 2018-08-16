#!/usr/bin/groovy

def call(testenv, filename=null) {
    opts = ['-e '+testenv]
    if (filename) {
        opts += '-c '+filename
    }
    cmd = ['tox'] + opts
    sh(script: cmd.join(' '))
}
