Configurations
===============

config.yml: configuring credentials
-----------------------------------

In Jenkins configurations section is explained how to add the required
``credentials``. Here is explained how to use credentials with JePL itself. *config*
section of *config.yml* supports the keyword credentials that allows to define
an array of credentials of the following types:

- secret text
- username and password
- secret file
- ssh username with private key
- certificate

The difference between them are the keywords for each one. The association is
made by the credentials id from Jenkins, where the credential type is defined.
The following table resumes the required parameters for each case:

+-------------------------------+----------------------------------------------+
| Credential Type               | Properties                                   |
+===============================+==============================================+
| secret text                   | ``id``: jenkins identifier                   |
|                               |                                              |
|                               | ``variable``: environment variable with      |
|                               | secret                                       |
+-------------------------------+----------------------------------------------+
| username and password         | ``id``: jenkins identifier                   |
|                               |                                              |
|                               | ``username_var``: environment variable for   |
|                               | username                                     |
|                               |                                              |
|                               | ``password_var``: environment variable to    |
|                               | receive the password                         |
+-------------------------------+----------------------------------------------+
| secret file                   | ``id``: jenkins identifier                   |
|                               |                                              |
|                               | ``variable``: environment variable with file |
|                               | content.                                     |
+-------------------------------+----------------------------------------------+
| ssh username with private key | ``id``: jenkins identifier                   |
|                               |                                              |
|                               | ``keyfile_var``: environment variable to     |
|                               | receive the private key                      |
|                               |                                              |
|                               | ``passphrase_var``: environment variable to  |
|                               | receive the private key passphrase           |
|                               |                                              |
|                               | ``username_var``: environment variable for   |
|                               | username                                     |
+-------------------------------+----------------------------------------------+
| certificate                   | ``id``: jenkins identifier                   |
|                               |                                              |
|                               | ``keystore_var``: environment variable set   |
|                               | to the keystore temporary location           |
|                               |                                              |
|                               | ``alias_var``: environment variable set to   |
|                               | the keystore alias name of the certificate   |
|                               |                                              |
|                               | ``password_var``: environment variable to    |
|                               | receive the password                         |
+-------------------------------+----------------------------------------------+

In the next configuration examples, the values there have the following meaning:

- *jenkins_credential_id*: this is the id associated to the Jenkins credential
- *ENV_**: name of the environment variable to be set with the correspondent value defined in Jenkins credential

*Examples:*
    .. tabs::

        .. tab:: Secret text

           .. code-block:: yaml

              config:
                credentials:
                  - id: jenkins_credential_id
                    variable: ENV_VAR

        .. tab:: Username and password

           .. code-block:: yaml

              config:
                credentials:
                  - id: jenkins_credential_id
                    username_var: ENV_USER
                    password_var: ENV_PASS

        .. tab:: Secret file

           .. code-block:: yaml

              config:
                credentials:
                  - id: jenkins_credential_id
                    variable: ENV_VAR_FILE

        .. tab:: SSH Username with private key

           .. code-block:: yaml

              config:
                credentials:
                  - id: jenkins_credential_id
                    keyfile_var: ENV_PRIVATE_KEY
                    passphrase_var: ENV_PASS
                    username_var: ENV_USER

        .. tab:: Certificate

           .. code-block:: yaml

              config:
                credentials:
                  - id: jenkins_credential_id
                    keystore_var: ENV_KEYSTORE
                    alias_var: ENV_CERT_ALIAS
                    password_var: ENV_PASS
