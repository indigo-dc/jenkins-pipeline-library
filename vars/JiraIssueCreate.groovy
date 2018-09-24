#!/usr/bin/groovy

def call(site_id, project_id, summary, description, label, issue_type='Task') {
	def testIssue = [fields: [ 
		// id or key must present for project.
		project: [id: project_id],
		summary: summary,
		description: description,
		//customfield_1000: 'customValue',
		// id or name must present for issueType.
		issuetype: [name: issue_type],
		labels: [label]]]

	response = jiraNewIssue issue: testIssue, site: site_id

	echo response.successful.toString()
	echo response.data.toString()
}
