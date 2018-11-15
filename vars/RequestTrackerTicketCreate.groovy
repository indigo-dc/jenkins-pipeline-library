#!/usr/bin/groovy

import java.net.URLEncoder

/**
 * Creates a greeting method for a certain person.
 *
 * @param url RT server URL
 * @param creds credential's ID in Jenkins
 * @param queue Queue name in RT
 * @param subject Ticket's subject in RT
 * @return reponse object
 */
def call(url, creds, queue, subject) {
    def content = [
        "id: ticket/new",
        "Queue: ${queue}",
        "Subject: ${subject}",
    ].join('\n')

    def content_utf8 = URLEncoder.encode(content, "UTF-8")
	def response = httpRequest authentication: "${creds}",
                               customHeaders: [[maskValue: false, name: 'Content-type', value: 'text/plain; charset=utf-8']], 
                               httpMode: 'POST',
                               responseHandle: 'NONE',
                               url: "${url}/REST/1.0/ticket/new?content=${content_utf8}",
                               consoleLogResponseBody: true
}
