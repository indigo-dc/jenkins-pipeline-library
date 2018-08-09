def call(dir, index, title) {
    publishHTML([allowMissing: false,
                 alwaysLinkToLastBuild: false,
                 keepAll: true,
                 reportDir: dir,
                 reportFiles: index,
                 reportName: title,
                 reportTitles: ''])
}
