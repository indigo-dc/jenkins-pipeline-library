#!/usr/bin/groovy

def create_issue(String site,
                 String project_id,
                 String summary,
                 String description,
                 List label,
                 String issue_type,
                 String assignee) {
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

def search_issue(String project_name,
                 List label,
                 String site) {
    label_str += label.join(' and LABELS = ')
	jiraJqlSearch jql: "PROJECT = ${project_name} and LABELS = ${label_str}",
                  site: "${site}",
                  failOnError: true
}

def comment_issue(String site,
                  String key_id,
                  String comment) {
    jiraAddComment site: site, idOrKey: key_id, comment: comment
}

def add_watchers(String site, String key_id, List user_list) {
    if (user_list != null) {
        user_list.each {
            jiraAddWatcher site: site, idOrKey: key_id, userName: it
        }
    }
}

def call(String site,
         String project_name,
         String project_id,
         String summary,
         String description,
         List label,
         String issue_type,
         String assignee,
         List watchers=null) {
    def issues = search_issue(project_name, label, site)
    if (issues.data.issues == []) {
        def issue_id = create_issue(site,
                                    project_id,
                                    summary,
                                    description,
                                    label,
                                    issue_type,
                                    assignee)
        add_watchers(site, issue_id.key, watchers)
    }
    else {
        issues.data.issues.each {
            comment_issue(site, it.key, description)
            add_watchers(site, it.key, watchers)
        }
    }
}
