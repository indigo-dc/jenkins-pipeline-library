# A library for Jenkins pipelines that addresses common checks in SQA environments

## Motivation
The library contains common functionalities that are needed for the
implementation of Software Quality Assurance (SQA) requirements in Jenkins
CI/CD pipelines.

## Library content
The library consists in a set of Groovy scripts that interface with popular
tools to carry out agile software developement and CI/CD environment setup.
The Groovy scripts make use of documented Jenkins plugins that need to be
available beforehand in Jenkins service.

 * Automated testing (tox, maven)
 * Reporting (checkstyle, cobertura, PEP8, JUnit)
 * Metrics gathering (sloc)
 * Artifact building (Docker, RPM/DEB)
 * Delivery (Docker Hub)
 * Issue tracking (JIRA, RT)
 * Notifications (e-mails)

## Usage

Just import the library at the beginning of your Jenkinsfile:

```
@Library(['github.com/indigo-dc/jenkins-pipeline-library']) _
```

or use a specific version of the library:
```
@Library(['github.com/indigo-dc/jenkins-pipeline-library@1.0.0']) _
```

__Documentation in groovydoc format is available
[here](https://indigo-dc.github.io/jenkins-pipeline-library/) for each
supported version.__

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

The INDIGO-DataCloud, DEEP-Hybrid-DataCloud and eXtreme-DataCloud projects have
received funding from the European Union’s Horizon 2020 research and innovation
programme under grant agreement number 653549, 777435 and 777367 respectively.
<p align="center">
  <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1WF4g5KH3PnQE_Ve10QFRS-gZ0NpCQ7Qr-_km1RqnOCEF1fQt">
</p>
