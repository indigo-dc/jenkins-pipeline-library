#!/usr/bin/groovy

def call(src_path, project="My Project") {
	OWASP_DIR=[env.WORKSPACE, 'OWASP'].join('/')
	OWASP_DATA_DIR=[env.WORKSPACE, 'OWASP', 'data'].join('/')
	OWASP_REPORT_DIR=[env.WORKSPACE, 'OWASP', 'report'].join('/')
	
	sh "mkdir -p $OWASP_DATA_DIR"
	sh "mkdir -p $OWASP_REPORT_DIR"
	sh "chmod -R 777 $OWASP_DIR"
	
	sh '''git clone https://github.com/indigo-dc/im'''
	
	dir("$src_path") {
		timeout(time: 10, unit: "MINUTES") {
			withDockerContainer(image: 'owasp/dependency-check', args: "--entrypoint '' --volume $src_path:/src --volume $OWASP_DATA_DIR:/usr/share/dependency-check/data --volume $OWASP_REPORT_DIR:/report") {
				sh "/usr/share/dependency-check/bin/dependency-check.sh --project 'OWASP Dependency Check for $project' --scan /src --format 'ALL'"
			}
		}
	}
}
