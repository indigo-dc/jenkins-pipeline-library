#!/usr/bin/groovy

/**
 * Collects SLOC metric using cloc.
 *
 */
def call() {
    sh "cloc --by-file --xml --out=cloc.xml ."
}

