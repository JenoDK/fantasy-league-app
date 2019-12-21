package com.jeno.fantasyleague.backend.data.service.email;

import java.io.IOException;
import java.util.Map;

import it.ozimov.springboot.mail.service.TemplateService;
import it.ozimov.springboot.mail.service.exception.TemplateException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationTemplateService implements TemplateService {

	@Override
	public String mergeTemplateIntoString(String templateReference, Map<String, Object> model) throws IOException, TemplateException {
		return getHtmlBody((String) model.get("content"));
	}

	@Override
	public String expectedTemplateExtension() {
		return "ftl";
	}

	private String getHtmlBody(String body) {
		return "<!doctype html>\n" +
				"<html>\n" +
				"\t<body>" +
				body +
				"</body>\n" +
				"</html>";
	}

}
