#!/usr/bin/groovy

/**
 * Collects multiple types of reports.
 *
 * @param format Report format [mandatory]
 * @see https://plugins.jenkins.io/warnings
 */
def call(String format) {
    warnings canComputeNew: false,
             canResolveRelativePaths: false,
             categoriesPattern: '',
             consoleParsers: [[parserName: format]],
             defaultEncoding: '',
             excludePattern: '',
             healthy: '',
             includePattern: '',
             messagesPattern: '',
             unHealthy: ''
}
