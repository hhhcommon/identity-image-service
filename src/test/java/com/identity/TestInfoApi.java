package com.identity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.SignatureException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils; 

import com.identity.service.GenerateString;

public class TestInfoApi {

	public static final String GET_URL = "https://v2-auth-api.visioncloudapi.com/info/api";
	
    public static void httpGet() {
        HttpClient httpClient = new DefaultHttpClient();
        String getURL = GET_URL;
        String url = getURL;
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", GenerateString.getSignStr());//请将AUTHORIZATION替换为根据API_KEY和API_SECRET得到的签名认证串
            try{
                HttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(entity.getContent()));
                    String line = reader.readLine();
                    System.out.println(line);
                }else{
                    HttpEntity r_entity = response.getEntity();
                    String responseString = EntityUtils.toString(r_entity);
                    System.out.println("错误码是："+response.getStatusLine().getStatusCode()+"  "+response.getStatusLine().getReasonPhrase());
                    System.out.println("出错原因是："+responseString);
                    //你需要根据出错的原因判断错误信息，并修改
                }
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    public static void main(String[] args) {
        try {
            httpGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
