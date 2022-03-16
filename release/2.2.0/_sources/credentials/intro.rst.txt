JePL credentials
=================

Introduction
------------

jenkins-pipeline-library supports the available credentials bindings
from `Jenkins automation server <https://jenkins.eosc-synergy.eu/>`_. The
credentials can be defined in config.yml configuration file and used for each stage.

Credentials stored in Jenkins can be used:

  - as global credentials using environment variables
  - by any specific Pipeline project represented in config.yml or Jenkinsfile
  - in docker configuration files (Dockerfile or docker-compose.yml)

JePL can use the following type of credentials:

  - ``Secret text``: a token such as an API token (e.g. a GitHub personal access token)

  - ``Username and password``: which could be handled as separate components or as a colon separated string in the format username:password (read more about this in Handling credentials)

  - ``Secret file``: which is essentially secret content in a file or zipped file
  
  - ``SSH Username with private key``: an SSH public/private key pair
  
  - ``Certificate``: a PKCS#12 certificate file and optional password

Credentials security
--------------------

To maximize security, credentials configured in Jenkins are stored in an
encrypted form on the controller Jenkins instance (encrypted by the Jenkins
instance ID) and are only handled in Pipeline projects via their credential IDs.

This minimizes the chances of exposing the actual credentials themselves to
Jenkins users and hinders the ability to copy functional credentials from one
Jenkins instance to another. 
