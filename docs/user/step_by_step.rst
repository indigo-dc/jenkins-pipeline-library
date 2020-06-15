Step-by-step guide
==================

 * You have a Python-based software project whose source code is openly
   available in the GitHub platform.
 * You want to enhance the quality of this software, and in particular,
   you would like to improve:
   1. The readability of the code since you start a new research collaboration
   that will imply external contributions to that code, or you have
   received positive feedback about the software and you would like to
   guarantee its long-term sustainability.
   2. The reliability and deterministic behaviour of the software, as you have
   detected unexpected behaviours when running this software.
 * You have read the
   `SQA baseline <https://indigo-dc.github.io/sqa-baseline>`_, so you know
   that:
   1. The readability is aligned with the good practices in the
   `QC.Sty <https://indigo-dc.github.io/sqa-baseline/#code-style-qc.sty>`_
   category. You have decided that the Python's `PEP8
   <https://www.python.org/dev/peps/pep-0008/>`_ standard is the most
   suitable for your goals.
   2. The reliability of the software is improved by considering the practices
   in the
   `QC.Uni <https://indigo-dc.github.io/sqa-baseline/#unit-testing-qc.uni>`_,
   `QC.Fun <https://indigo-dc.github.io/sqa-baseline/#functional-testing-qc.fun>`_
   and
   `QC.Int <https://indigo-dc.github.io/sqa-baseline/#integration-testing-qc.int>`_.
   You have got familiar with the unit testing libraries available in
   Python and started writing a few test cases. For the time being, you just
   want to cover the QC.Uni category.of the baseline.
 * You want to execute the previous checks in an automated fashion for every
   change in your source code. You heard about the jenkins-pipeline-library
   (that's why you are reading this) and want to start using it.

So, let's see how to set it up.

Minimal configuration
---------------------
We will use the fictional repository *https://github.com/myorg/myrepo*. The
required steps to set up the layout would imply the following steps:

1. Clone the repo

.. code:: bash

   $ export MY_REPO=https://github.com/myorg/myrepo
   $ git clone $MY_REPO

2. A good practice is to add the changes herein described in an individual
   branch, so not directly in the production --usually ``master``-- branch. In
   this example we will use ``setup_jenkins-pipeline-library`` as follows:

.. code:: bash

   $ cd $MY_REPO
   $ git checkout -b setup_jenkins-pipeline-library


3. We will now create the directory structure required by the
   jenkins-pipeline-library, *as introduced in section*
   :ref:`Layout`.

   3.1. Main folder:

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

      $ cat <<EOF > .sqa/config.yml
      version: "3.6"
      EOF

   3.4. In the *root path of the code repository*, create the ``Jenkisfile``,
   file required by Jenkins. In order to make it work with the
   jenkins-pipeline-library, at least the following content must be present
   (see :ref:`The pipeline: Jenkinsfile` section):

   .. code:: bash

      $ cat <<EOF > Jenkinsfile
      @Library(['github.com:indigo-dc/jenkins-pipeline-library@2.0.0']) _

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
    $ git push origin setup_jenkins-pipeline-library
