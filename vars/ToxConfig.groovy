#!/usr/bin/groovy
def call(content, filename='tox.ini') {
  	writeFile file: filename, text: content
}
