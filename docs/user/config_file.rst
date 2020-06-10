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
              testenv:
                - stylecheck
    
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
:Location: ``config:node_agent``

Example:

.. code-block:: yaml

    config:
      node_agent: 'docker-compose'

.. _config-deploy_template-setting:

deploy_template
```````````````

Path to the template containing the definition of the services for the 
``node_agent`` in use.

:Type: ``path``
:Default: ``.sqa/docker-compose.yml``
:Location: ``config:deploy_template``

Example:

.. code-block:: yaml

    config:
      deploy_template: '.sqa/docker-compose.yml'

project_repos
`````````````

Describes the code repositories that the pipeline will deal with.

:Type: ``map``
:Required: ``true``
:Location: ``config:project_repos``

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
:Location: ``config:project_repos:repo``

**branch**

Branch name to be checked out.

:Type: ``string``
:Default: ``master``
:Location: ``config:project_repos:branch``

**dockerhub**

Repository name within the Docker Hub registry where the Docker images
produced by the pipeline will be pushed.

:Type: ``string``
:Required: ``true``
:Location: ``config:project_repos:dockerhub``

**dockertag**

Tag name to be used for labeling the resultant Docker image.

:Type: ``string``
:Default: ``latest``
:Location: ``config:project_repos:dockertag``

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
             testenv:
                - stylecheck

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

The previous table lists the set of criteria that is currently supported by 
the current version of the jenkins-pipeline-library. The settings described in
this section are common to all, which are applicable and defined 
per-repository, and thus, they must be used within the ``repos`` map setting, 
as showed in the following examples.

.. note:
   The repositories used under ``repos`` must be previously defined in the 
   ``config:project_repos`` setting. They are referred by the identifiers
   used there.

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
                        testenv:
                            - stylecheck

        .. tab:: qc-coverage

           .. code-block:: yaml

              sqa_criteria:
                qc-coverage:
                  repos:
                    worsica-processing:
                      container: processing
                      tox:
                        testenv:
                            - coverage
                    worsica-portal:
                      container: celery
                      tox:
                        testenv:
                            - coverage

        .. tab:: qc-functional

           .. code-block:: yaml

              sqa_criteria:
                qc-functional:
                  repos:
                    worsica-processing:
                      container: processing
                      tox:
                        testenv:
                            - unittest
                    worsica-portal:
                      container: celery
                      tox:
                        testenv:
                            - functional

        .. tab:: qc-security

           .. code-block:: yaml

              sqa_criteria:
                qc-security:
                  repos:
                    worsica-processing:
                      container: processing
                      tox:
                        testenv:
                            - security

        .. tab:: qc-doc

           .. code-block:: yaml

              sqa_criteria:
                qc-doc:
                  repos:
                    worsica-cicd:
                      container: processing
                      commands:
                        - python setup.py build_sphinx


Next, we will describe those available settings, some of them used in the 
previous examples, that can be defined for each repository associated with the
former criteria:

.. _config-container-setting:

container
`````````

Allows to specify the Docker container where the given criteria assessment will
take place. It using docker-compose, the value could be any of the services 
defined in the docker-compose.yml.

:Type: ``string``
:Location: ``sqa_criteria:<qc-xxx>:repos:<repo>:container``

tox
```

Built-in support tox application. It is only applicable for Python
environments.

:Type: ``map``
:Parameters: ``testenv``, ``tox_file``
:Location: ``sqa_criteria:<qc-xxx>:repos:<repo>:tox``

**testenv**

Identifier of the test environment that tox shall use.

:Type: ``list``
:Required: ``true``
:Location: ``sqa_criteria:<qc-xxx>:repos:<repo>:tox:testenv``

**tox_file**

Specifies the path to the tox configuration file.

:Type: ``path``
:Default: ``tox.ini``
:Location: ``sqa_criteria:<qc-xxx>:repos:<repo>:tox:tox_file``

.. note:
   If using ``tox`` withouth ``container``, the jenkins-pipeline-library will
   automatically select an appropriate Docker container for running the tool.

commands
````````

Allows to include a list of commands. This is helpful whenever there is no 
built-in support for the tool you use for building purposes.

:Type: ``list``
:Default: ``[]``
:Location: ``sqa_criteria:<qc-xxx>:repos:<repo>:commands``

Example:

.. code-block:: yaml

   sqa_criteria:
     qc-sec:
       repos:
        worsica-processing:
          commands:
            - bundle exec brakeman --exit-on-error

.. note:
   ``commands`` requires the presence of the ``container`` setting, which must
   have available all the tools --and dependencies-- used by the list of 
   commands.

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
