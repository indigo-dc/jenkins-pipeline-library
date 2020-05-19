#!/usr/bin/groovy

/**
 * Collects CheckStyle report.
 *
 * @param report Report location [default]
 * @see https://plugins.jenkins.io/checkstyle
 */
def call(String report='**/target/checkstyle-result.xml') {
	checkstyle canComputeNew: false,
			   defaultEncoding: '',
			   healthy: '',
        	   pattern: report,
        	   unHealthy: ''
}
