#!/usr/bin/groovy

import java.net.URLEncoder

/**
 * Creates a new ticket in RT.
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
/*
def encode() {
def content = [
//    "id: ticket/new",
//    "Queue: sw-rel",
//    "Subject: dummy-test",
//    "CF.{ReleaseType}: Minor",
//    "CF.{ReleaseMetadata}: /tmp/dummy_metadata.xml"].join('\n')
//
//    "id: 15165",
//    "Action: comment",
//    "Text: text text",
//    "Attachment: dummy-file",
//    "attachment_1: /tmp/dummy-file.txt",
    ].join('\n')
def content_utf8 = URLEncoder.encode(content, "UTF-8")
println(content_utf8)
}

encode()
*/
