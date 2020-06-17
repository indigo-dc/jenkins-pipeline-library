The ``sqa-criteria`` setting
============================
In this section we will cover the ``sqa-criteria`` setting, which represents
the fundamental part of the configuration since it contains the definitions of
the checks that comprise the quality criteria.

The full set of criteria currently supported in the library is summarized in
the following table and can be found in the :ref:`sqa_criteria` section.

+-----------------------------+------------------------------------------------------------------------+
| ``sqa-criteria`` setting    | What does it cover?                                                    |
+=============================+========================================================================+
| ``qc-style``                | Make your code compliant with a style standard                         |
+-----------------------------+------------------------------------------------------------------------+
| ``qc-coverage``             | Calculate the unit testing coverage of your code                       |
+-----------------------------+------------------------------------------------------------------------+
| ``qc-functional``           | Test the main features of your software                                |
+-----------------------------+------------------------------------------------------------------------+
| ``qc-security``             | Assess the security (uncover vulnerabilities & bad security practices) |
+-----------------------------+------------------------------------------------------------------------+
| ``qc-doc``                  | Generate the documentation                                             |
+-----------------------------+------------------------------------------------------------------------+

Python and Java examples
------------------------
The best way to learn the basics about the library is through examples. In the
next subsections we will show how to fulfill the ``qc-style`` criterion for 
Python and Java applications. Working configurations are provided so you can
readily test them.

The current version of the library supports both ``commands`` and ``tox``
(this last only available for Python-based applications) environments to
execute the checks, hence the examples below cover the three possible use cases,
i.e. Python (with and without Tox) and Java (with commands).

Python with ``tox``
^^^^^^^^^^^^^^^^^^^

.. tabs::

    .. tab:: config.yml

       .. code-block:: yaml

          config:
            project_repos:
              myrepo:
                repo: 'https://github.com/myorg/myrepo'

           sqa_criteria:
             qc-style:
               repos:
                 myrepo:
                   container: myrepo-testing
                   tox:
                     tox_file: /myrepo-testing/tox.ini
                     testenv: stylecheck

    .. tab:: docker-compose.yml

       .. code-block:: yaml

          version: "3.6"

          services:
            myrepo-testing:
              image: "indigodatacloud/ci-images:python3.6"
              hostname: "myrepo-testing-host"
              volumes:
               - type: bind
                 source: ./myrepo
                 target: /myrepo-testing

    .. tab:: tox.ini

       .. code-block:: ini

          [tox]
          minversion = 2.1
          envlist = py{36,37},stylecheck
          skipsdist = True

          [testenv]
          usedevelop = True
          basepython = python3

          [testenv:stylecheck]
          envdir = {toxworkdir}/shared
          commands =
            flake8

As it can be seen, the three files are linked together. In order to compose
them, the following requirements must be considered:

Notes on ``docker-compose.yml`` (DC)
    1. Minimum ``version: 3.6`` [DC] is required, otherwise bind
       volume definitions are not correctly supported.
    2. There are 3 main parameters that must be defined in DC file, i.e.:

       * ``hostname``: sets the hostname of the Docker container. This
         parameter is useful when communicating with other services (Docker
         containers).
       * ``image``: points to the Docker image that will be used by the
         container.

         * When using this parameter, **the image must be previously
           available in Docker Hub registry**. Additionally, DC allows the
           image to be built in runtime by providing a Dockerfile. In this
           case, the ``build`` parameter must be used (check out
           `DC's build parameter documentation <https://docs.docker.com/compose/compose-file/#build>`_).
         * Note that **all the tools required to run the tests must be
           deployed in the Docker image**. In this example, the
           *indigodatacloud/ci-images:python3.6* image already contains the
           tools needed to execute the subsequent tox commands.
       * ``volumes``: identifies the volume where the repository (*myrepo* in
         this example) content will be accessible. **The** ``type: bind`` **is
         required and only the values for** ``source`` **and** ``target``
         **parameters must be provided**.

Notes on links between ``config.yml`` (CONFIG) and ``docker-compose.yml`` (DC)
    1. The value for the ``container`` setting [CONFIG] must correspond to a
       service definition in the DC file. In the example above, the service
       *myrepo-testing* is defined under *services* inside DC file.
    2. The ``source`` parameter [DC file] corresponds to the ID/name used to
       identify the current repository, i.e. the ID used in the
       ``config:project_repos`` definition [CONFIG]. Due to some limitations
       found in the DC file specification, the ``source`` **[DC file] value
       must always be prefixed by** ``./``. In our example, we have set
       *myrepo* as the ID so the correct value for ``source`` [DC file] is
       *./myrepo*.

Notes on links between ``tox.ini`` (TOX), ``config.yml`` (CONFIG) and ``docker-compose.yml`` (DC)
    1. The value for ``tox_file`` [CONFIG] must be the absolute path to the
       TOX file. **To obtain the full path to the TOX file,** ``target``
       **[DC file] must be prepended to the relative path of the TOX file 
       within the code repository**, as it is the folder where the
       repository has been checked out. In the example above, *myrepo* has the
       TOX file available in the root path of the repository, therefore
       */myrepo-testing/tox.ini* is the correct location.
    2. The value for ``testenv`` [CONFIG] must correspond to any of the test
       environments [TOX file]. In our example, *stylecheck* testenv executes
       the *flake8* style tool, and thus, it can be used as the value for
       tox's ``testenv`` [CONFIG].

.. tip::
   We recommend the use of `Tox tool <https://tox.readthedocs.io/en/latest/>`_
   in the case of Python applications, as it is the most accurate way of
   defining and running all your tests. Hence, Tox can execute each test in an
   individual Python virtual environment (virtualenv), so it is isolated from
   the other tests. Note that the use of Tox in this example is extremelly
   simple and does not take advantage of the full capabilities of the tool.

Python with ``commands``
^^^^^^^^^^^^^^^^^^^^^^^^

.. tabs::

     .. tab:: config.yml

        .. code-block:: yaml

           config:
             project_repos:
               myrepo:
                 repo: 'https://github.com/myorg/myrepo'

            sqa_criteria:
              qc-coverage:
                repos:
                  myrepo:
                    container: myrepo-testing
                    commands:
                      - flake8

     .. tab:: docker-compose.yml

        .. code-block:: yaml

           version: "3.6"

              services:
                myrepo-testing-java:
                  image: "indigodatacloud/ci-images:java"
                  hostname: "myrepo-testing-host"
                  volumes:
                   - type: bind
                     source: ./myrepo
                     target: /myrepo-testing

In this example, the only difference with respect to the previuos example is
the use of ``commands`` [CONFIG]. Here, we will obtain the same output as in
the previous Python-with-tox example since *flake8* tool is executed.

Java with ``commands``
^^^^^^^^^^^^^^^^^^^^^^

.. tabs::

     .. tab:: config.yml

        .. code-block:: yaml

           config:
             project_repos:
               myrepo:
                 repo: 'https://github.com/myorg/myrepo'

            sqa_criteria:
              qc-coverage:
                repos:
                  myrepo:
                    container: myrepo-testing-java
                    commands:
                      - mvn checkstyle:checkstyle

     .. tab:: docker-compose.yml

        .. code-block:: yaml

           version: "3.6"

              services:
                myrepo-testing-java:
                  image: "indigodatacloud/ci-images:java"
                  hostname: "myrepo-testing-host"
                  volumes:
                   - type: bind
                     source: ./myrepo
                     target: /myrepo-testing


Don't forget to commit
----------------------
Once you have added one of the former definitions in the ``sqa-criteria``
setting, it is time to commit our work. Following up with the example of
previous section:

.. code:: bash

    $ git commit -m "Add sqa-criteria setting & associated docker-compose services"

In the next section, we will provide the last steps to make all this work being
executed in Jenkins.
