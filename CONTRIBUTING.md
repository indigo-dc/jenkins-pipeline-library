# Contributions

Please note the following aspects when sending us your contribution.

## Code contributions
 * We use the [Git Flow](https://nvie.com/posts/a-successful-git-branching-model/)
   convention:
    * The `release/<version>` branch contains the last features being added.
    * Releases will be done periodically on `stable/<version>` branch.
    * Production ready releases are defined by tag `<version>` and will be created from `stable/<version>` branch after real use case testing are enough.
    * The `master` branch contains the freezed code from latest tag `<version>`.
    * The CI code is only present in `jenkins/<prefix>/<version>` branch, where `<prefix> ::= <release> | <stable>  | <tag>`:
      * `<release> ::= "release"` in case of a branch created from `release/<version>`
      * `<stable> ::= "stable"` in case of a branch created from `stable/<version>`
      * `<tag> ::= ""` in case of a branch created from `<version>` tag
 * We expect that your contributions:
    * Are done through pull requests.
    * Use the convention `feature/<branch>` name for new features.
    * Use the convention `bugfix/<branch>` name for bug fixes.
    * Use the convention `test/<branch>` name for testing CI code before merging into `jenkins/<branch>`.
    * Use the convention `docs/<branch>` name for documentation.
 * Compliant with [The Apache Groovy programming language - Style
 guide](http://groovy-lang.org/style-guide.html)
 * Compliant with [Jenkins code style guidelines](https://wiki.jenkins.io/display/JENKINS/Code+Style+Guidelines)

## Issues
 * Before opening a new issue, please double-check first if a mathing
 issue already exists in the list of [open issues](https://github.com/indigo-dc/jenkins-pipeline-library/issues).
 * Use the issues to:
   * Report & Fix bugs
   * Implement new features/functionalities
   * Improve library structure
   * Share ideas
