# Contributions

Please note the following aspects when sending us your contribution.

## Code contributions
 * We use the [Git Flow](https://nvie.com/posts/a-successful-git-branching-model/)
   convention:
    * The `release/<version>` branch contains the last features being added.
    * Releases will be done periodically on `stable/<version>` branch.
    * Production ready releases are defined by tag `<version>` and will be created from `stable/<version>` branch after real use case testing are enough.
    * The `master` branch contains the freezed code from latest tag `<version>`.
    * We expect that your contributions:
      * Are done through pull requests.
      * Use the convention `feature/<branch>` name for new features.
      * Use the convention `fix/<branch>` name for bug fixes.
      * Use the convention `docs/<branch>` name for documentation.
 * The CI code is only present in `jenkins/<prefix>/<version>` branches & maintained in [`JePL`](https://github.com/indigo-dc/jenkins-pipeline-library/tree/JePL) branch:
    * `<prefix> ::= <release> | <stable>  | <feature> | <fix> | <docs>`:
      * `<release> ::= "release"` in case of a branch created from `release/<version>`
      * `<stable> ::= "stable"` in case of a branch created from `stable/<version>`, which is created when a corresponding GitHub Milestone is closed
         * Milestone name MUST match the `<version>`
      * `<feature> | <fix> | <docs>` are generated from the associated branches. The `docs/<version>` branches only trigger the JePL docs re-generation stage.
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
