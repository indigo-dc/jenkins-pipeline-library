#!/usr/bin/groovy
def call() {
    sloccountPublish encoding: '', pattern: '**/cloc.xml'
}
