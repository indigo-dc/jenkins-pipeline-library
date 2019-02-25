#!/usr/bin/groovy

/**
 * Creates a JIRA issue.
 *
 * @param site Site name for the JIRA backend, as defined in Jenkins [mandatory]
 * @param project_id Project identification [mandatory]
 * @param summary Issue summary [mandatory]
 * @param description Issue description [mandatory]
 * @param label Labels that feature the issue [mandatory]
 * @param issue_type Type of issue [mandatory]
 * @param assignee Person in charge of the issue [mandatory]
 */
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

/**
 * Searches for a JIRA issue.
 *
 * @param site Site name for the JIRA backend, as defined in Jenkins [mandatory]
 * @param label Labels that feature the issue, to filter the search [mandatory]
 * @param project_name Project's name [mandatory]
 */
def search_issue(String site,
                 List label,
                 String project_name) {
    label_str += label.join(' and LABELS = ')
	jiraJqlSearch jql: "PROJECT = ${project_name} and LABELS = ${label_str}",
                  site: "${site}",
                  failOnError: true
}

/**
 * Comment JIRA issue.
 *
 * @param site Site name for the JIRA backend, as defined in Jenkins [mandatory]
 * @param key_id Issue ID [mandatory]
 * @param comment Text to add to the issue [mandatory]
 */
def comment_issue(String site,
                  String key_id,
                  String comment) {
    jiraAddComment site: site, idOrKey: key_id, comment: comment
}

/**
 * Add watchers to an existing JIRA issue.
 *
 * @param site Site name for the JIRA backend, as defined in Jenkins [mandatory]
 * @param key_id Issue ID [mandatory]
 * @param watchers List of users that will be added as watchers for the issue [mandatory]
 */
def add_watchers(String site, String key_id, List watchers) {
    if (watchers != null) {
        watchers.each {
            jiraAddWatcher site: site, idOrKey: key_id, userName: it
        }
    }
}

/**
 * Creates a JIRA issue or updates an existing one.
 *
 * @param site Site name for the JIRA backend, as defined in Jenkins [mandatory]
 * @param project_name Project's name [mandatory]
 * @param project_id Project identification [mandatory]
 * @param summary Issue summary [mandatory]
 * @param description Issue description [mandatory]
 * @param label Labels that feature the issue [mandatory]
 * @param issue_type Type of issue [mandatory]
 * @param assignee Person in charge of the issue [mandatory]
 * @param watchers List of users that will be added as watchers for the issue [default]
 */
def call(String site,
         String project_name,
         String project_id,
         String summary,
         String description,
         List label,
         String issue_type,
         String assignee,
         List watchers=[]) {
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
