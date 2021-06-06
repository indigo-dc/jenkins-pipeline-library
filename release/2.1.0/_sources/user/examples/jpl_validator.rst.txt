jpl-validator
=============

The production repository can be found in
`GitHub <https://github.com/eosc-synergy/jpl-validator>`_.

Project layout
--------------

::

    (project)
    |-- .sqa
    |    |-- config.yml
    |    |-- docker-compose.yml
    |-- Jenkinsfile
    |-- build.gradle 
    |-- gradlew
    |-- .. 


.. tabs::

    .. tab:: .sqa/config.yml

        .. code-block::
        
           config:
             project_repos:
               jpl-validator:
                 repo: 'https://github.com/eosc-synergy/jpl-validator'
                 branch: master
             credentials:
                 - type: username_password
                   id: orviz-dockerhub
           
           sqa_criteria:
             qc_doc:
               repos:
                 jpl-validator:
                   container: gradle
                   commands:
                       - /jpl-validator/gradlew -p /jpl-validator jib

        **Notes**:
         * The credential *orviz-dockerhub* must be present, as
           *username_password* type, in the Jenkins instance where the pipeline
           will run.
         * Since gradlew builder is used in `commands`, the project directory
           must be set to the path where this script is available. Otherwise,
           the *jib* task will not be found.

    .. tab:: .sqa/docker-compose.yml

        .. code-block::
        
            version: "3.6"
            
            services:
              gradle:
                image: "gradle"
                hostname: "jpl-validator"
                volumes:
                  - type: bind
                    source: ./jpl-validator
                    target: /jpl-validator
                command: sleep infinity

        **Notes**:
          * The *gradle* official image from Docker Hub is one-shot so in order
            to stay available in background, we need to add a *sleep infinity*
            command.

    .. tab:: Jenkinsfile

        .. code-block::

           @Library(['github.com/indigo-dc/jenkins-pipeline-library@2.1.0']) _
           
           def projectConfig
           
           pipeline {
               agent any
           
               stages {
                   stage('SQA baseline dynamic stages') {
                       steps {
                           script {
                               projectConfig = pipelineConfig('./.sqa/config.yml', null, null, 'eoscsynergy/jpl-validator:jib-with-jpl')
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
        **Notes**:
          * Credential support has been available since 2.1.0

    .. tab:: build.gradle

        .. code-block::
		   
           (..)
           mainClassName = 'eu.indigo.jplvalidator.Cli'
           version = 'jib-with-jpl'
           project.ext.organization = 'eoscsynergy'
           
           // Google's jib task
           jib.container.mainClass = mainClassName
           jib.to.image = "${project.ext.organization}/${project.name}:${version}"
           jib.to.auth.username = "${System.env.JPL_USERNAME}"
           jib.to.auth.password = "${System.env.JPL_PASSWORD}" 
           (..)

        **Notes**:
          * An excerpt of ``build.gradle`` file where the username
            (*JPL_USERNAME*) and password (*JPL_PASSWORD*) from the defined
            credentials in ``config.yml`` (*orviz-dockerhub*) are used.
