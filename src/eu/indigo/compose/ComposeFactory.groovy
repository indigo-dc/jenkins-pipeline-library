package eu.indigo.compose

/**
 * Definitions for Docker Compose integration in Jenkins
 * @see: https://docs.docker.com/compose/compose-file/
 */
class ComposeFactory implements Serializable {

    private static final long serialVersionUID = 0L

    def static factory
    def static tox
    def static processStages(projectConfig) { return factory.processStages(projectConfig) }

    static class ComposeFactoryBuilder {
        def factory
        def tox

        def setFactory(factory) {
            this.factory = factory
            return this
        }

        def setTox(tox) {
            this.tox = tox
            return this
        }

        ComposeFactory build() {
            new ComposeFactory(factory: this.factory,
                               tox: this.tox)
        }

    }

}
