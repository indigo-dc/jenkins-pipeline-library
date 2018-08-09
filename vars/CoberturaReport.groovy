def call(report='**/coverage_unit.xml') {
    cobertura autoUpdateHealth: false,
              autoUpdateStability: false,
              coberturaReportFile: report,
              conditionalCoverageTargets: '70, 0, 0',
              failUnhealthy: false,
              failUnstable: false,
              lineCoverageTargets: '80, 0, 0',
              maxNumberOfBuilds: 0,
              methodCoverageTargets: '80, 0, 0',
              onlyStable: false,
              sourceEncoding: 'ASCII',
              zoomCoverageChart: false
}
