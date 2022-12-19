package chat.service;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

//该类管理客服端连接服务端的线程
public class ManageClientConnectServiceThread {
    public static ConcurrentHashMap<String,ServiceConnectClientThread> hm=new ConcurrentHashMap<>();
}
