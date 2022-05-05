Testing LDAP connections:

Preparation:

- Open a command prompt and enter the following commands:

cd %TMP%
mkdir ldap
cd ldap

- keep the command prompt open
- copy the files "LdapTestClient.jar" and "ldap.settings" to %TMP%\LDAP
- edit the ldap.settings file to match your configuration
- check if Java works and enter this command:

java -version

- this command will list the users names found in the configured LDAP directory

java -cp LdapTestClient.jar com.apisgroup.LdapTestClient.LdapTestClient

- if you need special certificates for TLS connection and the certificate is stored in the Windows cert store use this command:


java -Djavax.net.ssl.trustStoreType=WINDOWS-ROOT -cp LdapTestClient.jar com.apisgroup.LdapTestClient.LdapTestClient

- if the command fails, try this command:

java -Djavax.net.debug=ssl:certpath,provider,handshake -cp LdapTestClient.jar com.apisgroup.LdapTestClient.LdapTestClient > ldap.log

- (if you need the Windows cert store the command looks like this)

java -Djavax.net.ssl.trustStoreType=WINDOWS-ROOT -Djavax.net.debug=ssl:certpath,provider,handshake -cp LdapTestClient.jar com.apisgroup.LdapTestClient.LdapTestClient > ldap.log

