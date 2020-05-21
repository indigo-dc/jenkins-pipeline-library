#!/usr/bin/groovy

import java.nio.file.Paths
import hudson.util.Secret

/**
 * Gets credential object from Jenkins identifier.
 *
 * @param  credential_id Jenkins credential identifier 
 * @return Jenkins credential object
 * @see https://plugins.jenkins.io/credentials
 */
def get_creds(String credential_id) {
    def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.Credentials.class,
        Jenkins.instance,
        null,
        null
    );
    for (c in creds) {
        if (c.id == "${credential_id}") {
            return c
        } 
    }
}

/**
 * Builds Python application.
 *
 */
def build() {
    sh(script: 'python setup.py sdist bdist_wheel')
}

/**
 * Tests and uploads Python application.
 * 	See https://hynek.me/articles/sharing-your-labor-of-love-pypi-quick-and-dirty/
 *
 * @param  module_name Name of the Python application, matching the module to be imported
 */
def test(String module_name) {
    ["whl", "tar.gz"].each { ext ->
        def cnt = 0
        files = findFiles(glob: "**/dist/*.${ext}")
        files.each { file ->
            println(file.name)
            sh """
            [ -d "venv-${ext}-${cnt}" ] && rm -rf venv-${ext}-${cnt}
            virtualenv "venv-${ext}-${cnt}"
            . venv-${ext}-${cnt}/bin/activate
            pip install dist/${file.name}
            venv-${ext}-${cnt}/bin/python -c 'import ${module_name}; print(${module_name}.__version__)'
            deactivate
            """
            cnt += 1
        }
    }
}

/**
 * Pushes Python application to PyPI.
 *
 * @param  pypi_user User of PyPI account
 * @param  pypi_pass Password of PyPI account 
 */
def publish(String pypi_user, hudson.util.Secret pypi_pass) {
    content = """
[distutils]
index-servers =
    testpypi
    pypi

[testpypi]
repository: https://test.pypi.org/legacy/
username: ${pypi_user}
password: ${pypi_pass}

[pypi]
repository: https://upload.pypi.org/legacy/
username: ${pypi_user}
password: ${pypi_pass}"""
    writeFile file: '/tmp/.pypirc', text: content
    //sh 'twine upload --config-file /tmp/.pypirc --repository testpypi --skip-existing "dist/*"'
    sh 'twine upload --config-file /tmp/.pypirc --repository pypi --skip-existing "dist/*"'
}

/**
 * PyPIDeploy main method.
 *
 * @param  module_name Name of the Python application, matching the module to be imported
 * @param  credential_id Jenkins credential identifier 
 */
def call(String module_name, String credential_id) {
    creds = get_creds(credential_id)
    build()
    test(module_name)
    publish(creds.username, creds.password)
}


