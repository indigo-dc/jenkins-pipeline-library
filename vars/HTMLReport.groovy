#!/usr/bin/groovy

/**
 * Publishes HTML reports.
 *
 * @param dir Path to HTML files [mandatory]
 * @param index Main index file [mandatory]
 * @param title Report title [mandatory]
 */
def call(String dir, String index, String title) {
    publishHTML([allowMissing: false,
                 alwaysLinkToLastBuild: false,
                 keepAll: true,
                 reportDir: dir,
                 reportFiles: index,
                 reportName: title,
                 reportTitles: ''])
}
