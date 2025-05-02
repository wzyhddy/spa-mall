package com.net.sparrow.websocket;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.net.sparrow.entity.common.CommonNotifyEntity;
import lombok.extern.slf4j.Slf4j;
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
 * @Description: TODO
 * @DateTime: 2025/5/2 08:32
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
	 * 用来存放每个客户端对应的WebSocket对象。
	 */
	private static ConcurrentHashMap<Long, WebSocketServer> webSocketMap = new ConcurrentHashMap<Long, WebSocketServer>();

	/**
	 * 与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	private Session session;

	/**
	 * 接收userId
	 */
	private Long userId;

	/**
	 * 连接建立成功调用的方法
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
			HashMap<Object, Object> map = new HashMap<>();
			map.put("key", "连接成功");
			sendMessage(JSON.toJSONString(map));
		} catch (IOException e) {
			log.error("用户userId:" + userId + ",网络异常!!!!!!");
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		if (webSocketMap.containsKey(userId)) {
			webSocketMap.remove(userId);
			subOnlineCount();
		}
		log.info("用户退出userId:" + userId + ",当前在线人数为:" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 * @param session
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("用户消息:" + userId + ",报文:" + message);
		if (StringUtils.isNotBlank(message)) {
			try {
				if (Objects.nonNull(userId) && webSocketMap.containsKey(userId)) {
					webSocketMap.get(userId).sendMessage(message);
				} else {
					log.error("请求的userId:" + userId + "不在该服务器上");
				}
			} catch (Exception e) {
				log.error("服务器处理通知失败:" + e.getMessage());
			}
		}
	}


	@OnError
	public void onError(Session session, Throwable error) {
		log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
		error.printStackTrace();
	}

	/**
	 * 实现服务器主动推送
	 */
	public void sendMessage(String message) throws IOException {
		//加入线程锁
		synchronized (session) {
			try {
				//同步发送信息
				this.session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				log.error("服务器推送失败:" + e.getMessage());
				throw e;
			}
		}
	}

	/**
	 * 发送自定义消息
	 *
	 * @param message
	 * @param toUserId
	 * @throws IOException
	 */
	public static void sendMessage(CommonNotifyEntity commonNotifyEntity) throws IOException {
		//如果userId为空，向所有群体发送
		if (Objects.isNull(commonNotifyEntity.getToUserId())) {
			Iterator<Long> iterator = webSocketMap.keySet().iterator();
			while (iterator.hasNext()) {
				Long userId = iterator.next();
				WebSocketServer item = webSocketMap.get(userId);
				item.sendMessage(commonNotifyEntity.getContent());
			}
		} else if (webSocketMap.containsKey(commonNotifyEntity.getToUserId())) {
			WebSocketServer item = webSocketMap.get(commonNotifyEntity.getToUserId());
			item.sendMessage(commonNotifyEntity.getContent());
		} else {
			log.error("请求的userId:" + commonNotifyEntity.getToUserId() + "不在该服务器上");
		}
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
