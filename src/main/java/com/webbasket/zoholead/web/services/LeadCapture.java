package com.webbasket.zoholead.web.services;

import com.webbasket.zoholead.web.controllers.Lead;
import com.zoho.crm.library.setup.restclient.ZCRMRestClient;
import com.zoho.oauth.client.ZohoOAuthClient;
import com.zoho.oauth.contract.ZohoOAuthTokens;

import java.util.HashMap;

public class LeadCapture {
    HashMap<String, String> zcrmConfigurations = new HashMap<String, String>();

    public LeadCapture() {
        zcrmConfigurations.put("minLogLevel", "ALL");
        zcrmConfigurations.put("currentUserEmail", "user1@demo2.thinkwp.io");
        zcrmConfigurations.put("client_id", "1000.O7MJ9UO0M2C2BHZS78NPV82IJK0Q5Z");
        zcrmConfigurations.put("client_secret", "b6e8c28799780a013539dde5c7a59a27389c449cdb");
        zcrmConfigurations.put("redirect_uri", "https://connect.eko.in" );
        zcrmConfigurations.put("persistence_handler_class", "com.zoho.oauth.clientapp.ZohoOAuthDBPersistence ");//for database. "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence" for file, user can implement his own persistence and provide the path here
        zcrmConfigurations.put("oauth_tokens_file_path", "oauthtokens.properties");//optional
        zcrmConfigurations.put("domainSuffix", "com");//optional. Default is com. "cn" and "eu" supported
        zcrmConfigurations.put("accessType", "Production");//Production->www(default), Development->developer, Sandbox->sandbox(optional)

    }


    public  String saveLead() throws Exception {
        ZCRMRestClient.initialize(zcrmConfigurations);//for initializing

        ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
        String grantToken = "1000.5b10698b6f3950853e63f998b80cdb4a.711cbb048c2f48350916952d9c1e2611";
        ZohoOAuthTokens tokens = cli.generateAccessToken(grantToken);
        String accessToken = tokens.getAccessToken();
        String refreshToken = tokens.getRefreshToken();
        System.out.println("access token = " + accessToken + " & refresh token = " + refreshToken);
        return accessToken;
    }

    public static void main(String[] args) {
        try {
            System.out.println(new LeadCapture().saveLead());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}