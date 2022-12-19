package chat.service;

import Jdbc.dao.Dao;
import Jdbc.dao.DaoImpl;
import chat.comment.*;
import chat.utils.EmailSend;
import chat.utils.MD5;
import chat.utils.PictureShow;
import chat.utils.SnowFlake;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static chat.service.ManageClientConnectServiceThread.hm;

public class QQUtils {
    private static Dao dao=new DaoImpl();
    //注册方法
    public static void checkId(Event event, Socket socket){
        String id="";
        id= SnowFlake.start();
        User user=(User) event.get();
        user.setUserId(id);
        user.setPassword(MD5.MD5Encode(user.getPassword(),"utf8"));
        File file=new File("D:\\headPicture\\sever\\person.png");
        PictureShow.set(PictureShow.get(file), user.getUserId());
        user.setBytes(PictureShow.get(file));
        user.setPicture("D:/headPicture/sever/"+user.getUserId()+".png");
        if(dao.insertUser(user)!=0) {
            event.set(user);
            event.setMesType(MessageType.MESSAGE_REGISTER_SUCCEED);
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //登录方法
    public static void checkLogin(Event event, Socket socket, ServiceConnectClientThread thread){
        User user=(User) event.get();
        if(user.getUserId()!=null) {
            User use = dao.findUserById(user.getUserId());
            if (use.getId() != null && use.getPassword().equals(MD5.MD5Encode(user.getPassword(), "utf8")) && use.getStatus().equals("0")) {
                use.setStatus("1");
                event.set(use);
                dao.updateUser(use);
                event.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                hm.put(use.getUserId(), thread);
            } else {
                event.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
            }
        }else if(user.getEmail()!=null){
            User use=dao.findUserByEmail(user.getEmail());
            if(use.getStatus().equals("0")&&use.getId()!=null){
                use.setStatus("1");
                event.set(use);
                dao.updateUser(use);
                event.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                hm.put(use.getUserId(), thread);
            }else{
                event.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
            }
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //判断Id是不是正确的
    public static void isId(Event event,Socket socket){
        User user=(User) event.get();
        user=dao.findUserById(user.getUserId()) ;
        if (user.getId() != null) {
            event.set(user);
            event.setMesType(MessageType.MESSAGE_ID_SUCCEED);//ID是对的
        } else {
            event.setMesType(MessageType.MESSAGE_ID_FAIL);//ID是错的
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //重写密码
    public static void checkPassword(Event event,Socket socket){
        User user=(User) event.get();
        System.out.println(event.getMesType()+" "+user);
        User use = dao.findUserByEmail(user.getEmail());
        use.setPassword(MD5.MD5Encode(user.getPassword(),"utf8"));
        System.out.println(use);
        //验证
        if(dao.updateUser(use)!=0){
            event.set(use);
            event.setMesType(MessageType.MESSAGE_PASSWORD_SUCCEED);//重写密码成功
        } else {
            event.setMesType(MessageType.MESSAGE_PASSWORD_FAIL);
        }ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //修改个人信息
    public static void checkMessage(Event event,Socket socket){
        User user=(User) event.get();
        User use=dao.findUserById(user.getUserId());
        user.setId(use.getId());
        user.setPassword(use.getPassword());
        user.setStatus(use.getStatus());
        user.setPicture(use.getPicture());
        PictureShow.set(user.getBytes(), use.getUserId());
        if(dao.updateUser(user)!=0) {
            event.set(user);
            event.setMesType(MessageType.MESSAGE_CHANGE_SUCCEED);
        }else{
            event.setMesType(MessageType.MESSAGE_CHANGE_FAIl);
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //修改群消息
    public static void updateGroup(Event event, Socket socket) {
        Room room = (Room) event.get();
        PictureShow.set(room.getBytes(), room.getId());
        ArrayList<Room> rooms=dao.findAllRoomPeople(room.getId());//找到群的所有消息
        for(Room room1 : rooms){
            room1.setName(room.getName());
            dao.updateRoom(room1);
        }
        event.set(room);
        event.setMesType(MessageType.MESSAGE_CHANGE_GROUP_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //发送验证码
    public static void checkEmail(Event event, Socket socket) {
        User user=(User) event.get();
        if((dao.findUserByEmail(user.getEmail()).getUserId()==null&&event.getMesType().equals(MessageType.MESSAGE_EmailSend))||(dao.findUserByEmail(user.getEmail()).getUserId()!=null&&event.getMesType().equals(MessageType.MESSAGE_EmailSend1)))
        {
            String id=dao.findUserByEmail(user.getEmail()).getUserId();
            String yzm = EmailSend.getCode();
            if (EmailSend.sendEmail(user.getEmail(), yzm)) {
                long time=System.currentTimeMillis();//获取系统的当前时间
                Identify identify =new Identify(user.getEmail(), yzm,time);
                EmailTimeThread.add(user.getEmail(), identify);
                Message message =new Message();
                message.setYzm(yzm);
                user.setUserId(id);
                event.set(user);
                event.set(message);
                event.setMesType(MessageType.MESSAGE_EmailSend_SUCCEED);
            } else {
                event.setMesType(MessageType.MESSAGE_EmailSend_FAIL);
            }
        }else{
            event.setMesType(MessageType.MESSAGE_EmailSend_FAIL);
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //退出登录
    public static void checkClose(Event event, Socket socket) {
        String id=(String) event.get();
        if(id!=null) {
            User use = dao.findUserById(id);
            use.setStatus("0");
            if (dao.updateUser(use) != 0)
                event.setMesType(MessageType.MESSAGE_CLOSE_SUCCEED);
            hm.remove(use.getUserId());
        }else{
            event.setMesType(MessageType.MESSAGE_CLOSE_SUCCEED);
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //判断是否已经添加好友，没有添加好友，保存好友记录
    public static void addFriend(Event event) {
        Friend friend =(Friend) event.get();
        Friend friend1=dao.findFriendById(friend.getUserId(), friend.getFriendId());
        //查表是不是朋友
        if((friend1.getId()==null)||(friend1.getId()!=null&&friend1.getStatus().equals("2")!=true)){
            friend.setStatus("0");//发送验证请求
            if(friend1.getId()==null)//没有添加过好友
                dao.addFriend(friend);
            else if (friend1.getId() != null && ( friend.getCreateTime().equals(friend1.getCreateTime()) != true))
                dao.updateFriend(friend);
            event.set(friend);
            //保存消息在数据库
            News news = new News();
            news.setSend(friend.getUserId());//发送者
            news.setReceive(friend.getFriendId());//接收者
            news = dao.findNews(news.getSend(), news.getReceive());
            if (news.getId() == null) {//没有过消息记录
                news.setStatus("0");
                news.setSend(friend.getUserId());//发送者
                news.setReceive(friend.getFriendId());//接收者
                dao.insertNews(news);
            } else {//有过消息记录，改变状态
                news.setStatus("0");
                dao.updateNews(news);
            }
        }
    }
    //添加群聊
    public static void addGroup(Event event) {
        GroupNews groupNews = (GroupNews) event.get();
        GroupNews groupNews1= dao.findGroupNews(groupNews.getSend(),groupNews.getGroupId());
        if(groupNews1.getId()==null){//群聊申请消息没有出现过
            dao.insertGroupNews(groupNews);
        }else{//出现过，修改状态
            groupNews1.setStatus("0");
            dao.updateGroupNews(groupNews1);
        }
    }
    //删除好友
    public static void deleteFriend(Event event, Socket socket) {
        Friend friend =(Friend) event.get();
        Friend friend1=dao.findFriendById(friend.getUserId(), friend.getFriendId());
        dao.deleteFriend(friend1.getId());
        dao.deleteAllFriendInformation(friend.getFriendId(),friend.getUserId());
        ArrayList<Friend> friends=dao.findFriendById(friend1.getUserId());
        event.setMesType(MessageType.MESSAGE_DELETE_FRIEND_SUCCEED);
        event.set(friends);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //查看消息记录
    public static void watchNews(Event event,Socket socket) {
        User user=(User) event.get();
        ArrayList<News> news =dao.findNewsByUserId(user.getUserId());
        ArrayList<News> news2=new ArrayList<>();
        for (News news1 : news) {
            if (news1.getStatus().equals("1")!=true) {
                news2.add(news1);
            }
        }
        if(news2.isEmpty() != true){
            event.set(news2);
            event.setMesType(MessageType.MESSAGE_SEEK_SUCCEED);
        }else{
            event.setMesType(MessageType.MESSAGE_SEEK_FAIL);
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //群聊
    public static void watchGroupNews(Event event, Socket socket) {
        User user=(User) event.get();
        ArrayList<GroupNews> news =dao.findAllGroupNews(user.getUserId());
        ArrayList<GroupNews> news2=new ArrayList<>();
        for (GroupNews news1 : news) {
            if (news1.getStatus().equals("1")!=true) {
                news2.add(news1);
            }
        }
        if(news2.isEmpty() != true){
            event.set(news2);
            event.setMesType(MessageType.MESSAGE_SEEK_GROUP_SUCCEED);
        }else{
            event.setMesType(MessageType.MESSAGE_SEEK_GROUP_FAIL);
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //修改好友消息,添加好友
    public static void updateNews(Event event) {
        News news = (News) event.get();
        news.setStatus("1");
        dao.updateNews(news);//修改消息
        Friend friend=new Friend();
        if(event.getMesType().equals(MessageType.MESSAGE_REQUEST_SUCCEED)){//添加好友
            friend =dao.findFriendById(news.getSend(),news.getReceive());//获取好友
            friend.setStatus("2");//成为好友
            dao.updateFriend(friend);
        }else if(event.getMesType().equals(MessageType.MESSAGE_REQUEST_FAIL)){//对方拒绝添加好友
            friend =dao.findFriendById(news.getSend(),news.getReceive());//获取好友,删除好友消息
            dao.deleteFriend(friend.getId());
            dao.deleteNews(news.getSend(),news.getReceive());
        }
    }
    //添加群聊
    public static void updateGroupNews(Event event) {
        GroupNews groupNews = (GroupNews) event.get();
        groupNews.setStatus("1");
        dao.updateGroupNews(groupNews);//修改消息
        if(event.getMesType().equals(MessageType.MESSAGE_REQUEST_GROUP_SUCCEED)){//添加群聊
            Room room =dao.findRoomById(groupNews.getGroupId());//获取群聊的消息
            Room room1 =room;
            room1.setUserId(groupNews.getSend());
            room1.setStatus("0");
            dao.insertGroup(room1);
        }
    }
    //查看所有好友
    public static void findAllFriend(Event event, Socket socket) {
        User user = (User) event.get();
        ArrayList<Friend> friend=dao.findFriendById(user.getUserId());
        if(friend!=null) {
            event.set(friend);
            event.setMesType(MessageType.MESSAGE_FIND_FRIEND_SUCCEED);
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //判断好友是否在线
    public static void findFriendOnLine(Event event, Socket socket) {
        User user = (User) event.get();
        ArrayList<Friend> friend=dao.findFriendById(user.getUserId());
        ArrayList<User> users =new ArrayList<>();
        for(Friend friend1 :friend){
           if(friend1.getUserId().equals(user.getUserId())){
               User user1=dao.findUserById(friend1.getFriendId());
               users.add(user1);
           }else if(friend1.getUserId().equals(user.getUserId())!=true){
               User user1=dao.findUserById(friend1.getUserId());
               users.add(user1);
           }
        }
        event.set(users);
        event.setMesType(MessageType.MESSAGE_FIND_ONLINE_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //查看所有的群
    public static void findAllGroup(Event event, Socket socket) {
        User user = (User) event.get();
        ArrayList<Room> rooms=dao.findAllRoom(user.getUserId());
        if(rooms!=null)
            event.setMesType(MessageType.MESSAGE_FIND_GROUP_SUCCEED);
        event.set(rooms);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //验证码的时效性
    public static void checkOnTime(Event event, Socket socket) {
        Identify identify = (Identify) event.get();
        Identify identify1 = EmailTimeThread.getYzm(identify.getEmail());
        if(identify.getContent().equals(identify1.getContent())&& identify.getTime()- identify1.getTime()<=300000){
            event.setMesType(MessageType.MESSAGE_ON_TIME_SUCCEED);
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //查看好友所有的消息
    public static void findAllInformation(Event event, Socket socket) {
        Information information =(Information) event.get();
        ArrayList<Information> information1 =dao.findAllInformation(information.getSend(),information.getReceive());
        event.setMesType(MessageType.MESSAGE_FIND_INFORMATION_SUCCEED);
        event.set(information1);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //群聊
    public static void findAllGroupInformation(Event event, Socket socket) {
        Information information =(Information) event.get();
        ArrayList<Information> information1 =dao.findAllGroupInformation(information.getReceive());
        event.setMesType(MessageType.MESSAGE_FIND_GROUP_INFORMATION_SUCCEED);
        event.set(information1);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //分片加载数据
    public static void downDocument(Event event) {
        Information information=(Information) event.get();
        PictureShow.sendDocument(information.getBytes(),information.getSend()+"_"+information.getCount());
    }
    //发送消息
    public static void sendMessage(Event event) throws IOException {
        Information information =(Information) event.get();
        System.out.println(information);
        if(information.getStatus().equals("0")) {
            dao.insertInformation(information);
        }
        if(information.getStatus().equals("1")){
            char ch='\\';
            int i =information.getContent().lastIndexOf(ch);
            String img= information.getContent().substring(i+1,information.getContent().length()-4);
            System.out.println(img);
            PictureShow.send(information.getBytes(),information.getSend()+"_"+img);
            information.setContent(information.getSend()+'_'+img);
            dao.insertInformation(information);
        }
        if(information.getStatus().equals("2")){//把分片和起来
            char ch='\\';
            int i =information.getContent().lastIndexOf(ch);
            String img= information.getContent().substring(i+1);
            PictureShow.setDocument(information.getSend(),information.getCount(),img);
            information.setContent(img);
            dao.insertInformation(information);
        }
        //好友
        if(dao.findUserById(information.getReceive()).getId()!=null) {
            try {
                ServiceConnectClientThread serviceConnectClientThread = hm.get(information.getReceive());
                if (serviceConnectClientThread != null) {//在线
                    event.set(information);
                    event.setMesType(MessageType.MESSAGE_RECEIVE_INFORMATION);
                    ObjectOutputStream oos = new ObjectOutputStream(serviceConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(event);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {//群聊
            ArrayList<Room> rooms=dao.findAllRoomPeople(information.getSend(),information.getReceive());
            for(Room room:rooms){
                ServiceConnectClientThread serviceConnectClientThread = hm.get(room.getUserId());
                if (serviceConnectClientThread != null) {//在线
                    event.set(information);
                    event.setMesType(MessageType.MESSAGE_RECEIVE_INFORMATION);
                    ObjectOutputStream oos = null;
                    try {
                        oos = new ObjectOutputStream(serviceConnectClientThread.getSocket().getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        oos.writeObject(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    //得到朋友的消息
    public static void checkFriendMessage(Event event, Socket socket) {
        User user=(User) event.get();
        user=dao.findUserById(user.getUserId()) ;
        event.set(user);
        event.setMesType(MessageType.MESSAGE_ID1_SUCCEED);//ID是对的
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //得到群聊的消息
    public static void checkGroupMember(Event event, Socket socket) {
        Room room =(Room) event.get();
        ArrayList<Room> rooms=dao.findAllRoomPeople(room.getId());
        event.set(rooms);
        event.setMesType(MessageType.MESSAGE_FIND_GROUP_MEMBER_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //创建群聊
    public static void creatGroup(Event event, Socket socket) {
        Room room=(Room) event.get();
        String id="";
        id= SnowFlake.start();
        room.setId(id);
        PictureShow.set(room.getBytes(), room.getId());
        room.setChatRoomPic("D:/headPicture/sever/"+room.getId()+".png");
        dao.insertGroup(room);
        event.set(room);
        event.setMesType(MessageType.MESSAGE_CREAT_GROUP_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //创建群聊时拉人
    public static void creatGroupPeople(Event event) {
        ArrayList<Room> rooms =(ArrayList<Room>) event.get();
        System.out.println(rooms);
        for(Room room:rooms){
            Room room1=dao.findRoomPower(room.getId());//群主
            room1.setUserId(room.getUserId());
            room1.setStatus(room.getStatus());
            dao.insertGroup(room1);
        }
    }
    //判断是否是好友id
    public static void isFriend(Event event,Socket socket){
        User user=(User) event.get();
        user=dao.findUserById(user.getUserId()) ;
        if (user.getId() != null) {
            event.setMesType(MessageType.MESSAGE_IS_FRIEND_SUCCEED);//ID是对的
        } else {
            event.setMesType(MessageType.MESSAGE_IS_FRIEND_FAIL);//ID是错的
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //判断是不是正确的id
    public static void idRight(Event event, Socket socket) {
        User user=(User) event.get();
        User use=dao.findUserById(user.getUserId()) ;
        if (use.getId() != null) {
            event.set(use);
            event.setMesType(MessageType.MESSAGE_ID2_SUCCEED);//ID是对的
        } else {
            Room room = dao.findRoomById(user.getUserId());
            if (room.getId() != null) {
                event.set(room);
                event.setMesType(MessageType.MESSAGE_ID2_GROUP_SUCCEED);//ID是对的
            }
            else
            event.setMesType(MessageType.MESSAGE_ID2_FAIL);//ID是错的
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //退出群聊
    public static void outGroup(Event event) {
        Room room =(Room) event.get();
        dao.outRoom(room.getId(),room.getUserId());
    }
    //删除群聊
    public static void deleteGroup(Event event) {
        Room room=(Room) event.get();
        dao.deleteRoom(room.getId());
        dao.deleteGroupInformation(room.getId());
        dao.deleteAllGroupNews(room.getId());
    }
    //删人
    public static void deleteGroupPeople(Event event, Socket socket) {
        Room room = (Room) event.get();
        dao.outRoom(room.getId(),room.getUserId());
        event.setMesType(MessageType.MESSAGE_DELETE_GROUP_PEOPLE_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //设置管理员
    public static void setGroupManage(Event event, Socket socket) {
        Room room = (Room) event.get();
        Room room1=dao.findRoomByUserId(room.getUserId(),room.getId());
        room1.setStatus(room.getStatus());
        dao.updateRoom(room1);
        event.setMesType(MessageType.MESSAGE_SET_GROUP_MANAGE_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //撤销管理员
    public static void deleteGroupManage(Event event, Socket socket) {
        Room room = (Room) event.get();
        Room room1=dao.findRoomByUserId(room.getUserId(),room.getId());
        room1.setStatus(room.getStatus());
        dao.updateRoom(room1);
        event.setMesType(MessageType.MESSAGE_DELETE_GROUP_MANAGE_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //转让群主
    public static void changeGroupPower(Event event, Socket socket) {
        Room room =(Room) event.get();
        Room room1=dao.findRoomPower(room.getId());//群主
        room1.setStatus("1");
        dao.updateRoom(room1);
        room=dao.findRoomByUserId(room.getUserId(),room.getId());
        room.setStatus("2");
        dao.updateRoom(room);
        event.setMesType(MessageType.MESSAGE_CHANGE_GROUP_POWER_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //查看一个人所有的常用语
    public static void checkComment(Event event, Socket socket) {
        Common common =(Common) event.get();
        ArrayList<Common> u=dao.findAllUsed(common.getUserId());
        event.set(u);
        event.setMesType(MessageType.MESSAGE_CHECK_COMMENT_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //添加常用语
    public static void addComment(Event event, Socket socket) {
        Common common =(Common) event.get();
        dao.insertUsed(common);
        event.set(common);
        event.setMesType(MessageType.MESSAGE_ADD_COMMENT_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //将文件分片下载到客户端
    public static void fileDown(Event event, Socket socket) throws IOException {
        Information information=(Information) event.get();
        File file=new File("D:\\headPicture\\content\\"+information.getSend()+"_"+information.getContent());
        BufferedInputStream in=new BufferedInputStream(new FileInputStream(file));
        byte[] bytes=new byte[1024*1024];
        int total = 0;
        while(in.read(bytes)!=-1){
           information.setCount(total);
           information.setBytes(bytes);
           event.set(information);
           event.setMesType(MessageType.MESSAGE_DOWN_DOCUMENT_SUCCEED);
           ObjectOutputStream oos= null;
           try {
                oos = new ObjectOutputStream(socket.getOutputStream());
           } catch (IOException e) {
               e.printStackTrace();
           }
           try {
                oos.writeObject(event);
           } catch (IOException e) {
                e.printStackTrace();
           }
           total++;
        }
        in.close();
    }
    //模糊查找
    public static void checkAll(Event event, Socket socket) {
        Seek seek =(Seek) event.get();
        ArrayList<Seek> seeks=dao.checkAll(seek);
        System.out.println(seeks);
        event.set(seeks);
        event.setMesType(MessageType.MESSAGE_SEEK_LIKE_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //找到群聊的消息
    public static void findGroupNews(Event event, Socket socket) {
        Room  room = (Room) event.get();
        room=dao.findRoomNews(room.getId());
        System.out.println(room);
        event.set(room);
        event.setMesType(MessageType.MESSAGE_SEEK_GROUP_NEWS_SUCCEED);
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
