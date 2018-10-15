#!/usr/bin/groovy
def call(subject, body, to_address) {
	emailext body: "${body}",
			 recipientProviders: [culprits(), requestor(), developers()],
			 subject: "${subject}",
			 to: "${to_address}"
}
