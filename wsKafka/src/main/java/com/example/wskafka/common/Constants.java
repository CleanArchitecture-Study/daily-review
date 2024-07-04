package com.example.wskafka.common;

public class Constants {
	/*
	2. refactor
	클래스에서 필요한 상수 값들을 하나의 클래스에서 관리하도록 수정
	*/
	public static final String CHAT_TOPIC = "chat";
	public static final String CHAT_GROUP = "chat_group1";
	public static final String CLIENT_CONNECTION_MSG = " 클라이언트가 접속했습니다";
	public static final String CLIENT_CLOSED_MSG = " 클라이언트가 연결을 끊었습니다";
}
