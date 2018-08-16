#!/usr/bin/groovy

call("guachuguei", "")

def call(packages, filename) {
    if (packages instanceof List) {
        package_str = packages.join('\n')
    }
    else { // assume String
        package_str = packages
    }

    if (fileExists(filename)) {
        def readContent = readFile filename
        writeFile file: filename, text: readContent+'\n'+package_str
    }
    else {
  	    writeFile file: filename, text: package_str
    }
}
