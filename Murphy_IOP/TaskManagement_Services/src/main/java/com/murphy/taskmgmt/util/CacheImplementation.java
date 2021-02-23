package com.murphy.taskmgmt.util;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.taskmgmt.dto.TokenDetailsDto;

public class CacheImplementation {

	private static final Logger logger = LoggerFactory.getLogger(CacheImplementation.class);
	private CacheAccess<String, TokenDetailsDto> cache = null;

	public CacheImplementation() {
		try {
			cache = JCS.getInstance("default");
		} catch (CacheException e) {
			logger.error("[CacheImplementation] : ERROR- Problem initializing cache: ", e.getMessage());
		}
	}

	public void putInCache(TokenDetailsDto token, String key) {
//		String key = "pbiTokenCacheKey";// generate a key

		try {
			cache.put(key, token);
		} catch (CacheException e) {
			logger.error("[CacheImplementation] : Problem putting data to cache ", e.getMessage());
		}
	}

	public TokenDetailsDto retrieveFromCache(String key) {
		return cache.get(key);
	}

}
