#!/usr/bin/groovy

def create_issue(site, project_id, summary, description, label, issue_type, assignee) {
	def testIssue = [fields: [ 
		project: [id: "${project_id}"],
		summary: "${summary}",
		description: "${description}",
		issuetype: [name: "${issue_type}"],
		labels: ["${label}"],
        assignee: [name: "${assignee}"]]]
    
    response = jiraNewIssue issue: testIssue, site: "${site}"

	echo response.successful.toString()
	echo response.data.toString()
}

def search_issue(project_name, label, site) {
	label_str = ''
    if (label instanceof List) {
        label_str += label.join(' and LABELS = ')
    }
    else {
        label_str += label
    }
	jiraJqlSearch jql: "PROJECT = ${project_name} and LABELS = ${label_str}", site: "${site}", failOnError: true
}

def comment_issue(site, key_list, comment) {
    key_list.each {
        jiraAddComment site: site, idOrKey: it, comment: comment
    }
}

def call(site, project_name, project_id, summary, description, label, issue_type, assignee) {
    def issues = search_issue(project_name, label, site)
    if (issues.data.issues == []) {
        create_issue(site, project_id, summary, description, label, issue_type, assignee)
    }
    else {
        key_list = []
        issues.data.issues.each {
            key_list.push(it.key)    
        }
        comment_issue(site, key_list, "New build of tag vX.Y.Z")
    }
}
