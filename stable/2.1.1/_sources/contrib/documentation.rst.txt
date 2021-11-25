Contributing with docs
======================

Build the documentation
-----------------------

Building locally the documentation is the most visual way to check how your
changes are rendered before being committed to the production documentation
repository.

Set up the environment
^^^^^^^^^^^^^^^^^^^^^^
Clone and chdir to the root path of the jenkins-pipeline-library (jpl) code
repository:

.. code-block:: bash

   $ git clone https://github.com/indigo-dc/jenkins-pipeline-library jpl
   $ cd jpl/docs

For the next steps, it is recommended (but not required) to use a Python's
virtual environment:

.. code-block:: bash

   $ python3 -m venv .venv
   $ source .venv/bin/activate

Install the dependencies
^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   $ pip install -r requirements.txt

Run Sphinx to build & access the docs locally
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   $ make html

The docs (in HTML format) are availabe in ``_build/html``, so access them with
your browser through the index file: ``_build/html/index.html``.
