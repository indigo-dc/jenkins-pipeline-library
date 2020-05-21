#!/usr/bin/groovy


/**
 * Checks the license of a GitHub repository.
 *
 * @param owner The owner of the repository [mandatory]
 * @param repository [mandatory]
 */
def call(String owner, String repository) {   
    def gh = new eu.indigo.sqa.GitHub()
    boolean citation = gh.isPathInRepository(owner,repository,"CITATION.json")
    println "CITATION.json present? ${citation}"
    boolean codemeta = gh.isPathInRepository(owner,repository,"codemeta.json")
    println "codemeta.json present? ${citation}"
}
