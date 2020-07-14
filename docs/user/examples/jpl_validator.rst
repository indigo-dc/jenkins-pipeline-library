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

        Notes
        '''''
        
         * The credential *orviz-dockerhub* must be present, as *username_password*
           type, in the Jenkins instance where the pipeline will run.
         * Since gradlew builder is used in `commands`, the project directory must be
           set to the path where this script is available. Otherwise, the *jib* task
           will not be found.

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
