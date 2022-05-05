/*   A simple LDAP client with authentication and encrypted communication.

    Debug Logging:
                -Djavax.net.debug=ssl:certpath,provider,handshake

    Use the Windows CertStore for TLS certificates:
                -Djavax.net.ssl.trustStoreType=WINDOWS-ROOT
*/

package com.apisgroup.LdapTestClient;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.naming.Context;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.StartTlsResponse;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import java.io.*;
import java.util.Date;
import java.util.Hashtable;

public class LdapTestClient {
    public static void main(String[] args) throws IOException, ParseException {

        // Read settings from JSON file named "ldap.settings"
        JSONParser jsonP = new JSONParser();
        JSONObject jsonO = (JSONObject) jsonP.parse(new FileReader("ldap.settings"));

        String gUseTLS = (String) jsonO.get("starttls");
        String gHost = (String) jsonO.get("host");
        String gPort = (String) jsonO.get("port");
        String gRootDN = (String) jsonO.get("rootdn");
        String gSearchDir = (String) jsonO.get("searchdir");
        String gUserClass = (String) jsonO.get("userclass");
        String gUserName = (String) jsonO.get("username");
        String gPassword = (String) jsonO.get("password");

		StartTlsResponse tls=null;
		InitialLdapContext ldapContext;
		String url;

        System.out.println("run: " + new Date());
        if (gUseTLS.equals("true")) {
             url = "ldap:" + gHost + ":" + gPort + gRootDN;
        } else {
             url = "ldaps:" + gHost + ":" + gPort + gRootDN;
        }

        System.out.println("LDAP URL       :"+url);
        System.out.println("LDAP search dir:"+gSearchDir);
        System.out.println("LDAP userclass :"+gUserClass);

        ldapContext = getLdapContext(url,gUserName,gPassword);
        if (ldapContext != null) {
            try {
                if (gUseTLS.equals("true")) {
                    tls = (StartTlsResponse) ldapContext.extendedOperation(new StartTlsRequest());
                    tls.negotiate();
                }

                SearchControls searchControls = getSearchControls();
                getUserInfo( ldapContext , searchControls, gSearchDir, gUserClass);
                if (gUseTLS.equals("true")) {
                    tls.close();
                }
                ldapContext.close();
                System.out.println("done: " + new Date());
            } catch (IOException|NamingException ex) {  // IOException|
                System.out.println("LDAP Connection: FAILED");
                ex.printStackTrace();
            }
		}
	}

    private static InitialLdapContext getLdapContext(String url, String principal, String password) {
        InitialLdapContext ctx = null;

        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "Simple");
            env.put(Context.SECURITY_PRINCIPAL, principal );//input user & password for access to ldap
            env.put(Context.SECURITY_CREDENTIALS, password);
			env.put(Context.SECURITY_PROTOCOL, "SSL");
			env.put(Context.PROVIDER_URL, url);
            env.put(Context.REFERRAL, "follow");
            ctx = new InitialLdapContext(env, null);
            System.out.println("LDAP Connection: COMPLETE");
        } catch (NamingException nex) {
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace();
        }
        return ctx;
    }

    private static void getUserInfo( InitialDirContext ctx, SearchControls searchControls, String searchDir, String userClass) {
        try {
            NamingEnumeration<SearchResult> answer = ctx.search(searchDir,userClass,searchControls);
            while (answer.hasMore()) {
                Attributes attrs = answer.next().getAttributes();
                System.out.println(attrs.get("sn"));
            } 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static SearchControls getSearchControls() {
        SearchControls cons = new SearchControls();
        cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String[] attrIDs = {"distinguishedName", "sn", "givenname", "mail", "telephonenumber"};
        cons.setReturningAttributes(attrIDs);
        return cons;
    }
}