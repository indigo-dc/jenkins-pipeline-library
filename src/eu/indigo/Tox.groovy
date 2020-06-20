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
    * Run Tox's test environment.
    */
    def runTest(String testenv, String filename=null) {
        String opts = ['-e ' + testenv]
        if (filename) {
            opts += '-c '+filename
        }
        String cmd = ['tox'] + opts
        steps.sh(script: cmd.join(' '))
    }

    /**
     * Run Tox environment
     *
     * @param args.toxFile Tox configuration file to override the default tox.ini
     * @param testenv Test environment to run with tox
     */
    def runEnv(Map args, String testenv) {
        return "tox -c \"${args.toxFile}\" -e $testenv"
    }

}
