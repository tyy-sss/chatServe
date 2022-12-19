package chat.service;

import chat.comment.Identify;

import java.util.concurrent.ConcurrentHashMap;

//验证码的时效性
public class EmailTimeThread {
    private static ConcurrentHashMap<String, Identify> hm=new ConcurrentHashMap<>();
    public static void add(String email, Identify identify) {
            hm.put(email, identify);
     }
    public static Identify getYzm(String email) {
            return hm.get(email);
    }
}
