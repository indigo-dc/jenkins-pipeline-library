#!/usr/bin/groovy

def call(List packages, String filename) {
    package_str = packages.join('\n')

    if (fileExists(filename)) {
        def readContent = readFile filename
        writeFile file: filename, text: readContent+'\n'+package_str
    }
    else {
  	    writeFile file: filename, text: package_str
    }
}
