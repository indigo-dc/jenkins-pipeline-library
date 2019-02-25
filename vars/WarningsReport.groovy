#!/usr/bin/groovy

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
