package chat.service;

import chat.comment.Event;
import chat.comment.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.ObjectInputStream;

//该类对应的对象要和某个客户端保存通信
public class ServiceConnectClientThread extends Thread{
    private Socket socket;
    private Event event = new Event<>();
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public ServiceConnectClientThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {//线程一直处于run的状态，可以接收/发送消息
        while(true) {
            try {
                ois = new ObjectInputStream(socket.getInputStream());
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                event = (Event) ois.readObject();
                System.out.println(event.getMesType());
                if(event.getMesType().equals(MessageType.MESSAGE_REGISTER)){//注册，添加账号
                    QQUtils.checkId(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_LOGIN)){//请求登录
                    QQUtils.checkLogin(event,socket,this);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_ID)){//判断Id对应的邮箱号
                    QQUtils.isId(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_ID1)){//个人资料
                    QQUtils.checkFriendMessage(event, socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_FIND_GROUP_MEMBER)){
                    QQUtils.checkGroupMember(event, socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_PASSWORD)||(event.getMesType().equals(MessageType.MESSAGE_CHANGE_PASSWORD))){//修改密码
                    QQUtils.checkPassword(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_CHANGE)){//修改消息
                    QQUtils.checkMessage(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_EmailSend)||event.getMesType().equals(MessageType.MESSAGE_EmailSend1)){//发送验证码
                    QQUtils.checkEmail(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_CLOSE)){//退出登录
                    QQUtils.checkClose(event,socket);
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println(socket.getRemoteSocketAddress() + "下线了！");
                    break;
                }
                if(event.getMesType().equals(MessageType.MESSAGE_ADD_FRIEND)){//添加好友
                    QQUtils.addFriend(event);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_ADD_GROUP)){
                    QQUtils.addGroup(event);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_SEEK)){//查看好友申请消息
                    QQUtils.watchNews(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_SEEK_GROUP)){
                    QQUtils.watchGroupNews(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_REQUEST_SUCCEED)||event.getMesType().equals(MessageType.MESSAGE_REQUEST_FAIL)){//对方同意添加好友
                    QQUtils.updateNews(event);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_REQUEST_GROUP_SUCCEED)||event.getMesType().equals(MessageType.MESSAGE_REQUEST_GROUP_FAIL)){
                    QQUtils.updateGroupNews(event);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_FIND_FRIEND)){//查看所有好友
                    QQUtils.findAllFriend(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_FIND_ONLINE)){
                    QQUtils.findFriendOnLine(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_FIND_GROUP)){
                    QQUtils.findAllGroup(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_ON_TIME)){//验证码的时效性
                    QQUtils.checkOnTime(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_DELETE_FRIEND)){//删除好友
                    QQUtils.deleteFriend(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_OUT_GROUP)){
                    QQUtils.outGroup(event);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_DELETE_GROUP)){
                    QQUtils.deleteGroup(event);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_FIND_INFORMATION)){//查询所有消息
                    QQUtils.findAllInformation(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_SEND_INFORMATION)){//发送消息
                    QQUtils.sendMessage(event);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_CREAT_GROUP)){
                    QQUtils.creatGroup(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_IS_FRIEND)){
                    QQUtils.isFriend(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_FIND_GROUP_INFORMATION)){//找群聊消息
                    QQUtils.findAllGroupInformation(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_ID2)){
                    QQUtils.idRight(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_CHANGE_GROUP)){
                    QQUtils.updateGroup(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_DELETE_GROUP_PEOPLE)){
                    QQUtils.deleteGroupPeople(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_SET_GROUP_MANAGE)){
                    QQUtils.setGroupManage(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_DELETE_GROUP_MANAGE)){
                    QQUtils.deleteGroupManage(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_CHANGE_GROUP_POWER)){
                    QQUtils.changeGroupPower(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_CREAT_GROUP_PEOPLE)){
                    QQUtils.creatGroupPeople(event);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_SEND_DOCUMENT)){
                    QQUtils.downDocument(event);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_CHECK_COMMENT)){
                    QQUtils.checkComment(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_ADD_COMMENT)){
                    QQUtils.addComment(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_DOWN_DOCUMENT)){
                    QQUtils.fileDown(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_SEEK_LIKE)){
                    QQUtils.checkAll(event,socket);
                }
                if(event.getMesType().equals(MessageType.MESSAGE_SEEK_GROUP_NEWS)){
                    QQUtils.findGroupNews(event,socket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}