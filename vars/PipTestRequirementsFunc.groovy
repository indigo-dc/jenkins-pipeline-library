#!/usr/bin/groovy
def call(content) {
  	writeFile file: '/tmp/test-requirements.txt', text: content
}
