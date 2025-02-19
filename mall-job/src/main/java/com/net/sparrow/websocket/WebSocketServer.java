package com.net.sparrow.websocket;

import cn.hutool.json.JSONUtil;
import com.net.sparrow.entity.common.CommonNotifyEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Sparrow
 * @Description: WebSocketServer
 * @DateTime: 2025/2/19 15:43
 **/
@ServerEndpoint("/websocket/{userId}")
@Component
@Slf4j
public class WebSocketServer {
	/**
	 * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	 */
	private static int onlineCount = 0;

	/**
	 * 用来存放每个客户端对应的webSocket对象
	 */
	private static ConcurrentHashMap<Long, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

	/**
	 * 与某个客户端的连接会话
	 */
	private Session session;

	/**
	 * 接收userId
	 */
	private Long userId;

	/**
	 * 连接成功调用的方法
	 *
	 * @param session
	 * @param userId
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("userId") Long userId) {
		this.session = session;
		this.userId = userId;
		if (webSocketMap.containsKey(userId)) {
			webSocketMap.remove(userId);
		} else {
			webSocketMap.put(userId, this);
			addOnlineCount();
		}
		log.info("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());
		try {
			HashMap<Object, Object> hashMap = new HashMap<>();
			hashMap.put("key", "连接成功");
			sendMessage(JSONUtil.toJsonStr(hashMap));
		} catch (IOException e) {
			log.error("用户userId:" + userId + ",网络异常!!!!!!");
		}
	}

	/**
	 * 实现服务器主动推送
	 *
	 * @param message
	 */
	public void sendMessage(String message) throws IOException {
		synchronized (session) {
			try {
				this.session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				log.error("服务器推送失败:" + e.getMessage());
				throw e;
			}
		}
	}

	/**
	 * 发送自定义消息
	 * @throws IOException
	 */
	public static void sendMessage(CommonNotifyEntity commonNotifyEntity) throws IOException {
		//如果userId为空，向所有群体发送
		if (Objects.nonNull(commonNotifyEntity.getToUserId())) {
			//向所有用户发送信息
			Iterator<Long> iterator = webSocketMap.keySet().iterator();
			while (iterator.hasNext()) {
				Long userId = iterator.next();
				WebSocketServer item = webSocketMap.get(userId);
				item.sendMessage(commonNotifyEntity.getContent());
			}
		}
		//如果不为空，则发送指定用户信息
		else if (webSocketMap.containsKey(commonNotifyEntity.getToUserId())) {
			WebSocketServer item = webSocketMap.get(commonNotifyEntity.getToUserId());
			item.sendMessage(commonNotifyEntity.getContent());
		} else {
			log.error("请求的userId:" + commonNotifyEntity.getToUserId() + "不在该服务器上");
		}
	}

	@OnClose
	public void onClose() {
		if (webSocketMap.containsKey(userId)) {
			webSocketMap.remove(userId);
			subOnlineCount();
		}
		log.info("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message) {
		log.info("用户消息:" + userId + ",报文:" + message);
		if (StringUtils.isNoneBlank(message)) {
			try {
				if (Objects.nonNull(userId) && webSocketMap.containsKey(userId)) {
					webSocketMap.get(userId).sendMessage(message);
				} else {
					log.error("请求的userId:" + userId + "不在该服务器上");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发生错误时候
	 *
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
		error.printStackTrace();
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketServer.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketServer.onlineCount--;
	}


}
