#!/usr/bin/groovy
import groovy.json.JsonSlurper 

    
 def boolean isPathInRepository(String owner,String repository,String path){
      String repository_url = 'https://api.github.com/repos'
      URL url = "${repository_url}/${owner}/${repository}/contents/${path}".toURL()
      def connection = (HttpURLConnection)url.openConnection()
      connection.setRequestMethod("GET")
      connection.connect()
      return connection.getResponseCode() == 200
}

def call(String owner, String repository) {
    boolean citation = isPathInRepository(owner,repository,"CITATION.json")
    println "CITATION.json present? ${citation}"
    boolean codemeta = isPathInRepository(owner,repository,"codemeta.json")
    println "codemeta.json present? ${citation}"
    
}

