This branch contains the JePL folder structure required to execute the CI/CD
pipelines for the _stable_ and _release_ branches. Accordingly, these files
are fetched at the time of creating the `jenkins/stable/*` and
`jenkins/release/*` branches.

The `Jenkinsfile` contains a placeholder to load the appropriate version of
the JePL to be used in the `jenkins/*` branches. This value is required for
the pipeline to work, and thus, the execution of the current branch needs
to be disabled in the Jenkins instance. However, if the current branch needs
to be evaluated without using the `jenkins/*` branch, the following steps
must be accomplished:

1. Set the JePL version to be used in the `Jenkinsfile`, such as:
    ```groovy
    @Library(['github.com/indigo-dc/jenkins-pipeline-library@release/2.1.0']) _
    ```

2. Fetch a branch that contains all the required data being used in
`.sqa/config.yml`:
    ```yaml
    config:
      project_repos:
        jepl-release-branch:
          repo: https://github.com/indigo-dc/jenkins-pipeline-library
          branch: release/2.1.0
    ```

By doing this, we will mimic the behaviour of the `jenkins/*` branches that is
being set up by the GitHub workflows existing in the `release/*` branches.
