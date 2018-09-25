#!/usr/bin/groovy

def call(site_id, project_id, summary, description, label, issue_type, assignee) {
	def testIssue = [fields: [ 
		project: [id: "${project_id}"],
		summary: "${summary}",
		description: "${description}",
		issuetype: [name: "${issue_type}"],
		labels: ["${label}"],
        assignee: [name: "${assignee}"]]]
    
    response = jiraNewIssue issue: testIssue, site: "${site_id}"

	echo response.successful.toString()
	echo response.data.toString()
}
