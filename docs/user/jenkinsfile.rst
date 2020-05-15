The pipeline: Jenkinsfile
=========================

The minimal content for the ``Jenkinsfile`` is as follows:

.. code-block:: 

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

The pipeline explained
----------------------

The pipeline loads the jenkins-pipeline-library in the first place:

.. code-block::

   @Library(['github.com:indigo-dc/jenkins-pipeline-library@2.0.0']) _

and only requires the definition of a **single stage** that will be in charge
of:

1. Parsing the ``.sqa/config.yml`` file:

.. code-block::
             
   projectConfig = pipelineConfig()

2. Build one stage per criteria found in ``sqa_criteria`` setting in the 
   ``.sqa/config.yml`` file:

.. code-block::
             
   buildStages(projectConfig)

.. note::
   The library expects the presence of the configuration file in 
   ``.sqa/config.yml``. This behaviour is not configurable.
