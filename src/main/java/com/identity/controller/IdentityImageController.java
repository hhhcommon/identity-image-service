package com.identity.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.identity.service.IdentityImageService;

@RestController
public class IdentityImageController {
	private static Logger logger = LoggerFactory.getLogger(IdentityImageController.class);

	
	@Autowired
	private IdentityImageService identityImageService;

	@RequestMapping(value = "/api/identity/image/verification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyImage(
							@RequestParam("liveness_id") 	String liveness_id,
							@RequestParam("image_id") 		String image_id) {
		return identityImageService.verifyImage(liveness_id, image_id);
	}

    @RequestMapping("/")
    String home() {
        return "hello";
    }
}
