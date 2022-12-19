package Jdbc.dao;

import chat.comment.*;
import utils.JDBCUntils;

import java.sql.*;
import java.util.ArrayList;

public class DaoImpl implements Dao {
    //查询用户消息
    @Override
    public ArrayList<User> findAll() {
        ArrayList<User> list = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM users";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String userId = rs.getString("userId");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String name = rs.getString("name");
                String picture = rs.getString("picture");
                String gender = rs.getString("gender");
                Date birthday = rs.getDate("birthday");
                String freedom = rs.getString("freedom");
                String status = rs.getString("status");
                User uer = new User(id, userId, password, email, name, picture, gender, birthday, freedom, status);
                list.add(uer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return list;
    }
    //根据用户ID，查询用户消息
    @Override
    public User findUserById(String userId) {
        User user = new User();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM users WHERE userId='" + userId + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String userId1 = rs.getString("userId");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String name = rs.getString("name");
                String picture = rs.getString("picture");
                String gender = rs.getString("gender");
                Date birthday = rs.getDate("birthday");
                String freedom = rs.getString("freedom");
                String status = rs.getString("status");
                user.setId(id);
                user.setUserId(userId1);
                user.setPassword(password);
                user.setEmail(email);
                user.setName(name);
                user.setPicture(picture);
                user.setGender(gender);
                user.setBirthday(birthday);
                user.setFreedom(freedom);
                user.setStatus(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return user;
    }
    //邮箱查重
    public User findUserByEmail(String email) {
        User user = new User();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM users WHERE email='" + email + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String userid1 = rs.getString("userid");
                String password = rs.getString("password");
                String email1 = rs.getString("email");
                String name = rs.getString("name");
                String picture = rs.getString("picture");
                String gender = rs.getString("gender");
                Date birthday = rs.getDate("birthday");
                String freedom = rs.getString("freedom");
                String status = rs.getString("status");
                user.setId(id);
                user.setUserId(userid1);
                user.setPassword(password);
                user.setEmail(email1);
                user.setName(name);
                user.setPicture(picture);
                user.setGender(gender);
                user.setBirthday(birthday);
                user.setFreedom(freedom);
                user.setStatus(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return user;
    }
    @Override
    public int insertUser(User i) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "INSERT INTO users (userId,password,email,name,picture,gender,birthday,freedom,status)VALUES('" + i.getUserId() + "','" + i.getPassword() + "','" + i.getEmail() + "','" + i.getName() + "','" + i.getPicture() + "','" + i.getGender() + "','" + i.getBirthday() + "','" + i.getFreedom() + "','" + i.getStatus() + "')";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public int updateUser(User i) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "UPDATE users SET id='" + i.getId() + "',userId='" + i.getUserId() + "',password='" + i.getPassword() + "',email='" + i.getEmail() + "',name ='" + i.getName() + "',picture='" + i.getPicture() + "',gender='" + i.getGender() + "',birthday='" + i.getBirthday() + "', freedom='" + i.getFreedom() + "',status='" + i.getStatus() + "' WHERE userId='" + i.getUserId() + "'";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public int deleteUser(String userId) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "DELETE FROM users WHERE userid='" + userId + "'";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    //连接查询，多表查询
    //条件查询，通过用户id和好友id判断是否添加为好友
    @Override
    public Friend findFriendById(String userId, String friendId) {
        Friend friend = new Friend();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM friend WHERE (userId ='" + userId + "' AND friendId = '" + friendId + "')or (userId ='" + friendId + "' AND friendId = '" + userId + "')";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String userId1 = rs.getString("userId");
                String friendId1 = rs.getString("friendId");
                String status = rs.getString("status");
                String friendName = rs.getString("friendName");
                String userName = rs.getString("userName");
                Date createTime = rs.getDate("createTime");
                friend.setId(id);
                friend.setFriendId(friendId1);
                friend.setUserId(userId1);
                friend.setStatus(status);
                friend.setCreateTime(createTime);
                friend.setFriendName(friendName);
                friend.setUserName(userName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return friend;
    }
    @Override
    public ArrayList<Friend> findFriendById(String id) {
        ArrayList<Friend> friend = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM friend WHERE (userId ='" + id + "' )or (friendId = '" + id + "')";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id1 = rs.getInt("id");
                String userId = rs.getString("userId");
                String friendId = rs.getString("friendId");
                String status = rs.getString("status");
                String friendName = rs.getString("friendName");
                String userName = rs.getString("userName");
                Date createTime = rs.getDate("createTime");
                if (status.equals("2")) {
                    Friend friend1 = new Friend(id1, userId, friendId, status, friendName, userName, createTime);
                    friend.add(friend1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return friend;
    }
    @Override
    public int addFriend(Friend friend) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "INSERT INTO friend (userId,friendId,status,friendName,userName,createTime)VALUES('" + friend.getUserId() + "','" + friend.getFriendId() + "','" + friend.getStatus() + "','" + friend.getFriendName() + "','" + friend.getUserName() + "','" + friend.getCreateTime() + "')";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public int updateFriend(Friend friend) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "UPDATE friend SET id='" + friend.getId() + "',userId='" + friend.getUserId() + "'," +
                    "friendId='" + friend.getFriendId() + "',status='" + friend.getStatus() + "'," +
                    "createTime ='" + friend.getCreateTime() + "',friendName='" + friend.getFriendName() + "'," +
                    "userName='" + friend.getUserName() + "' WHERE id='" + friend.getId() + "'";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public int deleteFriend(Integer id) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "DELETE FROM friend WHERE id='" + id + "'";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    //好友申请
    @Override
    public ArrayList<News> findAllNews() {
        ArrayList<News> list = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM news";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String send = rs.getString("send");
                String receive = rs.getString("receive");
                String status = rs.getString("status");
                News news = new News(id, send, receive, status);
                list.add(news);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return list;
    }
    @Override
    public News findNews(String send, String receive) {
        News news = new News();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM news WHERE (send = '" + send + "' AND receive = '" + receive + "')";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String send1 = rs.getString("send");
                String receive1 = rs.getString("receive");
                String status = rs.getString("status");
                news.setSend(send1);
                news.setReceive(receive1);
                news.setId(id);
                news.setStatus(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return news;
    }
    @Override
    public ArrayList<News> findNewsByUserId(String userId) {
        ArrayList<News> list = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM news WHERE receive = '" + userId + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String send1 = rs.getString("send");
                String receive = rs.getString("receive");
                String status = rs.getString("status");
                News news = new News(id, send1, receive, status);
                if (status.equals("2") != true) {
                    list.add(news);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return list;
    }
    @Override
    public int insertNews(News news) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "INSERT INTO news (send,receive,status)VALUES('" + news.getSend() + "','" + news.getReceive() + "','" + news.getStatus() + "')";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public int updateNews(News news) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "UPDATE news SET id='" + news.getId() + "',send='" + news.getSend() + "',receive='" + news.getReceive() + "',status='" + news.getStatus() + "' WHERE id='" + news.getId() + "'";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public int deleteNews(String send, String receive) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "DELETE FROM news WHERE (send ='" + send + "' AND receive = '" + receive + "')";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    //群聊消息,查看这个人的所有群聊申请
    @Override
    public ArrayList<GroupNews> findAllGroupNews(String receive) {
        ArrayList<GroupNews> list = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM groupNews WHERE receive = '" + receive + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String send = rs.getString("send");
                String receive1 = rs.getString("receive");
                String status = rs.getString("status");
                String groupId = rs.getString("groupId");
                GroupNews news = new GroupNews(id, send, receive1, status, groupId);
                if (status.equals("1") != true) {
                    list.add(news);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return list;
    }
    //群聊申请，条件查询，查看这个消息之前有没有存在
    @Override
    public GroupNews findGroupNews(String send, String groupId) {
        GroupNews groupNews = new GroupNews();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM groupNews WHERE (send = '" + send + "' AND groupId = '" + groupId + "')";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String send1 = rs.getString("send");
                String receive = rs.getString("receive");
                String status = rs.getString("status");
                String groupId1 = rs.getString("groupId");
                groupNews.setSend(send1);
                groupNews.setReceive(receive);
                groupNews.setId(id);
                groupNews.setStatus(status);
                groupNews.setGroupId(groupId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return groupNews;
    }
    //插入群聊申请
    @Override
    public int insertGroupNews(GroupNews groupNews) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql1 = "SELECT userId FROM room WHERE ( id='" + groupNews.getGroupId() + "' AND status='" + "2" + "');";
            ResultSet rs1 = stat.executeQuery(sql1);
            while (rs1.next()) {
                String userId = rs1.getString("userId");
                groupNews.setReceive(userId);
            }
            String sql = "INSERT INTO groupNews (send,receive,groupId,status)VALUES('" + groupNews.getSend() + "','" + groupNews.getReceive() + "','" + groupNews.getGroupId() + "','" + groupNews.getStatus() + "')";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    //修改群聊申请
    @Override
    public int updateGroupNews(GroupNews groupNews) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "UPDATE groupNews SET id='" + groupNews.getId() + "',send='" + groupNews.getSend() + "',receive='" + groupNews.getReceive() + "'," +
                    "status='" + groupNews.getStatus() + "',groupId ='" + groupNews.getGroupId() + "' WHERE id='" + groupNews.getId() + "'";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    //删除群聊申请
//    @Override
//    public int deleteGroupNews(String send,String groupId){
//        Connection con=null;
//        Statement stat=null;
//        int rs=0;
//        try {
//            con= JDBCUntils.getConnection();
//            stat = con.createStatement();
//            String sql="DELETE FROM groupNews WHERE (send ='"+send+"' AND groupId = '"+groupId+"')";
//            rs=stat.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            JDBCUntils.close(con,stat);
//        }
//        return rs;
//    }
    //删除群聊
    //删除聊天的消息
    @Override
    public int deleteGroupInformation(String receive) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "DELETE FROM information WHERE receive='" + receive + "'";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public int deleteAllGroupNews(String groupId) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "DELETE FROM groupNews WHERE groupId = '" + groupId + "'";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    //聊天消息
    @Override
    public ArrayList<Information> findAllInformation(String send, String receive) {
        ArrayList<Information> information = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM information WHERE (send = '" + send + "' AND receive = '" + receive + "') OR(send = '" + receive + "' AND receive = '" + send + "')";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String send1 = rs.getString("send");
                String receive1 = rs.getString("receive");
                String content = rs.getString("content");
                String status = rs.getString("status");
                String time = rs.getString("time");
                Information information1 = new Information(id, send1, receive1, content, time, status);
                information.add(information1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return information;
    }
    @Override
    public ArrayList<Information> findAllGroupInformation(String receive) {
        ArrayList<Information> information = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM information WHERE receive = '" + receive + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String send = rs.getString("send");
                String receive1 = rs.getString("receive");
                String content = rs.getString("content");
                String status = rs.getString("status");
                String time = rs.getString("time");
                Information information1 = new Information(id, send, receive1, content, time, status);
                information.add(information1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return information;
    }
    //添加聊天的消息
    @Override
    public int insertInformation(Information information) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "INSERT INTO information (send,receive,content,time,status)VALUES('" + information.getSend() + "','" + information.getReceive() + "','" + information.getContent() + "','" + information.getTime() + "','" + information.getStatus() + "')";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public int deleteAllFriendInformation(String send, String receive) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "DELETE FROM information WHERE(send = '" + send + "' AND receive = '" + receive + "') OR(send = '" + receive + "' AND receive = '" + send + "')";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    //群聊，添加群聊
    @Override
    public int insertGroup(Room room) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "INSERT INTO room (userId,chatRoomPic,createTime,status,name,id)VALUES('" + room.getUserId() + "'," +
                    "'" + room.getChatRoomPic() + "','" + room.getCreateTime() + "','" + room.getStatus() + "'" +
                    ",'" + room.getName() + "','" + room.getId() + "')";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public Room findRoomByUserId(String userId, String id) {
        Room room = new Room();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM room WHERE (id='" + id + "' AND userId='" + userId + "')";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer gId = rs.getInt("gId");
                String userId1 = rs.getString("userId");
                String chatRoomPic = rs.getString("chatRoomPic");
                String name = rs.getString("name");
                String id1 = rs.getString("id");
                String status = rs.getString("status");
                Date createTime = rs.getDate("createTime");
                room.setGid(gId);
                room.setUserId(userId1);
                room.setChatRoomPic(chatRoomPic);
                room.setName(name);
                room.setCreateTime(createTime);
                room.setId(id1);
                room.setStatus(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return room;
    }
    @Override
    public Room findRoomPower(String id) {
        Room room = new Room();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM room WHERE id='" + id + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer gId = rs.getInt("gId");
                String userId1 = rs.getString("userId");
                String chatRoomPic = rs.getString("chatRoomPic");
                String name = rs.getString("name");
                String id1 = rs.getString("id");
                String status = rs.getString("status");
                Date createTime = rs.getDate("createTime");
                if (status.equals("2")) {
                    room.setGid(gId);
                    room.setUserId(userId1);
                    room.setChatRoomPic(chatRoomPic);
                    room.setName(name);
                    room.setCreateTime(createTime);
                    room.setId(id1);
                    room.setStatus(status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return room;
    }
    //查找群聊id
    @Override
    public Room findRoomById(String id) {
        Room room = new Room();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM room WHERE id='" + id + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer gId = rs.getInt("gId");
                String userId = rs.getString("userId");
                String chatRoomPic = rs.getString("chatRoomPic");
                String name = rs.getString("name");
                String id1 = rs.getString("id");
                String status = rs.getString("status");
                Date createTime = rs.getDate("createTime");
                room.setGid(gId);
                room.setUserId(userId);
                room.setChatRoomPic(chatRoomPic);
                room.setName(name);
                room.setCreateTime(createTime);
                room.setId(id1);
                room.setStatus(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return room;
    }
    //查找自己的群聊
    @Override
    public ArrayList<Room> findAllRoom(String userId) {
        ArrayList<Room> list = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM room WHERE userId = '" + userId + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer gId = rs.getInt("gId");
                String userId1 = rs.getString("userId");
                String chatRoomPic = rs.getString("chatRoomPic");
                String name = rs.getString("name");
                String id = rs.getString("id");
                String status = rs.getString("status");
                Date createTime = rs.getDate("createTime");
                Room room = new Room(gId, userId1, chatRoomPic, createTime, status, name, id);
                list.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return list;
    }
    @Override
    public Room findRoomNews(String id) {
        Room room=new Room();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM room WHERE id = '" + id + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                String name = rs.getString("name");
                String status = rs.getString("status");
                Date createTime = rs.getDate("createTime");
                if(status.equals("2")){
                    room.setName(name);
                    room.setCreateTime(createTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return room;
    }
    //发送消息
    @Override
    public ArrayList<Room> findAllRoomPeople(String send, String id) {
        ArrayList<Room> list = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM room WHERE id = '" + id + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer gId = rs.getInt("gId");
                String userId1 = rs.getString("userId");
                String chatRoomPic = rs.getString("chatRoomPic");
                String name = rs.getString("name");
                String id1 = rs.getString("id");
                String status = rs.getString("status");
                Date createTime = rs.getDate("createTime");
                if (userId1.equals(send) != true) {
                    Room room = new Room(gId, userId1, chatRoomPic, createTime, status, name, id1);
                    list.add(room);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return list;
    }
    @Override
    public ArrayList<Room> findAllRoomPeople(String id) {
        ArrayList<Room> list = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM room WHERE id = '" + id + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer gId = rs.getInt("gId");
                String userId1 = rs.getString("userId");
                String chatRoomPic = rs.getString("chatRoomPic");
                String name = rs.getString("name");
                String id1 = rs.getString("id");
                String status = rs.getString("status");
                Date createTime = rs.getDate("createTime");
                Room room = new Room(gId, userId1, chatRoomPic, createTime, status, name, id1);
                list.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return list;
    }
    @Override
    public int outRoom(String id, String userId) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "DELETE FROM room WHERE (id='" + id + "' AND userId='" + userId + "')";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public int deleteRoom(String id) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "DELETE FROM room WHERE id='" + id + "'";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public int updateRoom(Room room) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "UPDATE room SET userId='" + room.getUserId() + "',chatRoomPic = '" + room.getChatRoomPic() + "'" +
                    ",createTime = '" + room.getCreateTime() + "',status='" + room.getStatus() + "'," +
                    "name='" + room.getName() + "',id='" + room.getId() + "' WHERE gid='" + room.getGid() + "'";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    //添加常用语
    @Override
    public int insertUsed(Common common) {
        Connection con = null;
        Statement stat = null;
        int rs = 0;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "INSERT INTO common (userId,comment,count)VALUES('" + common.getUserId() + "','" + common.getComment() + "','"+ common.getCount()+"')";
            rs = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat);
        }
        return rs;
    }
    @Override
    public ArrayList<Common> findAllUsed(String userId) {
        ArrayList<Common> list = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM common WHERE userId = '" + userId + "'";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                Integer id = rs.getInt("Id");
                String userId1 = rs.getString("userId");
                String comment = rs.getString("comment");
                Integer count = rs.getInt("count");
                Common used=new Common(id,userId1,comment,count);
                list.add(used);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return list;
    }
    //模糊查找
    @Override
    public ArrayList<Seek> checkAll(Seek seek){
        ArrayList<Seek> list = new ArrayList<>();
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = JDBCUntils.getConnection();
            stat = con.createStatement();
            String sql = "SELECT * FROM friend WHERE (userId = '" + seek.getUserId() + "' AND friendName like '%"+seek.getContent()+"%') OR" +
                    "(userId = '" + seek.getUserId() + "' AND friendId like '%"+seek.getContent()+"%')";
            rs = stat.executeQuery(sql);//执行sql语句
            while (rs.next()) {
                String friendId = rs.getString("friendId");
                String friendName = rs.getString("friendName");
                Seek seek1=new Seek(seek.getUserId(),friendId,friendName);
                list.add(seek1);
            }
            String sql1 = "SELECT * FROM friend WHERE (friendId = '" + seek.getUserId() + "' AND userName like '%"+seek.getContent()+"%') OR " +
                    "(friendId = '" + seek.getUserId() + "' AND userId like '%"+seek.getContent()+"%')";
            rs = stat.executeQuery(sql1);//执行sql语句
            while (rs.next()) {
                String userId = rs.getString("userId");
                String userName = rs.getString("userName");
                Seek seek1=new Seek(seek.getUserId(),userId,userName);
                list.add(seek1);
            }
            Connection con1 = null;
            Statement stat1 = null;
            ResultSet rs1 = null;
            try {
                con1 = JDBCUntils.getConnection();
                stat1 = con1.createStatement();
                String sql2 = "SELECT * FROM room WHERE (userId = '" + seek.getUserId() + "' AND id='%"+seek.getContent()+"%')OR(" +
                        "userId = '" + seek.getUserId() + "' AND name='%"+seek.getContent()+"%')";
                rs1 = stat1.executeQuery(sql2);//执行sql语句
                while (rs.next()) {
                    String name = rs.getString("name");
                    String id = rs.getString("id");
                    Seek seek1=new Seek(seek.getUserId(),id,name);
                    list.add(seek1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                JDBCUntils.close(con, stat, rs);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUntils.close(con, stat, rs);
        }
        return list;
    }
}
