package com.identity.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
public class IdentityImageService {
	private static Logger logger = LoggerFactory.getLogger(IdentityImageService.class);
	private static String url = "https://v2-auth-api.visioncloudapi.com/identity/liveness_image_verification";
    private static String api_key = "b1905ccfb1f5461699f73400729a04c3";
    private static String api_secret = "59a7945147d342fb916ca855afffa72e";
	
	public ResponseEntity<?> verifyImage(
							MultipartFile first_image_file,
							MultipartFile second_image_file) {
        String authorizationToken = "";
		logger.info("url: " + "'" + url + "'");
		//////
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = null;
		try {
			sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
					.build();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		@SuppressWarnings("deprecation")
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, // for you this is builder.build()
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);

		MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
		try {
			byte[] bytesFront = first_image_file.getBytes();
			File file1 = File.createTempFile("~" + first_image_file.getOriginalFilename().replace(".", "-") + "-", ".tmp");
			Files.write(Paths.get(file1.getPath()), bytesFront);
			multipartMap.add("first_image_file", new FileSystemResource(file1));

			byte[] bytesBack = second_image_file.getBytes();
			File file2 = File.createTempFile("~" + second_image_file.getOriginalFilename().replace(".", "-") + "-", ".tmp");
			Files.write(Paths.get(file2.getPath()), bytesBack);
			multipartMap.add("second_image_file", new FileSystemResource(file2));

		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("Created multipart request: " + multipartMap);

        try {
			authorizationToken = GenerateString.genHeaderParam(api_key, api_secret);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.set("Content-Type", "multipart/form-data");
		headers.add("Authorization", authorizationToken);

		HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
		logger.info("Posting request to: " + url);

		ResponseEntity<?> httpResponse = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
		if (!httpResponse.getStatusCode().equals(HttpStatus.OK)) {
			logger.error("Problems with the request. Http status: " + httpResponse.getStatusCode());
		}
		logger.info(httpResponse.getBody().toString());
		return new ResponseEntity(httpResponse.getBody(), new HttpHeaders(), HttpStatus.OK);
	}

	public ResponseEntity<?> verifyImage(String liveness_id, String image_id) {
        String authorizationToken = "";
		logger.info("url: " + "'" + url + "'");
		//////
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = null;
		try {
			sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
					.build();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		@SuppressWarnings("deprecation")
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, // for you this is builder.build()
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		;

		MultiValueMap<String, String> contents = new LinkedMultiValueMap<String, String>();
		contents.add("liveness_id", liveness_id);
		contents.add("image_id", image_id);

		logger.info("Created content-pairs request: " + contents);

        try {
			authorizationToken = GenerateString.genHeaderParam(api_key, api_secret);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", authorizationToken);

		HttpEntity<Object> request = new HttpEntity<Object>(contents, headers);
		logger.info("Posting request to: " + url);

		ResponseEntity<?> httpResponse = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
		if (!httpResponse.getStatusCode().equals(HttpStatus.OK)) {
			logger.error("Problems with the request. Http status: " + httpResponse.getStatusCode());
		}
		logger.info(httpResponse.getBody().toString());
		return new ResponseEntity(httpResponse.getBody(), new HttpHeaders(), HttpStatus.OK);
	}
	
	public ResponseEntity<?> uploadFile(MultipartFile data) {
		String authorizationToken = "";
		logger.info("url: " + "'" + url + "'");
		//////
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = null;
		try {
			sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
					.build();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		@SuppressWarnings("deprecation")
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, // for you this is builder.build()
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);

		MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
		try {
			byte[] bytesFront = data.getBytes();
			File file1 = File.createTempFile("~" + data.getOriginalFilename().replace(".", "-") + "-",
					".tmp");
			Files.write(Paths.get(file1.getPath()), bytesFront);
			multipartMap.add("data", new FileSystemResource(file1));

		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("Created multipart request: " + multipartMap);

		try {
			authorizationToken = GenerateString.genHeaderParam(api_key, api_secret);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.set("Content-Type", "multipart/form-data");
		headers.add("Authorization", authorizationToken);

		HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
		logger.info("Posting request to: " + url);

		ResponseEntity<?> httpResponse = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
		if (!httpResponse.getStatusCode().equals(HttpStatus.OK)) {
			logger.error("Problems with the request. Http status: " + httpResponse.getStatusCode());
		}
		logger.info(httpResponse.getBody().toString());
		return new ResponseEntity(httpResponse.getBody(), new HttpHeaders(), HttpStatus.OK);
	}
}
