package com.example.wskafka.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.wskafka.common.Constants;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
	// 세션들을 저장할 변수
	private final Set<WebSocketSession> sessions = new HashSet<>();
	private final KafkaTemplate<String, String> kafkaTemplate;

	// 클라이언트가 연결하면 세션 저장 변수에 클라이언트 세션을 추가
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
		System.out.println(session.getId() + Constants.CLIENT_CONNECTION_MSG);
	}

	// 메시지를 받으면 다른 사람들한테 메세지를 전송
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		kafkaTemplate.send(Constants.CHAT_TOPIC, message.getPayload());
	}

	@KafkaListener(topics = Constants.CHAT_TOPIC, groupId = Constants.CHAT_GROUP)
	public void receive(String message) throws IOException {
		for (WebSocketSession s : sessions) {
			s.sendMessage(new TextMessage(message));
		}
	}

	// 클라이언트가 연결으르 끊으면 세션 저장 변수에서 클라이언트 세션을 삭제
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		System.out.println(session.getId() + Constants.CLIENT_CLOSED_MSG);
	}
}

