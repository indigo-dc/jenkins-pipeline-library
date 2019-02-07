#!/usr/bin/groovy

def create_issue(site, project_id, summary, description, label, issue_type, assignee) {
    if (!label instanceof List) {
        label = [label]
    }

	def testIssue = [fields: [ 
		project: [id: "${project_id}"],
		summary: "${summary}",
		description: "${description}",
		issuetype: [name: "${issue_type}"],
		labels: label,
        assignee: [name: "${assignee}"],
    ]]
    
    response = jiraNewIssue issue: testIssue, site: "${site}"

	echo response.successful.toString()
	echo response.data.toString()

    return response.data
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

def comment_issue(site, key_id, comment) {
    jiraAddComment site: site, idOrKey: key_id, comment: comment
}

def add_watchers(site, key_id, user_list) {
    if (user_list != null) {
        user_list.each {
            jiraAddWatcher site: site, idOrKey: key_id, userName: it
        }
    }
}

def call(site, project_name, project_id, summary, description, label, issue_type, assignee, watchers=null) {
    def issues = search_issue(project_name, label, site)
    if (issues.data.issues == []) {
        def issue_id = create_issue(site, project_id, summary, description, label, issue_type, assignee)
        add_watchers(site, issue_id.key, watchers)
    }
    else {
        issues.data.issues.each {
            comment_issue(site, it.key, description)
            add_watchers(site, it.key, watchers)
        }
    }
}
