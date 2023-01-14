A minimal configuration
=======================
We will use the fictional repository *https://github.com/myorg/myrepo*. The
following steps set up the minimum layout:

1. Clone the repo

.. code:: bash

   $ export MY_REPO=https://github.com/myorg/myrepo
   $ git clone $MY_REPO

2. A `good practice
   <https://indigo-dc.github.io/sqa-baseline/#code-workflow-qc.wor>`_ is to add
   the changes herein described in an individual branch, so not directly in the
   production --usually ``master``-- branch. In this example we will use
   ``setup_jenkins-pipeline-library`` as follows:

.. code:: bash

   $ cd $MY_REPO
   $ git checkout -b setup_jenkins-pipeline-library


3. We will now create the following directory structure within the code
   repository, as required by the library (be sure to read :ref:`Layout`
   section):

   ::

       |-- .sqa
       |    |-- config.yml
       |    |-- docker-compose.yml
       |-- Jenkinsfile

   3.1. Create the main ``.sqa`` folder:

   .. code:: bash

      $ mkdir .sqa

   3.2. Create the initial content of the main configuration file,
   ``.sqa/config.yml``, with the description of your repository (see
   :ref:`The configuration file: config.yml` section):

   .. code:: bash

      $ cat <<EOF > .sqa/config.yml
      config:
        project_repos:
          myrepo:
            repo: 'https://github.com/myorg/myrepo'
      EOF

   3.3. Create the ``.sqa/docker-compose.yml``. For the time being, we will
   only specify the version required by the library (i.e. ``3.6``), later on
   we will add the service definitions (see
   :ref:`The services: docker-compose.yml` section):

   .. code:: bash

      $ cat <<EOF > .sqa/docker-compose.yml
      version: "3.6"
      EOF

   3.4. In the *root path of the code repository*, create the ``Jenkinsfile``,
   file required by Jenkins. In order to make it work with the
   jenkins-pipeline-library, at least the following content must be present
   (see :ref:`The pipeline: Jenkinsfile` section):

   .. code:: bash

      $ cat <<EOF > Jenkinsfile
      @Library(['github.com/indigo-dc/jenkins-pipeline-library@release/2.1.0']) _

      def projectConfig

      pipeline {
          agent any

          stages {
              stage('SQA baseline dynamic stages') {
                  steps {
                      script {
                          projectConfig = pipelineConfig()
                          buildStages(projectConfig)
                      }
                  }
                  post {
                      cleanup {
                          cleanWs()
                      }
                  }
              }
          }
      }
      EOF

4. Commit & push the layout files:

.. code:: bash

    $ git add .sqa Jenkinsfile
    $ git commit -m "Initial setup of jenkins-pipeline-library files"


Now that we have the skeleton with an initial version of the three relevant
files, let's add our checks through the ``sqa_criteria`` setting. We will see
how to do that in the next section.
