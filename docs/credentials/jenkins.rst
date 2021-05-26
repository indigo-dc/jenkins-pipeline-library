Jenkins: storing secrets
========================

Adding new global credentials
-----------------------------

To add new global credentials to your Jenkins instance:

1. If required, ensure you are logged in to Jenkins (as a user with the ``Credentials > Create permission``).

2. From the Jenkins home page (i.e. the Dashboard of the Jenkins classic UI), click ``Credentials > System`` on the left.

3. Under System, click the ``Global credentials (unrestricted)`` link to access this default domain.

4. Click ``Add Credentials`` on the left.

.. note:: If there are no credentials in this default domain, you could also click the add some credentials link (which is the same as clicking the ``Add Credentials`` link).

5. From the *Kind* field, choose the type of credentials to add.

6. From the *Scope* field, choose either:

    - *Global* - if the credential/s to be added is/are for a Pipeline project/item. Choosing this option applies the scope of the credential/s to the Pipeline project/item "object" and all its descendent objects.

    - *System* - if the credential/s to be added is/are for the Jenkins instance itself to interact with system administration functions, such as email authentication, agent connection, etc. Choosing this option applies the scope of the credential/s to a single object only.

7. Add the credentials themselves into the appropriate fields for your chosen credential type:

    - *Secret text* - copy the secret text and paste it into the Secret field.

    - *Username and password* - specify the credential’s Username and Password in their respective fields.

    - *Secret file* - click the Choose file button next to the File field to select the secret file to upload to Jenkins.

    - *SSH Username with private key* - specify the credentials Username, Private Key and optional Passphrase into their respective fields.

    .. note:: Choosing Enter directly allows you to copy the private key’s text and paste it into the resulting Key text box.

    - *Certificate* - specify the Certificate and optional Password. Choosing Upload PKCS#12 certificate allows you to upload the certificate as a file via the resulting Upload certificate button.

8. In the *ID* field, specify a meaningful credential ID value - for example, jenkins-user-for-xyz-artifact-repository. You can use upper- or lower-case letters for the credential ID, as well as any valid separator character. However, for the benefit of all users on Jenkins instance, it is best to use a single and consistent convention for specifying credential IDs.

.. note:: This field is optional. If you do not specify its value, Jenkins assigns a globally unique ID (GUID) value for the credential ID. Bear in mind that once a credential ID is set, it can no longer be changed.

9. Specify an optional *Description* for the credential/s.

10. Click *OK* to save the credentials.
