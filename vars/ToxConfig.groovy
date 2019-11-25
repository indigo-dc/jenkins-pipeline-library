#!/usr/bin/groovy

/**
 * Creates a Tox configuration file for py27.
 *
 *@param  content Additional configuration content.
 *@param  filename The base Tox configuration file, defaults to tox.ini
 */
def call(String content, String filename='tox.ini') {
    testenv_content = '''[tox]
envlist = py27
[testenv]
usedevelop = True
install_command = pip install -U {opts} {packages}
setenv =
   VIRTUAL_ENV={envdir}
deps = -r{toxinidir}/test-requirements.txt
       -r{toxinidir}/requirements.txt
'''
    content_all = testenv_content+content
    if (filename == 'tox.ini') {
        if (fileExists(filename)) {
            def readContent = readFile filename
            content_all = readContent+'\n'+content
        }
    }
    writeFile file: filename, text: content_all
}

