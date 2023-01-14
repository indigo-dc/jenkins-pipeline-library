The services: docker-compose.yml
================================

Since the jenkins-pipeline-library relies on Docker Compose framework, all the
services that will be required by the pipeline have to be previously defined.
The Docker Compose file is expected in the jenkins-pipeline-library's
configuration folder, in particular in ``.sqa/docker-compose.yml``.

.. note::
   If you opt for using a different location other than the default one for the
   Docker Compose definition, you need to use the
   :ref:`config-deploy_template-setting` setting.

The `official documentation <https://docs.docker.com/compose/>`_ is the most
appropriate place for getting familiar with the syntax. The next excerpt
showcases the definition of a single service in the version 3 of the Docker
Compose specification:

.. code-block::

   version: "3.6"

   services:
     processing:
       image: "worsica/worsica-backend:worsica-processing-dev_latest"
       container_name: "processing"
       volumes:
        - type: bind
          source: ./worsica_web_products
          target: /usr/local/worsica_web_products

The document uses a YAML format and starts by setting the version used. The
next section, ``services``, is the most relevant for the interests of the
jenkins-pipeline-library. Here, all the required services needed for tackling
the ``sqa_criteria`` requirements are defined.

.. note::
   The identifier used in the service definition --``processing`` in the
   example above-- will be then used in the ``config.yml`` as part of the
   :ref:`config-container-setting` setting. However, **the use of**
   ``container_name`` (see example above) **is highly recommended to set the
   name of the container that will be used later on in the** ``config.yml``. 
   Otherwise, Docker Compose will append a suffix to the service identifier 
   (e.g. ``processing_1``) in  the case of finding, in the same server, an
   existing container matching the same name. This situation is commonplace
   when previous deployments of the same pipeline have failed to be completely
   cleaned up.

.. _docker_compose_env:

Environment variables
---------------------

Environment variables can be set using the `environment
<https://docs.docker.com/compose/environment-variables/>`_ label. It is
possible to bypass variables defined from config.yml environment and set them
afterwards inside docker-compose. For example, based on previous examples:

.. code-block::

   version: "3.6"

   services:
     processing:
       image: "worsica/worsica-backend:worsica-processing-dev_latest"
       container_name: "processing"
       volumes:
        - type: bind
          source: ./worsica_web_products
          target: /usr/local/worsica_web_products
       environment:
        - DEBUG=1
        - GIT_COMMITTER_NAME=${GIT_COMMITTER_NAME}
        - GIT_COMMITTER_EMAIL=${GIT_COMMITTER_EMAIL}
        - LANG: ${LANG}

.. _dc_summary:

Summary of recommendations for best use with the library
--------------------------------------------------------

Use Docker Compose version ``3.6``
  Previous versions are not compatible with the use of bind volumes as expected
  by the library.

Use ``container_name`` to set the container ID
  It is currently the best choice provided by Docker Compose for ensuring that
  the required container will be accessible through the expected name. If this
  parameter is omitted, then Docker Compose will try to use the generic ID
  provided for the service definition, i.e. ``services:<service_name>`` (in the
  example above this would correspond to *processing*). The problem in this 
  latter case is that launching the container will not fail if the system is
  already running a service with the same ID, but it will append a prefix to it.
  The result of this behaviour is that the JPL library will not be able to 
  connect to the container as it is not aware of the change in the name. 
  Instead, if the ID set under ``container_name`` is already in use, the
  operation will fail as expected.

Try not to use *one-shot* Docker images
  Bear in mind that the images used for the services are expected to be ran in
  background, so those images configured to execute a specific task and then be
  shut down cannot be used unless we made them explicitly keep running. For 
  instance, this could be accomplished by adding a ``sleep infinity`` in the
  list of commands passed to the container at runtime, such as:

  .. code-block::
  
     version: "3.6"
  
     services:
       processing:
         image: "worsica/worsica-backend:worsica-processing-dev_latest"
         container_name: "processing"
         volumes:
          - type: bind
            source: ./worsica_web_products
            target: /usr/local/worsica_web_products
         command: sleep infinity
  
