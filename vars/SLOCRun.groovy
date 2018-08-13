#!/usr/bin/groovy
def call() {
    sh "cloc --by-file --xml --out=cloc.xml ."
}
