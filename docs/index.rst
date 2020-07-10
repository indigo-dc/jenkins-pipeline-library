.. jenkins-pipeline-library documentation master file, created by
   sphinx-quickstart on Tue May 12 13:07:24 2020.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

jenkins-pipeline-library: automate the SQA task
===============================================
The jenkins-pipeline-library provides an easy means to add to your Jenkins
code pipelines (aka ``Jenkinsfiles``) additional stages that implement the
criteria defined in the `Software Quality Assurance (SQA) baseline
<https://github.com/indigo-dc/sqa-baseline>`_.

Simplified and customized composition
  The SQA criteria is defined in a YAML configuration file, using specific
  parameters to provide the required data, thus avoiding the need of
  learning the domain-specific language (DSL) syntax used to compose the
  Jenkinsfiles.

Dynamic creation of the pipelines
  Each SQA requirement defined in the configuration file is dinamically
  loaded as a new stage in the resultant pipeline. This process does not
  affect the additional static stages already defined in the pipeline.

What is the SQA baseline?
-------------------------
The SQA criteria collects a comprehensive set of good practices meant to
improve the quality of the resultant software. The criteria is governed by the
DevOps culture and covers the whole software development life cycle, from the
code analysis and testing to its final delivery, with a particular focus on
research software. The latest version of the SQA criteria can be found in
`<https://indigo-dc.github.io/sqa-baseline>`_.

.. toctree::
   :maxdepth: 1
   :hidden:
   :caption: Getting started

   /user/layout

.. toctree::
   :maxdepth: 1
   :hidden:
   :caption: Configuration files

   /user/config_file
   /user/docker_compose
   /user/jenkinsfile

.. toctree::
   :caption: Step-by-step guide

   /user/step_by_step/intro
   /user/step_by_step/min_configuration
   /user/step_by_step/sqa_criteria
   /user/step_by_step/ending

.. toctree::
   :caption: Contribution guide

   /contrib/documentation
