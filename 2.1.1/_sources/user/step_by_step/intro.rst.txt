Intro
=====

This step-by-step guide will provide the easiest way to start using the
jenkins-library-pipeline. In particular, it is addressed for software
developers with any of the following needs:

... you have a Python or Java-based software project
    whose source code is openly accessible (e.g. through GitHub)

... you want to enhance the quality of this software, such as:
    * The *readability of the code* since you start a new research collaboration
      that will imply external contributions to that code, or you have
      received positive feedback about the software and you would like to
      guarantee its long-term sustainability.
    * The *reliability and deterministic behaviour of the software*, as you have
      detected unexpected behaviours when running this software.

... you have read the `SQA baseline <https://indigo-dc.github.io/sqa-baseline>`_, so you know that:
    1. The *readability* is aligned with the good practices in the
       `QC.Sty <https://indigo-dc.github.io/sqa-baseline/#code-style-qc.sty>`_
       category. You have decided that the Python's `PEP8
       <https://www.python.org/dev/peps/pep-0008/>`_ standard is the most
       suitable for your goals.
    2. The *reliability* of the software is improved by considering the practices
       in the
       `QC.Uni <https://indigo-dc.github.io/sqa-baseline/#unit-testing-qc.uni>`_,
       `QC.Fun <https://indigo-dc.github.io/sqa-baseline/#functional-testing-qc.fun>`_
       and
       `QC.Int <https://indigo-dc.github.io/sqa-baseline/#integration-testing-qc.int>`_.

... you want to execute the previous checks in an automated fashion for every change in your source code.
   You heard about the v2 series of the jenkins-pipeline-library (that's why
   you are reading this) and want to start using it.

So, let's see how to set it up.
