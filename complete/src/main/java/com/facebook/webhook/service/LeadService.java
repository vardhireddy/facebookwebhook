package com.facebook.webhook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.Lead;

@Component
public class LeadService {
	private final String access_token = "juotyeuiwt";
	private static final Logger log = LoggerFactory.getLogger(LeadService.class);

	public void getLeadData(String Id) {
		APIContext context = new APIContext(access_token).enableDebug(true);
		try {
			Lead lead = new Lead(Id, context).get().execute();
			log.info("getLeadData:" + lead.toString());
		} catch (APIException e) {
			log.error(e.getMessage());
		}
	}
}
