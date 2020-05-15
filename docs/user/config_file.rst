The configuration file: config.yml
==================================

jenkins-pipeline-library relies on a YAML file in order to compose dinamically
the stages that tackle the fulfillment of a requirement or good practice as 
defined by the SQA baseline.

The configuration file must exist under ``.sqa/config.yml`` path, relative to the
root directory of your code repository.

Below is an example YAML file which shows the most common configuration options:

.. code:: yaml

    # .sqa/config.yml - jenkins-pipeline-library configuration file
	
    # generic configuration: workspace, agents
    config:
      node_agent: 'docker-compose'
      deploy_template: '.sqa/docker-compose.yml'
      project_repos:
        worsica-processing:
          repo: 'https://github.com/WORSICA/worsica-processing.git'
          branch: master
          dockerhub: worsica/worsica-processing
          dockertag: $branch
    
    sqa-criteria:
      qc-style:
        repos:
          worsica-processing:
            container: processing
            tox:
              testenv: stylecheck
            environment:
              GIT_COMMITTER_NAME: Person2
              GIT_COMMITTER_EMAIL: person2@example.org
              LANG: en_US.UTF-8
    
    environment:
      GIT_COMMITTER_NAME: Person1
      GIT_COMMITTER_EMAIL: person1@example.org
      LANG: C.UTF-8
    
    timeout: 600

Supported settings
------------------

The ``.sqa/config.yml`` file shall contain the following main settings:

.. contents::
   :local:
   :depth: 1

config
~~~~~~

Here you can define the generic parameters, such as the workspace and execution
agents.

:Type: ``map``
:Parameters: ``node_agent``, ``deploy_template``, ``project_repos``
:Required: ``true``

Example:

.. code-block:: yaml

   config:
     project_repos:
       worsica-processing:
         repo: 'https://github.com/WORSICA/worsica-processing.git'
         branch: master
         dockerhub: worsica/worsica-processing
         dockertag: $branch

node_agent
``````````

The type of agent to drive the execution of the dynamic stages. Docker Compose
is used by default.

:Type: ``string``
:Options: ``docker-compose``
:Default: ``docker-compose``

deploy_template
```````````````

Path to the template containing the definition of the services for the 
``node_agent`` in use.

:Type: ``path``
:Default: ``.sqa/docker-compose.yml``

project_repos
`````````````

Describes the code repositories that the pipeline will deal with.

:Type: ``map``
:Required: ``true``

Example:

.. code-block:: yaml

   config:
     project_repos:
       worsica-processing:
         repo: 'https://github.com/WORSICA/worsica-processing.git'
         branch: master
         dockerhub: worsica/worsica-processing
         dockertag: $branch

The set of allowed parameters for the definition of the code repository's
description within the ``project_repos`` setting are herein described:

**repo**

URL pointing to the root path of the code repository.

:Type: ``url``
:Required: ``true``

**branch**

Branch name to be checked out.

:Type: ``string``
:Default: ``master``

**dockerhub**

Repository name within the Docker Hub registry where the Docker images
produced by the pipeline will be pushed.

:Type: ``string``
:Required: ``true``

**dockertag**

Tag name to be used for labeling the resultant Docker image.

:Type: ``string``
:Default: ``latest``

sqa_criteria
~~~~~~~~~~~~

This setting allows to define the criteria from the SQA baseline to be checked.
Each requirement has a unique identifier and an associated set of mandatory and
optional attributes.

:Type: ``map``
:Parameters: ``qc-style``, ``qc-functional``, ``qc-coverage``, ``qc-security``, ``qc-doc`` 
:Required: ``true``

Example:

.. code-block:: yaml

   sqa-criteria:
     qc-style:
       repos:
         worsica-processing:
           container: processing
           tox:
             testenv: stylecheck

.. note::
   The ``sqa_criteria`` setting is the most relevant section of the
   ``.sqa/config.yml`` as it defines the different stages that will be
   dynamically added to the pipeline.

The relationship between the identifiers used in the definition of the
``sqa_criteria`` (see *Options* above) and the ones used in the SQA criteria
is summarized as follows:

+-----------------+-----------------------+
| sqa_criteria ID | SQA baseline category |
+=================+=======================+
| qc-style        | QC.Sty                |
+-----------------+-----------------------+
| qc-coverage     | QC.Uni                |
+-----------------+-----------------------+
| qc-functional   | QC.Fun                |
+-----------------+-----------------------+
| qc-security     | QC.Sec                |
+-----------------+-----------------------+
| qc-doc          | QC.Doc                |
+-----------------+-----------------------+

The current set of criteria supported from the SQA baseline (check *Options*
above) share the same available settings. They all fall into a code repository
categorization, through the use of the ``repos`` map setting.

.. note:
   The repositories used under ``repos`` must be previously defined in the 
   ``config:project_repos`` setting. They are referred by the identifiers
   used there.

Next, we will describe the available settings that can be defined for each 
repository associated with a given criteria:

container
`````````

Allows to specify the Docker container where the given criteria assessment will
take place. It using docker-compose, the value could be any of the services 
defined in the docker-compose.yml.

:Type: ``string``
:Required: ``true``

tox
```

Built-in support tox application. It is only applicable for Python environments.

:Type: ``map``
:Parameters: ``testenv``, ``tox_file``

**testenv**

Identifier of the test environment that tox shall use.

:Type: ``string``
:Required: ``true``

**tox_file**

Specifies the path to the tox configuration file.

:Type: ``path``
:Default: ``tox.ini``

*Examples:*
    .. tabs::

        .. tab:: qc-style

           .. code-block:: yaml
              
              sqa_criteria:
                qc-style:
                  repos:
                    worsica-processing:
                      container: processing
                      tox:
                        testenv: stylecheck

        .. tab:: qc-coverage

           .. code-block:: yaml

              sqa_criteria:
              qc-functional:
                repos:
                  worsica-processing:
                    container: processing
                    tox:
                      testenv: coverage
                  worsica-portal:
                    container: celery
                    tox:
                      testenv: functional

        .. tab:: qc-functional

           .. code-block:: yaml

              sqa_criteria:
                qc-functional:
                  repos:
                    worsica-processing:
                      container: processing
                      tox:
                        testenv: unittest
                    worsica-portal:
                      container: celery
                      tox:
                        testenv: functional

        .. tab:: qc-security

           .. code-block:: yaml

              sqa_criteria:
                qc-security:
                  repos:
                    worsica-processing:
                      container: processing
                      tox:
                        testenv: security

        .. tab:: qc-doc

           .. code-block:: yaml

              sqa_criteria:
                qc-doc:
                  repos:
                    worsica-cicd:
                      container: processing
                      commands:
                        - python setup.py build_sphinx


environment
~~~~~~~~~~~
Contains the environment variables required to execute the previouos SQA 
criteria checks.

:Type: ``list``
:Default: ``[]``

Example:

.. code-block:: yaml

   environment:
     GIT_COMMITTER_NAME: Person1
     GIT_COMMITTER_EMAIL: person1@example.org
     LANG: C.UTF-8

timeout
~~~~~~~
Sets the timeout for the pipeline execution.

:Type: ``integer``
:Default: ``600``

Example:

.. code-block:: yaml

   timeout: 60
