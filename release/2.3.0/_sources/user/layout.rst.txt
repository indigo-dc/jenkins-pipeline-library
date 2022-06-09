Layout
======

A minimal structure in your code repository is required in order to leverage
the jenkins-pipeline-library. In addition to the ``Jenkinsfile``, the specific
configuration for the operation of the library is located in the ``.sqa`` 
folder, as follows:

::

    (project)
    |-- .sqa
    |    |-- config.yml
    |    |-- docker-compose.yml
    |-- Jenkinsfile
    |-- .. 

As shown in the figure above, the following key files are needed:

``.sqa/config.yml``
^^^^^^^^^^^^^^^^^^^

This is the central piece of configuration used by the jenkins-library-pipeline
to compose the new stages that implement the requirements from the SQA baseline.

To start creating your own setup please head over to:

* :doc:`/user/config_file`


``.sqa/docker-compose.yml``
^^^^^^^^^^^^^^^^^^^^^^^^^^^

The library relies by default on Docker Compose tool to facilitate the task of 
spawing the services required to carry out the verification and validation (V\&V)
processes.

Please refer to the `Docker Compose <https://docs.docker.com/compose/>`_ official
documentation to define the services that will be used by the pipeline. 
Additionally, we provide a template for very simple use cases:

* :doc:`/user/docker_compose`

.. note::
   
   docker-compose is currently the supported technology. Additional frameworks 
   that follow the same YAML approach to define the orchestration settings, 
   such as Kubernetes, are clear candidates to be supported in the future. 

``Jenkinsfile``
^^^^^^^^^^^^^^^

The presence of a Jenkinsfile in the code repository indicates Jenkins that the
actual branch must be built according to the stages thereof. The 
jenkins-pipeline-library requires the addition of a specific stage that will
dinamically load the stages resultant from parsing the previous 
``config.yml`` file.

The minimal content of the Jenkinsfile is described in:

* :doc:`/user/jenkinsfile`
