#!/usr/bin/groovy
def call(content, filename='tox.ini') {
    testenv_content = '''[tox]
envlist = py27
[testenv]
usedevelop = True
install_command = pip install -U {opts} {packages}
setenv =
   VIRTUAL_ENV={envdir}
deps = -r{toxinidir}/test-requirements.txt
       -r{toxinidir}/requirements.txt'''

    if (fileExists(filename)) {
        def readContent = readFile filename
        writeFile file: filename, text: readContent+'\n'+content
    }
    else {
        writeFile file: filename, text: testenv_content+content
    }
}
