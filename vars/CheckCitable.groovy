#!/usr/bin/groovy
import groovy.json.JsonSlurper 

def boolean isPathInRepository(String owner,String repository,String path)
{
    String GITHUB_API = 'https://api.github.com/repos'
    String url = "${GITHUB_API}/${owner}/${repository}/contents/${path}"
    def code = new URL(url).openConnection().with {
    requestMethod = 'GET'
    connect()
    responseCode
   }
   return code == 200
}

def call(String owner, String repository) {
    boolean citation = isPathInRepository(owner,repository,"CITATION.json")
    println "CITATION.json present? ${citation}"
    boolean codemeta = isPathInRepository(owner,repository,"codemeta.json")
    println "codemeta.json present? ${citation}"
    
}

