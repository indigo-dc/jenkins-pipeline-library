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

    sqa_criteria:
      QC.Sty:
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

node_agent
``````````

The type of agent to drive the execution of the dynamic stages. Docker Compose
is used by default.

:Type: ``string``
:Options: ``docker_compose``
:Default: ``docker_compose``
:Location: ``config:node_agent``

Example:

.. code-block:: yaml

    config:
      node_agent: 'docker_compose'

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

sqa_criteria
~~~~~~~~~~~~

This setting allows to define the criteria from the SQA baseline to be checked.
Each requirement has a unique identifier and an associated set of mandatory and
optional attributes.

:Type: ``map``
:Parameters: ``QC.Sty``, ``QC.Fun``, ``QC.Uni``, ``QC.Sec``, ``QC.Doc``
:Required: ``true``

Example:

.. code-block:: yaml

   sqa_criteria:
     QC.Sty:
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

The settings described in this section are common to all the criteria currently
supported, which are applicable and defined per-repository, and thus, they must
be used within the ``repos`` map setting, as showed in the following examples.

.. note::
   The repositories used under ``repos`` must be previously defined in the
   ``config:project_repos`` setting. They are referred by the identifiers
   used there.

*Examples:*
    .. tabs::

        .. tab:: QC.Sty

           .. code-block:: yaml

              sqa_criteria:
                QC.Sty:
                  repos:
                    worsica-processing:
                      container: processing
                      tox:
                        testenv:
                            - stylecheck

        .. tab:: QC.Uni

           .. code-block:: yaml

              sqa_criteria:
                QC.Uni:
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

        .. tab:: QC.Fun

           .. code-block:: yaml

              sqa_criteria:
                QC.Fun:
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

        .. tab:: QC.Sec

           .. code-block:: yaml

              sqa_criteria:
                QC.Sec:
                  repos:
                    worsica-processing:
                      container: processing
                      tox:
                        testenv:
                            - security

        .. tab:: QC.Doc

           .. code-block:: yaml

              sqa_criteria:
                QC.Doc:
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
take place. If using ``docker_compose``, the value could be any of the services
defined in the docker-compose.yml.

:Type: ``string``
:Required: ``true``
:Location: ``sqa_criteria:<QC.xxx>:repos:<repo>:container``

tox
```

Built-in support tox application. It is only applicable for Python
environments.

:Type: ``map``
:Parameters: ``testenv``, ``tox_file``
:Location: ``sqa_criteria:<QC.xxx>:repos:<repo>:tox``

**testenv**

Identifier of the test environment that tox shall use.

:Type: ``list``
:Required: ``true``
:Location: ``sqa_criteria:<QC.xxx>:repos:<repo>:tox:testenv``

**tox_file**

Specifies the path to the tox configuration file.

:Type: ``path``
:Default: ``tox.ini``
:Location: ``sqa_criteria:<QC.xxx>:repos:<repo>:tox:tox_file``

.. note::
   If using ``tox`` without ``container``, the jenkins-pipeline-library will
   automatically select an appropriate Docker container for running the tool.

commands
````````

Allows to include a list of commands. This is helpful whenever there is no
built-in support for the tool you use for building purposes.

:Type: ``list``
:Default: ``[]``
:Location: ``sqa_criteria:<QC.xxx>:repos:<repo>:commands``

Example:

.. code-block:: yaml

   sqa_criteria:
     QC.Sec:
       repos:
        worsica-processing:
          commands:
            - bundle exec brakeman --exit-on-error

.. note::
   ``commands`` requires the presence of the ``container`` setting, which must
   have available all the tools --and dependencies-- used by the list of
   commands. Also the commands runs relative to the root directory /. As a
   hacking solution is possible to use Docker Compose's
   :ref:`docker_compose_env` to define the expected workspace in
   docker-compose.yml context, as a solution for current release.

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

.. note::
   ``environment`` variables are only usable by the deployment (for example
   with docker_compose) or defined features in current version. This environment
   will not be available inside the containers. For that, you should use for
   example, docker-compose.yml environment definitions instead.

.. note::
   The following JPL-prefixed environment variables have a special purpose:

   +----------------------+---------------------------------------------------------------------------+
   | JPL vars             | Purpose                                                                   |
   +======================+===========================================================================+
   | JPL_DOCKERPUSH       | Space-separated list of defined docker-compose services whose image will  |
   |                      | be pushed to the Docker registry. If ``ALL`` value is used, it            |
   |                      | will push all locally built images defined in docker-compose.yml          |
   +----------------------+---------------------------------------------------------------------------+
   | JPL_IGNOREFAILURES   | If set, by using any random string value (without spaces), it             |
   |                      | will ignore any push-related failure                                      |
   +----------------------+---------------------------------------------------------------------------+
   | JPL_DOCKERFORCEBUILD | Forcedly rebuild all images with build clause in                          |
   |                      | docker-compose.yml                                                        |
   +----------------------+---------------------------------------------------------------------------+
   | JPL_DOCKERSERVER     | Sets Docker registry server. By default it will use Docker Hub            |
   +----------------------+---------------------------------------------------------------------------+
   | JPL_DOCKERUSER       | Sets username of Docker registry credentials                              |
   +----------------------+---------------------------------------------------------------------------+
   | JPL_DOCKERPASS       | Sets password of Docker registry credentials                              |
   +----------------------+---------------------------------------------------------------------------+

timeout
~~~~~~~
Sets the timeout for the pipeline execution.

:Type: ``integer``
:Default: ``600``

Example:

.. code-block:: yaml

   timeout: 60

Docker Registry: upload images
------------------------------
As mentioned in special purpose environment variables note, pushing images to
docker registry is supported using the following environment variables:

+----------------------+--------------------------------------------------------------------------+
| JPL vars             | Purpose                                                                  |
+======================+==========================================================================+
| JPL_DOCKERPUSH       | Space-separated list of defined docker-compose services whose image will |
|                      | be pushed to the Docker registry. If ``ALL`` value is used, it           |
|                      | will push all locally built images defined in docker-compose.yml         |
+----------------------+--------------------------------------------------------------------------+
| JPL_IGNOREFAILURES   | If set, by using any random string value (without spaces), it            |
|                      | will ignore any push-related failure                                     |
+----------------------+--------------------------------------------------------------------------+
| JPL_DOCKERSERVER     | Sets Docker registry server. By default it will use Docker Hub           |
+----------------------+--------------------------------------------------------------------------+
| JPL_DOCKERUSER       | Sets username of Docker registry credentials                             |
+----------------------+--------------------------------------------------------------------------+
| JPL_DOCKERPASS       | Sets password of Docker registry credentials                             |
+----------------------+--------------------------------------------------------------------------+

.. note::
  Images are defined in docker-compose.yml file and there is no relation of those with defined service names.
  Also the docker registry repository needs to be previously created before running the last step of the generated pipeline. Last step will be always the image push to docker registry.
  In next examples the sqa_criteria property is being omitted to focus only in the required configurations to push images to a docker registry. Also project_repos in config section is being removed since is not mandatory, so it turns the examples more clear.
  Jenkins environment variable ${GIT_BRANCH} receives the branch or tag from git repository.

Example1: upload specific images to dockerhub registry ignoring failures

config.yml example with minimal required configurations:

.. code-block:: yaml

   config:
     credentials:
       - id: my-dockerhub-token
         username_var: JPL_DOCKERUSER
         password_var: JPL_DOCKERPASS

   environment:
     JPL_DOCKERPUSH: "docs service1 service4"
     JPL_IGNOREFAILURES: "defined"

In this example there are three services:

- service1: main service that have is Dockerfile in the service1 directory inside git repository.
- service2: same as service1 with Dockerfile inside directory service2 and depends on service1 to be built.
- docs: service to generate the project documentation.

The docker-compose.yml file that would work with previous configuration can be as the following:

.. code-block:: yaml

   version: "3.7"

   services:
     service1:
        build:
           context: "."
           dockerfile: "./service1/Dockerfile"
        image: "organization/service1:${GIT_BRANCH}"

     service2:
        build:
           context: "."
           dockerfile: "./service2/Dockerfile"
           cache_from:
              - "organization/service1:${GIT_BRANCH}"
        image: "organization/service2:${GIT_BRANCH}"
        depends_on:
           - service1

     docs:
        build:
           context: "."
           dockerfile: "./docs/Dockerfile"
        image: "organization/docs:${GIT_BRANCH}"

Example2: upload all images to independent registry and fail with push failures

.. code-block:: yaml

   config:
     credentials:
       - id: my-dockerhub-token
         username_var: JPL_DOCKERUSER
         password_var: JPL_DOCKERPASS

   environment:
     JPL_DOCKERPUSH: "ALL"
     JPL_DOCKERSERVER: "mydockerregistry.example.com:8080"

.. note::
   When using custom docker registry is also expected that docker-compose.yml
   have the expected configuration for the image references, following the official
   `documentation <https://docs.docker.com/compose/compose-file/#image>`_.

.. warning::
   The docker-compose.yml file for this example could be any. With 'ALL' value it will upload all loaded images to the custom registry. This also includes all images pulled from Dockerhub or other docker registry without a build section defined in docker-compose.yml.
