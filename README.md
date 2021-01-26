# A library to implement Software Quality Assurance (SQA) checks in Jenkins environments

## Motivation
The v2 series of the present library provide a straightforward way for software projects
to be compliant with common SQA practices.

In particular, the library provides the functionality to cover as much criteria as
possible from the Software and Service quality baselines below:

 * [A set of Common Software Quality Assurance Baseline Criteria for Research Projects](https://github.com/indigo-dc/sqa-baseline/), [online](https://indigo-dc.github.io/sqa-baseline/) available.
 * [A set of Common Service Quality Assurance Baseline Criteria for Research Projects](https://github.com/eosc-synergy/service-qa-baseline/), [online](https://eosc-synergy.github.io/service-qa-baseline/) available.


## Short intro
The SQA requirements are defined through a YAML-based configuration file that hides the
complexity of handling directly the Jenkins [Pipeline as Code's](https://www.jenkins.io/solutions/pipeline/)
features. As a result, the SQA work, as defined in the YAML file `config.yml`, is
organized according to the criteria code names from the aforementioned baselines.

As an example, ``QC.Sty`` criterion establishes the good practices that the
source code shall follow in terms of style. The following example runs
[flake8](https://pypi.org/project/flake8/) to check style consistency in
a Python project:

```
sqa_criteria:
  QC.Sty:
    container: python-test-tools
    commands:
      - flake8
```

As it can be seen in the example, the library provides the ``container`` option as a
way to specify the agent where the check will run. In the current version, the
library supports [Docker Compose](https://docs.docker.com/compose/) as the container
orchestration tool to fire up the required set of services. In this example, the
``python-test-tools`` is the identifier of a Docker Compose service.

_You can check a full working example [here](https://github.com/EOSC-synergy/DEEPaaS)_

__In order to get started with the library, please check our
[documentation](https://indigo-dc.github.io/jenkins-pipeline-library/release/2.1.0/).__



## Contributing

Jenkins CI/CD pipelines are often similar in the type of actions that are
triggered. It then makes sense to have a common library for tackling them,
covering different programming languages and interfaces to software lifecycle
management tools.

Contributions are really welcome. Please check our
[contribution](CONTRIBUTING.md) guidelines.

## License

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

indigo-dc/jenkins-shared-library is licensed under [Apache 2.0](LICENSE)

## Acknowledgments

The development of the v2 series is taking place under the [EOSC-Synergy's](https://eosc-synergy.eu)
project that has received funding from the European Union’s Horizon 2020 research
and innovation programme under grant agreement number 857647.
<p align="center">
  <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1WF4g5KH3PnQE_Ve10QFRS-gZ0NpCQ7Qr-_km1RqnOCEF1fQt">
</p>
