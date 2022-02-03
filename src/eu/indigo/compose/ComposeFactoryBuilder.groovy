package eu.indigo.compose

/**
 * Definitions for Docker Compose integration in Jenkins
 * @see: https://docs.docker.com/compose/compose-file/
 */
class ComposeFactoryBuilder implements Serializable {

    private static final long serialVersionUID = 0L

    def factory
    def tox
    def maven

    def setFactory(factory) {
        this.factory = factory
        return this
    }

    def setTox(tox) {
        this.tox = tox
        return this
    }

    def setMaven(maven) {
        this.maven = maven
        return this
    }

    ComposeFactory build() {
        new ComposeFactory(factory: this.factory,
                           tox: this.tox,
                           maven: this.maven)
    }

}
