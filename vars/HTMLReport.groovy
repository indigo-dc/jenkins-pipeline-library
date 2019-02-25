#!/usr/bin/groovy

def call(String dir, String index, String title) {
    publishHTML([allowMissing: false,
                 alwaysLinkToLastBuild: false,
                 keepAll: true,
                 reportDir: dir,
                 reportFiles: index,
                 reportName: title,
                 reportTitles: ''])
}
