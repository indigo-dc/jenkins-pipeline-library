#!/usr/bin/groovy

/**
 * Runs either Checkstyle or Cobertura maven goals.
 *
 * @param  report Report location [default]
 * @see https://plugins.jenkins.io/cobertura
 */
def call(String goal) {
    options = []
    if (goal.startsWith('checkstyle')) {
        options = [
            '-Dcheckstyle.failOnViolation=true', 
            '-Dcheckstyle.console=true', 
            '-Dcheckstyle.violationSeverity=warning',
            '-Dcheckstyle.config.location=google_checks.xml',
        ]
        goal = 'checkstyle:check'
    }
    else if (goal.startsWith('cobertura')) {
        options = [
            '-Dcobertura.report.format=xml'
        ]
        goal = 'cobertura:cobertura'
    }
    l_cmd = ['mvn ', options.join(' '), goal]
    cmd = l_cmd.join(' ')
    sh(script: cmd)
}
