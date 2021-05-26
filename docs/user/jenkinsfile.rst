The pipeline: Jenkinsfile
=========================

The minimal content for the ``Jenkinsfile`` is as follows:

.. code-block::

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

The pipeline explained
----------------------

The pipeline loads the jenkins-pipeline-library in the first place:

.. code-block::

   @Library(['github.com/indigo-dc/jenkins-pipeline-library@release/2.1.0']) _

and only requires the definition of a **single stage** that will be in charge
of:

1. Parsing the ``.sqa/config.yml`` file:

.. code-block::

   projectConfig = pipelineConfig()

2. Build one stage per criteria found in ``sqa_criteria`` setting in the
   ``.sqa/config.yml`` file:

.. code-block::

   buildStages(projectConfig)

3. Customize the configurations for the pipeline job (advanced options)

   The library expects by default the presence of the configuration file in
   ``.sqa/config.yml``. Also other parameters that are set by default from triggered job can be overriden. For current version the supported configurations are configFile, baseRepository, baseBranch, credentialsId, validatorDockerImage and scmConfigs. All of this arguments are optional and don't have any dependency.

.. note::
   scmConfigs corresponds to extensions options for Jenkins SCM step. In current version is only supported LocalBranch extension for GitSCM class.

As an example using all available options, the call to pipelineConfig can be changed to the following:

.. code-block::

   projectConfig = pipelineConfig(
     configFile: '<alternative_path>',
     baseRepository: '<git repository url>',
     baseBranch: '<branch or tag name>',
     credentialsId: '<Jenkins credential id>',
     validatorDockerImage: '<jpl-validator docker image>',
     scmConfigs: [ localBranch: '<local branch name>' ]
     )

.. note::
   If <local branch name> value is an empty string or "**", then the branch name is computed from the remote branch without the origin. Example:
   - origin/master will be checked out to local branch named master
   - origin/feature/new the same into local branch named feature/new

   If given a different value from previous ones, it will checkout into a new branch with provided name.
