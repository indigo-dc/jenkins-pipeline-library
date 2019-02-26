#!/usr/bin/groovy

/**
 * Returns an array of strings relying on newline character.
 *
 * @param multline_str Multiline string [mandatory]
 */
def multilineToArray(String multiline_str) {
    return multiline_str.split('\n').toList()
}
