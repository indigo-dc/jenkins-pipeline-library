#!/usr/bin/groovy
def call(content, filename='test-requirements.txt') {
  	writeFile file: filename, text: content
}
