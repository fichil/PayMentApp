package com.cykj.util;

import com.alibaba.fastjson.JSON;
import com.cykj.pojo.CmsUser;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.websocket.Session;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;


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
        registry.addHandler(customWebSocketHandler(), "/chatRequest").
                setAllowedOrigins("*"); // 如果需要 SockJS 兼容性 // 允许来自指定域的请求
    }

    /**
     * WebSocket 处理器，用于处理浏览器发送的消息。
     * 当连接请求到达 /browserInfo时，Spring WebSocket 框架会将该请求转发给 CustomWebSocketHandler。
     */
    public class CustomWebSocketHandler implements WebSocketHandler {
        /**
         * 存储用户id和对应的session，管理员的也在里面，管理员的键为userid，相当于也是一个用户，和对应的usrrid共享页面
         */
        public  Map<String,Object> userMap = new HashMap();


        /**
         * 聊天记录
         */
        public  Map<Object,Object> chatRecordMap = new HashMap();


        /**
         * 管理员列表session
         */
       // public WebSocketSession adminSession;//
        public List<WebSocketSession> adminSession = new ArrayList<>();

        /**
         * 记录消息已读未读
         */
        Map<String,String> list = new LinkedHashMap<>();



        /**
         * afterConnectionEstablished 方法在建立 WebSocket 连接后被调用，可以在这个方法中进行一些初始化操作。
         * @param session
         * @throws Exception
         */
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            //System.out.println("WebSocket connection established: " + session.getId());
            // 获取 URI
            URI uri = session.getUri();
            System.out.println("uri是" + uri );
            System.out.println(session.getId());
            //查询部分通常是 URL 中问号 (?) 后面的部分，它包含了一个或多个键值对，格式通常为 key1=value1&key2=value2
            String query = uri.getQuery();
            // 解析查询参数
            Map<String,String> params = parseQuery(query);


            if (params != null && params.size() > 0) {
                // 获取特定参数，例如 user_id
                String userId = params.get("userId");
                String chatType = params.get("chatType");

                /*--------------管理员列表-----------------------*/
                if (ChatTypeEnum.adminListChatType.getKey().equalsIgnoreCase(chatType)) {
                    adminSession.add(session);//同个管理员账号可能在多个地方打开,都要通知
                    //userMap用来存储用户id,根据里面每个id拿到当前保持连接的每个用户信息，返回一个用来存储每个用户session的list
                    Map<String, String> userList = getUserList(userMap, null,"0");
                    System.out.println("初始化时的用户列表" + userList);
                    //把当前在线用户id都发回去，在列表中渲染
                    chat_list_show(adminSession, userList);
                    return;
                }


                /*--------------管理员聊天框打开-----------------------*/
                if (ChatTypeEnum.adminChatType.getKey().equalsIgnoreCase(chatType)) {//管理员要打开会话信息
                    if (userId.equals("0") || userId.equals("undefined")) {//0的值这个页面是子路由，父路由打开它也会自己加载，第一次加载这个页面初始值为0，要忽略掉这次
                        System.out.println("拒绝");
                        return;
                    }
                    // 从 userMap 中获取该管理员（userId）对应的会话列表
                    //sessions 作为值（List 类型）。一个用户可能有多个 WebSocket 会话连接（例如，一个用户可能同时在多个设备或浏览器上登录）。
                    //取出来，把当前的在加进去
                    List sessions = (List) userMap.get(userId); //根据userid来确定是要发给哪个用户的
                    // 如果该管理员还没有会话列表，初始化一个新的空列表
                    if (sessions == null) {
                        sessions = new ArrayList();
                    }
                    // 将当前 session 添加到在线用户中，sessions里面的session都是同一个人，防止一个账号多个地方打开
                    sessions.add(session);
                    // 将更新后的会话列表重新放回 userMap 中
                    userMap.put(userId, sessions);

                }

                //上下这两步可以合为一步
                /*--------------用户聊天框打开
                将更新后的会话列表重新放回 userMap 中，确保用户 ID 和其会话列表关联在一起。
                这样，可以在需要的时候通过 userId 找到这个用户的所有 WebSocket 会话，进行消息推送等操作。
                -----------------------*/
                if (ChatTypeEnum.userChatType.getKey().equalsIgnoreCase(chatType)) {
                    System.out.println("用户来聊天了");
                    // 判断是否已经为该用户建立了聊天通道 (即检查是否已经有该用户的 session 列表)
                    List sessions = (List) userMap.get(userId);
                    if (sessions == null) {
                        sessions = new ArrayList();
                    }
                    sessions.add(session);
                    System.out.println("用户来聊天了,放入的" + userId);
                    userMap.put(userId, sessions);

                    /**
                     * 写在用户聊天处理里，这时才需要刷新列表显示新消息
                     * 这里要看看怎么跟前端提醒有新消息
                     */
                    // 如果有管理员会话，向管理员更新用户列表
                    if (adminSession != null) {//admin在外面定义了，还为空，初始化的时候如果不为空就加入adminSession
                        // 从 userMap 中获取所有活跃用户的列表
                        Map<String, String> userList = getUserList(userMap, null,"0");
                        // 向管理员的 WebSocket 会话展示更新后的用户列表
                        chat_list_show(adminSession, userList);

                    }
                }

                //聊天记录信息存放,chatRecordMap用来存放聊天记录
                List chatRecords = (List) chatRecordMap.get(userId);// 从 聊天记录chatRecordMap 中获取该用户的聊天记录
                if (chatRecords != null) {// // 如果该用户有聊天记录
                    // 将聊天记录发送给该用户的所有 WebSocket 会话,一个用户可能在多个地方登录
                    chat((List<WebSocketSession>) userMap.get(userId), chatRecords);
                }



            }
        }

        /**
         * 发送消息
         *
         * @param  {userId:'',message:'',create_time:'',create_date:'',chat_type:'admin_list/admin_chat/user_chat'}
         *             admin_list:表示客服列表数据请求
         *             admin_chat:表示客服回复页面请求
         *             user_chat表示用户消息页面请求
         *
         *
         * @throws Exception
         */
        @Override
        public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
            System.out.println("有人发消息了");
            // 获取 URI
            URI uri = session.getUri();
            System.out.println(uri);
            //查询部分通常是 URL 中问号 (?) 后面的部分，它包含了一个或多个键值对，格式通常为 key1=value1&key2=value2
            String query = uri.getQuery();
            // 解析查询参数
            Map<String,String> params = parseQuery(query);

            String userId = params.get("userId");

            //用户发送的信息
            String msg = message.getPayload() + "";
            //请求类型
            String chatType = params.get("chatType");

            /*--------------管理员列表点击查看了某条消息，这时候更新列表标记-----------------------*/
            if(ChatTypeEnum.adminListChatType.getKey().equalsIgnoreCase(chatType)) {
                //System.out.println("msg" + msg);
                //msg转为
                Map<String, String> userList = getUserList(userMap, msg,"0");//userId是否替换成msg?
                //把当前在线用户id都发回去，在列表中渲染
                chat_list_show(adminSession, userList);
            }



            /*--------------管理员聊天-----------------------*/
                if (ChatTypeEnum.adminChatType.getKey().equalsIgnoreCase(chatType)) {
                    //把管理员加入用户建立的聊天管道中
                    //用户聊天
                    //封装请求参数，时间为当前时间
                    ChatDTO chatDTO = new ChatDTO();
                    //userId=admin表示是客服的回复,给前端根据这个来排版消息
                    chatDTO.setUserId("admin")
                            .setMessage(msg)
                            .setCreateDate( LocalDateTime.now() + "")
                            .setCreateTime(LocalDateTime.now() + "");
                    //聊天记录信息存放，取出原有的加进去
                    List chatRecords = (List) chatRecordMap.get(userId);
                    if (chatRecords == null) {
                        chatRecords = new ArrayList();
                    }
                    chatRecords.add(JSON.toJSONString(chatDTO));
                    chatRecordMap.put(userId, chatRecords);
                    chat((List<WebSocketSession>) userMap.get(userId), chatRecords);//发送消息

                }
                /*--------------用户聊天-----------------------*/
                if (ChatTypeEnum.userChatType.getKey().equalsIgnoreCase(chatType)) {
                    //封装请求参数，时间为当前时间
                    ChatDTO chatDTO = new ChatDTO();//聊天记录dto
                    chatDTO.setUserId(userId)
                            .setMessage(msg)
                            .setCreateDate(LocalDateTime.now() + "")
                            .setCreateTime(LocalDateTime.now() + "");
                    String key = chatDTO.getUserId();

                    //聊天记录信息存放
                    List chatRecords = (List) chatRecordMap.get(key);
                    if (chatRecords == null) {
                        chatRecords = new ArrayList();
                    }
                    chatRecords.add(JSON.toJSONString(chatDTO));
                    // 更新 chatRecordMap 中该用户的聊天记录列表
                    chatRecordMap.put(key, chatRecords);
                    System.out.println("聊天记录" + chatRecordMap);
                    // 将更新后的聊天记录发送给该用户的所有 WebSocket 会话
                    if (userMap.get(key) != null){
                        chat((List<WebSocketSession>) userMap.get(key), chatRecords);//发送消息
                    } else {
                        TextMessage message1 = new TextMessage("请重新发起会话");
                        session.sendMessage(message1);
                    }


                    /**
                     * 写在用户聊天处理里，这时才需要刷新列表显示新消息
                     * 这里要看看怎么跟前端提醒有新消息
                     */
                    // 如果有管理员会话，向管理员更新用户列表
                    if (adminSession != null) {//admin在外面定义了，还为空，初始化的时候如果不为空就加入adminSession
                        // 从 userMap 中获取所有活跃用户的列表
                        Map<String, String> userList = getUserList(userMap, userId,"1");
                        // 向管理员的 WebSocket 会话展示更新后的用户列表
                        chat_list_show(adminSession, userList);
                    }
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
            // 获取 URI
            URI uri = session.getUri();
            //查询部分通常是 URL 中问号 (?) 后面的部分，它包含了一个或多个键值对，格式通常为 key1=value1&key2=value2
            String query = uri.getQuery();
            // 解析查询参数
            Map<String,String> params = parseQuery(query);

            String userId = params.get("userId");
            //请求类型
            String chatType = params.get("chatType");


            /*--------------管理员聊天框关闭-----------------------*/
            if (ChatTypeEnum.adminListChatType.getKey().equalsIgnoreCase(chatType)) {
                //移除相应
                System.out.println(userId + "断开连接了");
                System.out.println(userMap);
                userMap.remove(userId);
                System.out.println(userMap);
                System.out.println(list);
                list.remove(userId);
                System.out.println(list);
                System.out.println(chatRecordMap);
                chatRecordMap.remove(userId);
                System.out.println(chatRecordMap);
            }

            /*--------------用户聊天框关闭-----------------------*/
            if (ChatTypeEnum.userChatType.getKey().equalsIgnoreCase(chatType)) {
                    //移除相应
                    System.out.println(userId + "断开连接了");
                    System.out.println(userMap);
                    userMap.remove(userId);
                    System.out.println(userMap);
                    System.out.println(list);
                    list.remove(userId);
                    System.out.println(list);
                    System.out.println(chatRecordMap);
                    chatRecordMap.remove(userId);
                    System.out.println(chatRecordMap);
                    chat_list_show(adminSession, list);

            }



        }

        /**
         * supportsPartialMessages 方法用于设置是否支持部分消息传输
         * @return
         */
        @Override
        public boolean supportsPartialMessages() {
            return false;
        }

        private Map<String, String> parseQuery(String query) {
            Map<String, String> queryPairs = new HashMap<>(); // 用于存储解析的参数
            if (query != null) { // 确保查询字符串不为 null
                String[] pairs = query.split("&"); // 按 '&' 分割查询参数
                for (String pair : pairs) { // 遍历每一个键值对
                    int idx = pair.indexOf("="); // 查找 '=' 的索引
                    String key = idx > 0 ? pair.substring(0, idx) : pair; // 获取键
                    String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : null; // 获取值
                    queryPairs.put(key, value); // 将键值对存入 Map
                }
            }
            return queryPairs; // 返回存储查询参数的 Map
        }

        public synchronized Map<String,String> getUserList(Map userMap,String currentId,String state) {
         if(list.size() > 0) {
             if (state.equals("0")){//把已读消息取消掉
                 for (Object str : userMap.keySet()) {
                     String userId = (String)str;
                     System.out.println("当前在线用户" + userId);
                     String s = list.get(userId);
                     if(s == null){
                         System.out.println("新加id" + userId);
                         list.put(userId,"0");
                     } else {
                         if(userId.equals(currentId)) {
                             list.put(userId,"0");
                         }
                     }
                 }
             } else {
                 for (Object str : userMap.keySet()) {
                     String userId = (String) str;
                     System.out.println("当前在线用户" + userId);
                     String s = list.get(userId);
                     if (s == null) {
                         System.out.println("新加id" + userId);
                         list.put(userId, "0");
                     } else {
                         if (userId.equals(currentId)) {
                             list.put(userId, "1");
                         }
                     }
                 }
             }
         } else {
             for (Object str : userMap.keySet()) {
                 String userId = (String) str;
                 list.put(userId,"0");//这个id有新消息
             }
         }
            return list;//返回当前在线的用户id
        }



        /**
         * 聊天列表显示
         */
        public void chat_list_show(List<WebSocketSession> sessions, Map<String,String> list) {
            if (sessions != null) {
                for (int i = 0; i < sessions.size(); i++) {
                    try {
                        if (sessions.get(i).isOpen()) {
                            //当当前会话没有被关闭 发送消息
                            //将数据（用户列表等）转换为 JSON 并通过 WebSocket 发送。
                            // 将 list 转换为 JSON 字符串
                            // 使用 TextMessage 包装 JSON 字符串
                            //TextMessage message = new TextMessage(JSON.toJSONString(list));
                            TextMessage message = new TextMessage(JSON.toJSONString(list));
                            // 发送消息到前端
                            sessions.get(i).sendMessage(message);//会发送当前还在线的商户
                            System.out.println("已经发送列表信息了");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        /**
         * 消息广播
         *
         * @param sessions
         */
        public void chat(List<WebSocketSession> sessions, List list) {
            if (sessions != null) {
                for (int i = 0; i < sessions.size(); i++) {
                    try {
                        if ( sessions.get(i).isOpen()) {
                            //当当前会话没有被关闭 发送消息
                            TextMessage message = new TextMessage(JSON.toJSONString(list));
                            // 发送消息到前端
                            sessions.get(i).sendMessage(message);//会发送当前还在线的商户
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        }

    }

}
