#!/usr/bin/groovy

pipeline {
    agent {
        dockerfile {
            filename 'Dockerfile.build'
        }
    }

    stages {
        stage('Fetch repository') {
            steps {
                checkout scm
            }
        }

        stage('Generate (lastest) groovydoc and publish to Github Pages') {
            when {
                anyOf {
                    branch 'master'
                }
            }
            steps {
                sh('groovydoc -d docs vars/*.groovy')
                withCredentials([string(credentialsId: "indigobot-github-token",
                                 variable: "GITHUB_TOKEN")]) {
                    sh('gh-pages-multi deploy')
                }
            }
        }

        stage('Generate (tagged) groovydoc and publish to Github Pages') {
            when {
                anyOf {
                    buildingTag()
                }
            }
            steps {
                sh("groovydoc -d ${env.GIT_BRANCH} vars/*.groovy")
                withCredentials([string(credentialsId: "indigobot-github-token",
                                 variable: "GITHUB_TOKEN")]) {
                    sh("gh-pages-multi deploy -s ${env.GIT_BRANCH} -t ${env.GIT_BRANCH}")
                }
            }
        }
    }
}
