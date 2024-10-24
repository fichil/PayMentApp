package com.cykj.config;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * WebSocket 配置类，用于注册 WebSocket 处理器。
 */
@Configuration
@EnableWebSocket // @EnableWebSocket 注解会启用 WebSocket 的支持
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public CustomWebSocketHandler customWebSocketHandler() {
        return new CustomWebSocketHandler();
    }
    /**
     * registerWebSocketHandlers 方法会被调用，用于注册 WebSocket 处理器
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册 WebSocket 处理器，并指定访问路径 "/browserInfo"
        registry.addHandler(customWebSocketHandler(), "/browserInfo").
                setAllowedOrigins("*"); // 如果需要 SockJS 兼容性 // 允许来自指定域的请求
    }

    /**
     * WebSocket 处理器，用于处理浏览器发送的消息。
     * 当连接请求到达 /browserInfo时，Spring WebSocket 框架会将该请求转发给 BrowserInfoWebSocketHandler。
     */
    public class CustomWebSocketHandler implements WebSocketHandler {


        private Map<String,WebSocketSession> sessionsMap = new HashMap<>();

        @Autowired
        private RocketMQTemplate rocketMQTemplate;

        /**
         * afterConnectionEstablished 方法在建立 WebSocket 连接后被调用，可以在这个方法中进行一些初始化操作。
         * @param session
         * @throws Exception
         */
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            System.out.println("WebSocket connection established: " + session.getId());

        }

        /**
         * handleMessage 方法用于处理客户端发送的消息，并向客户端发送响应消息。
         * @param session
         * @param message
         * @throws Exception
         */
        @Override
        public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
            System.out.println("Received message: " + message.getPayload());
            System.out.println(session);
            System.out.println(message);
            //先判断map有没有
            for (String key : sessionsMap.keySet()) {
                WebSocketSession webSocketSession = sessionsMap.get(key);
                if (session.getId() == webSocketSession.getId()){
                    return;
                }
            }

            System.out.println("我是" + message.getPayload().toString());
            if (message.getPayload().toString().equals("请求支付")) {
                sessionsMap.put(session.getId(),session);
                rocketMQTemplate.convertAndSend("createOrder",session.getId());
            }
        }

        /**
         * handleTransportError 方法在 WebSocket 传输发生错误时被调用。
         * @param session
         * @param exception
         * @throws Exception
         */
        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            System.out.println("WebSocket transport error: " + exception.getMessage());
        }

        /**
         *afterConnectionClosed 方法在 WebSocket 连接关闭后被调用，可以在这个方法中进行一些清理操作
         * @param session
         * @param closeStatus
         * @throws Exception
         */
        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
            System.out.println("WebSocket connection closed: " + closeStatus.getCode() + " - " + closeStatus.getReason());
            sessionsMap.remove(session);
        }

        /**
         * supportsPartialMessages 方法用于设置是否支持部分消息传输
         * @return
         */
        @Override
        public boolean supportsPartialMessages() {
            return false;
        }


        public Map<String, WebSocketSession> getSessionsMap() {
            return sessionsMap;
        }

        public void setSessionsMap(Map<String, WebSocketSession> sessionsMap) {
            this.sessionsMap = sessionsMap;
        }
    }

}
