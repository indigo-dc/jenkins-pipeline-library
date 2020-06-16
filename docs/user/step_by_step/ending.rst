Last steps
==========

After following the previous steps, *we have a working example that validates
the code style of our Python or Java application*. 

Throughout the guide we have learnt that:

 * There are *3 required files* (provided with relative paths to the code
   repository)
   * ``.sqa/config.yml``
   * ``.sqa/docker-compose.yml``
   * ``Jenkinsfile``

   Apart from those, in the specific case of using Tox tool with Python, we
   also need to consider both the definition of the environments and the 
   location of the Tox configuration file.
 * The checks are defined within the ``sqa-criteria`` setting of the
   ``.sqa/config.yml`` file.
 * The services are provided through Docker containers, using Docker Compose
   solution, and must be previously defined in the ``.sqa/docker-compose.yml``
   file.

Now it is time to push our changes to the remote repository:

.. code:: bash

    $ git push origin setup_jenkins-pipeline-library

If we have a Jenkins server already scanning our remote repository, the
previous *push* command will automatically trigger the execution of the
code style check we have defined in our example. No need of doing anything 
else. Once the check has been executed by Jenkins, the results will be
displayed. The ``setup_jenkins-pipeline-library`` branch will be eventually
merged into the production branch (usually ``master``) after the dealing with
the style standard complaints (if any).

.. tip::
   The EOSC-Synergy project provides a Jenkins instance that can be used for 
   research software projects using the jenkins-pipeline-library (v2). If you
   need support, please contact <wp3@eosc-synergy.eu>.
