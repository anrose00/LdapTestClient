Testing LDAP connections:

Preparation:

- Open a command prompt and enter the following commands:

cd %TMP%
mkdir ldap
cd ldap

- keep the command prompt open
- copy the files "LdapTestClient.jar" and "ldap.settings" to %TMP%\LDAP
- edit the ldap.settings file to match your configuration
- configure the console window to use the Java included with IQ software and enter the following into the command prompt
  ("C:\Program Files (x86)\APIS\APIS IQ-RM PRO 7.0" is the default install directory. If the SW has been installed in a
  different path, this part must be changed accordingly)

set PATH=C:\Program Files (x86)\APIS\APIS IQ-RM PRO 7.0\jre\bin;%PATH
set JAVA_HOME=C:\Program Files (x86)\APIS\APIS IQ-RM PRO 7.0\jre

- check if the java works and enter this command:

java -version

- this command will list the users names found in the configured LDAP directory

java -cp LdapTestClient.jar com.apisgroup.LdapTestClient.LdapTestClient

- if you need special certificates for TLS connection and the certificate is stored in the Windows cert store use this command:


java -Djavax.net.ssl.trustStoreType=WINDOWS-ROOT -cp LdapTestClient.jar com.apisgroup.LdapTestClient.LdapTestClient

- if the command fails, try this command:

java -Djavax.net.debug=ssl:certpath,provider,handshake -cp LdapTestClient.jar com.apisgroup.LdapTestClient.LdapTestClient > ldap.log

- (if you need the Windows cert store the command looks like this)

java -Djavax.net.ssl.trustStoreType=WINDOWS-ROOT -Djavax.net.debug=ssl:certpath,provider,handshake -cp LdapTestClient.jar com.apisgroup.LdapTestClient.LdapTestClient > ldap.log

