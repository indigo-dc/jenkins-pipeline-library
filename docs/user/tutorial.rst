.. _tutorial:

Introduction
============

The Software Quality Assurance baseline
---------------------------------------
The SQA criteria collects a comprehensive set of good practices meant to 
improve the quality of the resultant software. The criteria is governed by the
DevOps culture and covers the whole software development life cycle, from the 
code analysis and testing to its final delivery, with a particular focus on 
research software. The latest version of the SQA criteria can be found in 
`<https://indigo-dc.github.io/sqa-baseline>`_.

The jenkins-library-pipeline
----------------------------
The jenkins-pipeline-library implements the aforementioned requirements from 
the SQA baseline in order to simplify the task of developing quality software
in research. To this end, the library relies on the Jenkins' Pipeline-as-Code 
capabilities to dinamically compose customized workflows --aka pipelines or 
``Jenkinsfile``-- that are executed for every change in the source code.

Consequently, the jenkins-pipeline-library hides the complexity of the 
domain-specific language (DSL) syntax used in the Jenkisfiles, providing the 
users with the possibility of describing the relevant information --needed to
analyze, build, deploy, test and deliver the application-- through a YAML-based
file.

.. Leveraged solutions
.. -------------------
.. The jenkins-pipeline-library builds on the approach followed by
.. `wolox-ci <https://github.com/Wolox/wolox-ci>`_ library to achieve the dynamic
.. composition of Jenkinsfiles, and extends it in order to meet the additional 
.. requirements coming from the SQA baseline. In terms of technologies, the 
.. jenkin

Configuration file
==================

Where should be stored?
-----------------------
The jenkins-pipeline-library expects the YAML config file to be placed in a 
particular location within the application's code repository: 
``.sqa/config.yml``. Other important configuration will be placed as well in 
the ``.sqa`` folder as we will see in the following sections.

Main sections
-------------
The YAML configuration file has three primary sections:
 * ``config``, used for defining the generic parameters, such as the workspace
   and execution agents.
 * ``sqa_criteria``, where the criteria to be checked is defined. Each
   requirement has a unique identifier and a set of mandatory and optional
   attributes.
 * ``environment`` contains the environment variables required to execute the
   SQA criteria.

Syntax of the YAML configuration file
-------------------------------------

Validation
==========
