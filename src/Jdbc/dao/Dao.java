package Jdbc.dao;

import chat.comment.*;


import java.util.ArrayList;

public interface Dao {
    //查询用户消息
    ArrayList<User> findAll();
    //条件查询，根据用户ID查询用户消息
    User findUserById(String id);
    //条件查询，根据用户邮箱查询用户消息
    User findUserByEmail(String email);
    //增加用户消息
    int insertUser(User i);
    //修改用户消息
    int updateUser(User i);
    //删除用户消息
    int deleteUser(String id);
    //好友
    Friend findFriendById(String userId, String friendId);
    ArrayList<Friend> findFriendById(String id);
    //添加好友
    int addFriend(Friend friend);
    //修改好友消息
    int updateFriend(Friend friend);
    //删除好友
    int deleteFriend(Integer id);
    //好友消息
    ArrayList<News> findAllNews();
    //条件查询
    News findNews(String send,String receive);
    //条件查询，根据用户查询用户消息
    ArrayList<News> findNewsByUserId(String useId);
    //增加好友申请消息
    int insertNews(News news);
    //修改好友申请消息
    int updateNews(News news);
    //删除好友申请消息
    int deleteNews(String send,String receive);
    //群聊消息
    ArrayList<GroupNews> findAllGroupNews(String receive);
    //群聊申请，条件查询
    GroupNews findGroupNews(String send,String gId);
    //插入群聊申请
    int insertGroupNews(GroupNews groupNews);
    //修改群聊申请
    int updateGroupNews(GroupNews groupNews);
    //删除群聊申请
    int deleteAllGroupNews(String groupId);
    //查找所有的聊天消息
    //好友
    ArrayList<Information> findAllInformation(String send,String receive);
    //群聊，只要查询群id
    ArrayList<Information> findAllGroupInformation(String receive);
    //添加聊天的消息
    int insertInformation(Information information);
    //删除群聊的消息
    int deleteGroupInformation(String receive);
    //删除好友聊天记录
    int deleteAllFriendInformation(String send,String receive);

    //群聊
    //创建群聊
    int insertGroup(Room room);
    Room findRoomByUserId(String userId,String id);
    Room findRoomById(String id);
    //群主
    Room findRoomPower(String id);
    //查找群聊
    ArrayList<Room> findAllRoom(String userId);
    Room findRoomNews(String id);
    //查找群聊中的所有人(除了发消息的人)
    ArrayList<Room> findAllRoomPeople(String send,String id);
    //查看群里的所有人
    ArrayList<Room> findAllRoomPeople(String id);
    //退出群聊
    int outRoom(String id,String userId);
    //删除群聊
    int deleteRoom(String id);
    //修改群聊资料
    int updateRoom(Room room);

    //添加常用语
    int insertUsed(Common used);
    //查看常用语
    ArrayList<Common> findAllUsed(String useId);
    //模糊查找
    ArrayList<Seek> checkAll(Seek seek);
}
