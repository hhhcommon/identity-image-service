package com.identity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.identity.service.GenerateString;

public class TestValidityIdnumberVerification {
	 public static final String POST_URL = "https://v2-auth-api.visioncloudapi.com/validity/idnumber_verification";
     public static final String username = "范冰冰";//姓名
     public static final String id_number = "370602198109162149";//身份证号

     public static void HttpClientPost() throws ClientProtocolException, IOException {
           HttpClient httpclient = new DefaultHttpClient();
           HttpPost post = new HttpPost(POST_URL);
           StringBody name = new StringBody(username,Charset.forName("UTF-8"));
           StringBody number = new StringBody(id_number);
           MultipartEntity entity = new MultipartEntity();

           entity.addPart("name", name);
           entity.addPart("idnumber", number);
           post.setEntity(entity);
           post.setHeader("Authorization", GenerateString.getSignStr());//请将AUTHORIZATION替换为根据API_KEY和API_SECRET得到的签名认证串
           HttpResponse response = httpclient.execute(post);
           if (response.getStatusLine().getStatusCode() == 200) {
               HttpEntity entitys = response.getEntity();
               BufferedReader reader = new BufferedReader(
                   new InputStreamReader(entitys.getContent()));
               String line = reader.readLine();
               System.out.println(line);
           }else{
               HttpEntity r_entity = response.getEntity();
               String responseString = EntityUtils.toString(r_entity);
               System.out.println("错误码是："+response.getStatusLine().getStatusCode()+"  "+response.getStatusLine().getReasonPhrase());
               System.out.println("出错原因是："+responseString);
               //你需要根据出错的原因判断错误信息，并修改
           }

           httpclient.getConnectionManager().shutdown();
     }


   public static void main(String[] args) throws ClientProtocolException, IOException {
       HttpClientPost();
   }
}
