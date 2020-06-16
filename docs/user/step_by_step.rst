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

The SQA criteria
----------------
In this section we will cover the ``sqa-criteria`` setting, which represents
the fundamental part of the configuration since it contains the definitions of
the checks that comprise the quality criteria. The criteria currently supported
is documented in :ref:`sqa_criteria`, but in short it is currently reduced to:

+-----------------------------+------------------------------------------------------------------------+
| ``sqa-criteria`` setting    | What does it cover?                                                    |
+=============================+========================================================================+
| ``qc-style``                | Make your code compliant with a style standard                         |
+-----------------------------+------------------------------------------------------------------------+
| ``qc-coverage``             | Calculate the unit testing coverage of your code                       |
+-----------------------------+------------------------------------------------------------------------+
| ``qc-functional``           | Test the main features of your software                                |
+-----------------------------+------------------------------------------------------------------------+
| ``qc-security``             | Assess the security (uncover vulnerabilities & bad security practices) |
+-----------------------------+------------------------------------------------------------------------+
| ``qc-doc``                  | Generate the documentation                                             |
+-----------------------------+------------------------------------------------------------------------+

The definition of the ``sqa-criteria`` settings in the ``.sqa/config.yml`` file
follow a similar approach. As such, the following examples will only cover the
``qc-style`` setting, but the others can be implemented in a similar fashion.
Check out the previously referrenced :ref:`sqa_criteria` sections for the
specific parameters within each setting.

The current version of the library supports both ``commands`` and ``tox`` (only
for Python-based applications) to run the validaton checks. The examples present
working ``config.yml`` definitions, both for Python and Java programming
languages, covering all the use cases --i.e. Java & Python with and without
tox--, as well as the associated ``docker-compose.yml`` services (``tox.ini``
are also available when applicable).

Python with ``tox``
^^^^^^^^^^^^^^^^^^^

.. tabs::

    .. tab:: config.yml

       .. code-block:: yaml

          config:
            project_repos:
              myrepo:
                repo: 'https://github.com/myorg/myrepo'

           sqa_criteria:
             qc-style:
               repos:
                 myrepo:
                   container: myrepo-testing
                   tox:
                     tox_file: /myrepo-testing/tox.ini
                     testenv: stylecheck

    .. tab:: docker-compose.yml

       .. code-block:: yaml

          version: "3.6"

          services:
            myrepo-testing:
              image: "indigodatacloud/ci-images:python3.6"
              hostname: "myrepo-testing-host"
              volumes:
               - type: bind
                 source: ./myrepo
                 target: /myrepo-testing

    .. tab:: tox.ini

       .. code-block:: ini

          [tox]
          minversion = 2.1
          envlist = py{36,37},stylecheck
          skipsdist = True

          [testenv]
          usedevelop = True
          basepython = python3

          [testenv:stylecheck]
          envdir = {toxworkdir}/shared
          commands =
            flake8

As it can be seen, the ``config.yml`` file uses definitions from the two other
files, i.e. *services* from the ``docker-compose.yml`` file and 
*tox environments* from the ``tox.ini`` file. The following considerations must
be taken into account:

… ``config.yml`` (CONFIG) and ``docker-compose.yml`` (DC)
    1. Minimum ``version: 3.6`` [DC] is required, otherwise bind
       volume definitions are not correctly supported.
    2. The value for the ``container`` setting [CONFIG] must correspond to a
       service definition in the DC file. In the example above, the service
       *myrepo-testing* is defined under *services* inside DC file.
    3. There are 3 main parameters that must be defined in DC file, i.e.:

       * ``hostname``: sets the hostname of the Docker container. This
         parameter is useful when communicating with other services.
       * ``image``: points to the Docker image that will be used by the 
         container. **When using this parameter, the image must be previously
         available in Docker Hub registry**. Additionally, DC allows the image
         to be built in runtime by providing a Dockerfile. In this case, the 
         ``build`` parameter must be used (check out
         `DC's build parameter documentation <https://docs.docker.com/compose/compose-file/#build>`_).
       * ``volumes``: identifies the volume where the repository (*myrepo* in 
         this example) content will be accessible. **The** ``type: bind`` **is 
         required and only the values for** ``source`` **and** ``target`` 
         **parameters must be provided**.
    4. The ``source`` parameter [DC file] corresponds to the ID/name used to
       identify the current repository, i.e. the ID used in the 
       ``config:project_repos`` definition [CONFIG]. Due to some limitations
       found in the DC file specification, the ``source`` **[DC file] value
       must always be prefixed by** ``./``. In our example, we have set
       *myrepo* as the ID so the correct value for ``source`` [DC file] is 
       *./myrepo*.
    5. The value for ``tox_file`` [CONFIG] must be the absolute path to the
       *tox.ini* file. **To obtain the full path to tox's configuration file,**
       ``target`` **[DC file] must be prepended**, as it is the folder where the
       repository has been checked out. In the example above, *myrepo* has the
       *tox.ini* file available in the root path of the repository, therefore
       */myrepo-testing/tox.ini* is the correct location for the tox's
       configuration file.
    6. The value for ``testenv`` [CONFIG] must correspond to any of the test
       environments defined in the tox's configuration file. In our example,
       *stylecheck* testenv executes the *flake8* style tool, and thus, it can
       be used as the value for tox's ``testenv`` [CONFIG].

Python with ``commands``
^^^^^^^^^^^^^^^^^^^^^^^^

.. tabs::

     .. tab:: config.yml

        .. code-block:: yaml

           config:
             project_repos:
               myrepo:
                 repo: 'https://github.com/myorg/myrepo'

            sqa_criteria:
              qc-coverage:
                repos:
                  myrepo:
                    container: myrepo-testing
                    commands:
                      - flake8

     .. tab:: docker-compose.yml

        .. code-block:: yaml

           version: "3.6"

              services:
                myrepo-testing-java:
                  image: "indigodatacloud/ci-images:java"
                  hostname: "myrepo-testing-host"
                  volumes:
                   - type: bind
                     source: ./myrepo
                     target: /myrepo-testing

Java with ``commands``
^^^^^^^^^^^^^^^^^^^^^^

.. tabs::

     .. tab:: config.yml

        .. code-block:: yaml

           config:
             project_repos:
               myrepo:
                 repo: 'https://github.com/myorg/myrepo'

            sqa_criteria:
              qc-coverage:
                repos:
                  myrepo:
                    container: myrepo-testing-java
                    commands:
                      - mvn checkstyle:checkstyle

     .. tab:: docker-compose.yml

        .. code-block:: yaml

           version: "3.6"

              services:
                myrepo-testing-java:
                  image: "indigodatacloud/ci-images:java"
                  hostname: "myrepo-testing-host"
                  volumes:
                   - type: bind
                     source: ./myrepo
                     target: /myrepo-testing
