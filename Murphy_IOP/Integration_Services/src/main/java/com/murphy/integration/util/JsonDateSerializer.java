package com.murphy.integration.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonDateSerializer extends JsonSerializer<Date> {
	
	private static final Logger logger = LoggerFactory.getLogger(JsonDateSerializer.class);
	
	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		try {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		jgen.writeString(formatter.format(value));
		logger.error(formatter.format(value));
		}catch (Exception e) {
			logger.error("Exception " + e);
			// TODO: handle exception
		}
	}
}
