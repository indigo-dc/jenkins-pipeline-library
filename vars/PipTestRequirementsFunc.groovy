#!/usr/bin/groovy
def call(content) {
  	writeFile file: 'test-requirements.txt', text: content
}
