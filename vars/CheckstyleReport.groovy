#!/usr/bin/groovy

def call(String report='**/target/checkstyle-result.xml') {
	checkstyle canComputeNew: false,
			   defaultEncoding: '',
			   healthy: '',
        	   pattern: report,
        	   unHealthy: ''
}
