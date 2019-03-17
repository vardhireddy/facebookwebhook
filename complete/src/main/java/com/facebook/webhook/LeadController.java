package com.facebook.webhook;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LeadController {

	private static final Logger log = LoggerFactory.getLogger(LeadController.class);

	@SuppressWarnings("unchecked")
	@PostMapping("/api/webhooks")
	public Map<String, String> leadGen(@RequestHeader(value="X-Hub-Signature") String signature, @RequestBody Map<String, Object> subscriber) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		String status = "Failed";
		try {
			log.info(signature);
			String json = mapper.writeValueAsString(subscriber);
			Map<String, Object> input = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});
			log.info(json);
			List<Map<String, Object>> entry = (List<Map<String, Object>>) input.get("entry");
			for (Map<String, Object> x : entry) {
				List<Map<String, Object>> changes = (List<Map<String, Object>>) x.get("changes");
				for (Map<String, Object> y : changes) {
					if (y.get("field") == "leadgen") {
						Map<String, Object> value = (Map<String, Object>) y.get("value");
						String leadgenId = (String) value.get("leadgen_id");
						log.info("leadgen_id:{}", leadgenId);
					}
				}
			}
			status = "Success";

		} catch (IOException e) {
			log.error(e.getMessage());
		}

		map.put("status", status);
		return map;
	}

	@GetMapping("/api/webhooks")
	public String callBck(@RequestParam("hub.mode") String mode, @RequestParam("hub.challenge") String challenge,
			@RequestParam("hub.verify_token") String token) {
		log.info(token);
		log.info(challenge);
		return challenge;

	}
}
