package chat.utils;

import org.apache.commons.mail.HtmlEmail;

import java.util.Random;

public class EmailSend {

    //发送验证码
    public static boolean sendEmail(String sendTo,String a) {
        try {
            //创建网页邮箱对象
            HtmlEmail email = new HtmlEmail();
            //基本设置
            email.setDebug(true);
            //设置为QQ邮箱作为发送主邮箱
            email.setHostName("SMTP.qq.com");
            email.setSmtpPort(587);
            //qq邮箱的验证信息
            email.setAuthentication("3127023395@qq.com", "dshnyywbzxnddfce");
            //设置邮件发送人
            email.setFrom("3127023395@qq.com");
            //设置邮件接收人
            email.addTo(sendTo);
            //设置发送的内容
            email.setMsg(a);
            //设置邮箱标题
            email.setSubject("验证码");
            //执行邮件发送
            email.send();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //生成验证码
    public static String getCode() {
        char[] arr = new char[26 + 26];
        int index = 0;
        for (int i = 97; i <= 122; i++) { //小写字母
            arr[index] = (char) i;
            index++;
        }
        for (int i = 65; i <= 90; i++) { //大写字母
            arr[index] = (char) i;
            index++;
        }
        String result = "";
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            int randomIndex = r.nextInt(arr.length);
            char c = arr[randomIndex];
            result += c;
        }
        int number = r.nextInt(10);
        return result + number;
    }

}
