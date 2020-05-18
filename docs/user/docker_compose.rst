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

   version: 3

   services:
     processing:
       image: "worsica/worsica-backend:worsica-processing-dev_latest"
       container_name: "processing"
       hostname: "processing"
       volumes:
        - type: bind
          source: worsica_web_products
          target: /usr/local/worsica_web_products

The document uses a YAML format and starts by setting the version used. The
next section, ``services``, is the most relevant for the interests of the 
jenkins-pipeline-library. Here, all the required services needed for tackling
the ``sqa-criteria`` requirements are defined. 

.. note::
   The identifier used in the service definition --``processing`` in the
   example above-- will be then used in the ``config.yml`` as part of the
   :ref:`config-container-setting` setting.
