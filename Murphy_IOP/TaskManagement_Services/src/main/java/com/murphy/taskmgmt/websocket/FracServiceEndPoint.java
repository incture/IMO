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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.murphy.taskmgmt.dao.FracNotificationDao;

@ServerEndpoint(value = "/fracEndPoint/{userName}/{userGroup}", decoders = FracMessageDecoder.class, encoders = FracMessageEncoder.class)
public class FracServiceEndPoint {
	private Session session;
	private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
	private static final Logger logger = LoggerFactory.getLogger(FracNotificationDao.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("userName") String userName, @PathParam("userGroup") String userGroup) {
		try {
			this.session = session;
			session.getUserProperties().put(session.getId(), userName+"#%#"+userGroup);
			peers.add(session);
		} catch (Exception e) {
			logger.error("[FracServerEndPoint][onOpen][error]" + e.getMessage());
		}
	}

	@OnMessage
	public void onMessage(Session session, FracMessage message) {
		try {
			broadcast(message);
		} catch (Exception e) {

			logger.error("[FracServerEndPoint][onMessage][error]" + e.getMessage());
		}
	}

	@OnClose
	public void onClose(Session session) {
		try {
			peers.remove(session);
			FracMessage message = new FracMessage();
			message.setConnectionMessage("Client Server Connection Disconnected!");
			broadcast(message);

		} catch (Exception e) {
			logger.error("[FracServerEndPoint][onClose][error]" + e.getMessage());
		}

	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.error("Client Connection Failed With Error" + throwable.getMessage());
	}

	private void broadcast(FracMessage message) {
		String username = null;
		try {
			for (Session peer : peers) {
				if (peer.isOpen()) {
					username = (String) peer.getUserProperties().get(peer.getId());
					if (username.equalsIgnoreCase(message.getUserName())) {
						peer.getBasicRemote().sendObject(message);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Exception While Broadcasting to " + username + " Client" + e.getMessage());
		}
	}

	public void sendFracAlertToClient(FracMessage message) {
		try {
			onMessage(session, message);
		} catch (Exception e) {
			logger.error("[FracServerEndPoint][sendFracAlertToClient][error]" + e.getMessage());
		}
	}

	public Set<String> getAllClientUsers() {
		Set<String> clientUser = new HashSet<String>();
		try {
			for (Session peer : peers) {
				if (peer.isOpen()) {
					String username = (String) peer.getUserProperties().get(peer.getId());
					clientUser.add(username);
				}

				 else{ logger.error("Session is not Opened"); } 

			}
		} catch (Exception e) {
			logger.error("Exception While Iterating Session" + e.getMessage());
		}
		return clientUser;

	}

}