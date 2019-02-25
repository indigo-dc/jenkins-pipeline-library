#!/usr/bin/groovy

def call(String subject, String body, String to_address) {
	emailext body: "${body}",
			 recipientProviders: [culprits(), requestor(), developers()],
			 subject: "${subject}",
			 to: "${to_address}"
}
