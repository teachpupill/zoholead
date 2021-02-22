package com.webbasket.zoholead.web.controllers;

import com.zoho.crm.library.setup.restclient.ZCRMRestClient;
import com.zoho.oauth.client.ZohoOAuthClient;
import com.zoho.oauth.contract.ZohoOAuthTokens;
import com.zoho.oauth.clientapp.ZohoOAuthFilePersistence;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Map;

@Controller
public class Lead {
    HashMap<String, String> zcrmConfigurations = new HashMap < >();
    public  Lead() {


        zcrmConfigurations.put("minLogLevel", "ALL");
        zcrmConfigurations.put("currentUserEmail", "user1@demo2.thinkwp.io");
        zcrmConfigurations.put("client_id", "1000.O7MJ9UO0M2C2BHZS78NPV82IJK0Q5Z");
        zcrmConfigurations.put("client_secret", "b6e8c28799780a013539dde5c7a59a27389c449cdb");
        zcrmConfigurations.put("redirect_uri", "http://localhost:8080/api/home/");
        zcrmConfigurations.put("persistence_handler_class", "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence");//for database. "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence" for file, user can implement his own persistence and provide the path here
     //   zcrmConfigurations.put("oauth_tokens_file_path", "oauthtokens.properties");//optional
        zcrmConfigurations.put("domainSuffix", "");//optional. Default is com. "cn" and "eu" supported
     zcrmConfigurations.put("Scopes","ZohoCRM.modules.ALL,ZohoCRM.settings.ALL,ZohoCRM.users.ALL,ZohoCRM.org.ALL,aaaserver.profile.ALL,ZohoCRM.settings.functions.all,ZohoCRM.notifications.all,ZohoCRM.coql.read,ZohoCRM.files.create,ZohoCRM.bulk.all");

        zcrmConfigurations.put("response_type","code");


        zcrmConfigurations.put("photoUrl", "https://profile.zoho.com/api/v1/user/self/photo");
        zcrmConfigurations.put("apiVersion","v2");
        zcrmConfigurations.put("accessType","Production");//Production->www(default), Development->developer, Sandbox->sandbox(optional)
        zcrmConfigurations.put("access_type","offline");//optional
        zcrmConfigurations.put("apiBaseUrl","https://www.zohoapis.com");//optional
        zcrmConfigurations.put("iamURL","https://accounts.zoho.com");//optional
        zcrmConfigurations.put("logFilePath","log-file.log");//optional


    }

    @GetMapping("/api/lead")
    @ResponseBody
    public String createLead(@RequestParam Map<String,String> allParams) {
        if(allParams.size()<14){
            return "Failed";
        }

        return "Parameters are " + allParams.entrySet();
    }


    @GetMapping("/api/home")
    @ResponseBody
    public  String index(@RequestParam(defaultValue = "") String code ,String location, String accounts_server, HttpServletResponse httpResponse){
        try
        {
            //code = Grant Token
            if (code.isEmpty())
            {

                String ParentURL;
                ParentURL = "https://accounts.zoho.com/oauth/v2/auth?scope=" + zcrmConfigurations.get("Scopes") + "&prompt=consent&client_id=" + zcrmConfigurations.get("client_id") + "&response_type=code&access_type=" + zcrmConfigurations.get("access_type") + "&redirect_uri=" + zcrmConfigurations.get("redirect_uri");

                httpResponse.sendRedirect(ParentURL);
            }
            else
            {
                String accesstoken =GenerateZohoToken(code);
                System.out.println("Access Tokrn"+accesstoken);

                return accesstoken;
            }
        }
        catch (Exception Ex)
        {
            Ex.printStackTrace();
        }
        return "";
    }

    private String GenerateZohoToken(String code) throws Exception {
        Path newFilePath = Paths.get("c:\\oauthtokens.properties");
        try {

            if(!Files.exists(newFilePath)){
                Files.createFile(newFilePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        zcrmConfigurations.put("oauth_tokens_file_path", newFilePath.toFile().getAbsolutePath());//optional
        ZCRMRestClient.initialize(zcrmConfigurations);//for initializing

        ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
        String grantToken = code;
       // String accessToken= cli.getAccessToken(zcrmConfigurations.get("currentUserEmail"));

        ZohoOAuthTokens tokens = cli.generateAccessToken(code);
        String accessToken = tokens.getAccessToken();
        String refreshToken = tokens.getRefreshToken();
        System.out.println("access token = " + accessToken + " & refresh token = " + refreshToken);
        return accessToken;
    }


}

