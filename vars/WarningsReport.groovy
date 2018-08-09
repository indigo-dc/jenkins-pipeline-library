#!/usr/bin/groovy
def call(format) {
    warnings canComputeNew: false,
             canResolveRelativePaths: false,
             categoriesPattern: '',
             consoleParsers: [[parserName: 'Pep8']],
             defaultEncoding: '',
             excludePattern: '',
             healthy: '',
             includePattern: '',
             messagesPattern: '',
             unHealthy: ''
}
