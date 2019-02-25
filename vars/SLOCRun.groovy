#!/usr/bin/groovy

/**
 * Collects SLOC metric.
 *
 */
def call() {
    sh "cloc --by-file --xml --out=cloc.xml ."
}
