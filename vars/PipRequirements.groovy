#!/usr/bin/groovy
def call(content, filename) {
    if (fileExists(filename)) {
        def readContent = readFile filename
        writeFile file: filename, text: readContent+'\n'+content
    }
    else {
  	    writeFile file: filename, text: content
    }
}
