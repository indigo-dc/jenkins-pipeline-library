package eu.indigo

import eu.indigo.JenkinsDefinitions

/**
 * Definitions for Tox automation project
 * @see: https://tox.readthedocs.io/en/latest/
 */
@groovy.transform.InheritConstructors
class Tox extends JenkinsDefinitions implements Serializable {

    private static final long serialVersionUID = 0L

    /**
    * Creates a Tox configuration file for py27.
    */
    def config(String content, String filename='tox.ini') {
        testenv_content = libraryResource 'eu/indigo/tox.ini'
        content_all = testenv_content + content
        if (filename == 'tox.ini') {
            if (fileExists(filename)) {
                readContent = readFile(filename)
                content_all = readContent + '\n' + content
            }
        }
        writeFile(file: filename, text: content_all)
    }

    /**
    * Run Tox's test environment.
    */
    def envRun(String testenv, String filename=null) {
        opts = ['-e ' + testenv]
        if (filename) {
            opts += '-c '+filename
        }
        cmd = ['tox'] + opts
        steps.sh(script: cmd.join(' '))
    }

    /**
     * Run Tox inside a container
     */
    def composeToxRun(String testenv, String filename=null) {
        opts = ['-e ' + testenv]
        if (filename) {
            opts += '-c '+filename
        }
        cmd = ['tox'] + opts
        steps.sh(script: cmd.join(' '))
    }

}
