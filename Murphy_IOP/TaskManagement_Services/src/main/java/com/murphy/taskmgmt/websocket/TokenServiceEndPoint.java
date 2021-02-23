package com.murphy.taskmgmt.websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@ServerEndpoint(value = "/refreshAccessToken/{userMessage}",encoders = TokenEncoder.class)
public class TokenServiceEndPoint {	
	private Session session;
	private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
	private static final Logger logger = LoggerFactory.getLogger(TokenServiceEndPoint.class);
	
	@OnOpen
	public void onOpen(Session session,@PathParam("userMessage") String userMessage) {
		try {
			this.session = session;
			peers.add(session);
			if(!ServicesUtil.isEmpty(userMessage) && userMessage.equalsIgnoreCase("firstToken")){
				callRest();
			}
		} catch (Exception e) {
			logger.error("[TokenServiceEndPoint][onOpen][error]" + e.getMessage());
		}
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		try {
			broadcast(message);
		} catch (Exception e) {
			logger.error("[TokenServiceEndPoint][onMessage][error]" + e.getMessage());
		}
	}

	@OnClose
	public void onClose(Session session) {
		try {
			peers.remove(session);
			String message = "Client Server Connection Disconnected!";
			broadcast(message);

		} catch (Exception e) {
			logger.error("[TokenServiceEndPoint][onClose][error]" + e.getMessage());
		}

	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.error("Client Connection Failed With Error" + throwable.getMessage());
	}

	private void broadcast(String message) {
		try {
			for (Session peer : peers) {
				if (peer.isOpen()) {
						peer.getBasicRemote().sendObject(message);
				}
			}

		} catch (Exception e) {
			logger.error("Exception While Broadcasting : "+ e.getMessage());
		}
	}
	
	public void callRest(){
		try{
			JSONObject rootJson = new JSONObject();
			rootJson.put("client_id",MurphyConstant.client_id);
			rootJson.put("client_secret",MurphyConstant.client_secret);
			rootJson.put("refresh_token",MurphyConstant.refresh_token);
			rootJson.put("grant_type","refresh_token");
			
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			JSONObject jsonObject = RestUtil.callRest("https://oauth2.googleapis.com/token", rootJson.toString(), "POST",
					null,null);
			System.err.println("inside try " + jsonObject);
			logger.error("inside try " + jsonObject);
			
			// Fetch access token
			JsonNode node;
			if (!ServicesUtil.isEmpty(jsonObject) && jsonObject.toString().contains("access_token")) {
				JSONParser parser = new JSONParser();
				org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(jsonObject.toString());
				node = new ObjectMapper().readTree(json.toString());
				String access_token = node.get("access_token").toString();
				logger.error("access_token " + access_token);
				
				// push access token
				onMessage(session, access_token);
				
			}
		}
		catch(Exception e){
			logger.error("[Murphy][TokenServiceEndPoint][callRest][error]" + e.getMessage());
			
		}
		
	}

}
