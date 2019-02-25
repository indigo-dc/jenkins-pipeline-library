#!/usr/bin/groovy

/**
 * Sends e-mails.
 *
 * @param subject e-mail's subject [mandatory]
 * @param body e-mail's body [mandatory]
 * @param to_address e-mail's recipient  [mandatory]
 */
def call(String subject, String body, String to_address) {
	emailext body: "${body}",
			 recipientProviders: [culprits(), requestor(), developers()],
			 subject: "${subject}",
			 to: "${to_address}"
}
