package com.service.email.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.service.email.dto.BasicEmailRequestDTO;
import com.service.email.template.EmailProvider;

@RestController
@RequestMapping(value = EmailController.BASE_REQUEST_MAPPING)
public class EmailController {

	protected static final String BASE_REQUEST_MAPPING = "/send-email";

	@RequestMapping(value = "/basic", method = RequestMethod.POST)
	private ResponseEntity<String> sendBasicEmail(@RequestBody final BasicEmailRequestDTO request) {
		try {
			EmailProvider.sendBasicEmail(request.getSender(), "actual-address@email.com", request.getTitle(), request.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/report", method = RequestMethod.POST)
	private ResponseEntity<String> sendReportEmail(@RequestBody final BasicEmailRequestDTO request) {
		try {
			EmailProvider.sendReportEmail(request.getSender(), "actual-address@email.com", request.getTitle(), request.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
