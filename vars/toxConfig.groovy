@Library('indigo')
import static eu.indigo.Tox.config

/**
 * Creates a Tox configuration file for py27.
 *
 */
def call(String content, String filename='tox.ini') {
    config(content, filename)
}
