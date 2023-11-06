package com.mapper;

import com.pojo.FriendSettings;
import com.pojo.Images;
import com.pojo.Room;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RoomMapper {
    //    判断有没有重复的邀请码
    @Select("select * from room where invitationCode = #{invitationCode}")
    Room isRoomExists(String invitationCode);

    //   创建房间
    @Insert("Insert into room(invitationCode,senderId,detected)values(#{invitationCode},#{senderId},0) ")
    int createRoom(Room room);

    //    通过邀请码修改
    @Update("Update room set receiverId = #{receiverId} where InvitationCode = #{InvitationCode}")
    int updateReceiver(String InvitationCode,Integer receiverId);



    //    通过邀请码获取房间对象
    @Select("select * from room where InvitationCode = #{InvitationCode}")
    Room getRoomByInvitationCode(String InvitationCode);

    //    通过id去获取房间
    @Select("select id, invitationCode, senderId, receiverId, detected, key_words, guess_words from room where id = #{id}")
    Room getRoomById(Integer id);

    //   更新房间
    @Update("Update room set senderId = #{senderId},receiverId = #{receiverId} ,detected = #{detected} where id = #{id} ")
    int updateRoom(Room room);

    //加入关键词
    @Update("UPDATE room set character_key_words=#{json} where invitationCode = #{roomCode}")
    void updateCharacterKeyWords(String roomCode, String json);

    @Update("UPDATE room set job_key_words = #{json} where invitationCode = #{roomCode}")
    void updateJobKeyWords(String roomCode,String json);

    @Select("select character_key_words from room where invitationCode = #{roomCode}")
    String selectCharacterKeyWords(String roomCode);

    @Select("select job_key_words from room where invitationCode = #{roomCode}")
    String selectJobKeyWords(String roomCode);

    @Select("select description from keyword_prompt where type = #{type}")
    String selectWordsPrompt(String type);

    //加入所猜的关键词
    @Update("update room set character_guess_words = #{guessJson} where invitationCode = #{roomCode}")
    void updateGuessWords(String roomCode, String guessJson);

    @Update("update room set job_guess_words = #{guessJson} where invitationCode = #{roomCode}")
    void updateJobGuessWords(String roomCode,String guessJson);

    //保存画板
    @Insert("Insert into images (user_id,whiteboard,from_id)values(#{userId},#{whiteboard},#{fromId}) ")
    int saveBoard(Images board64);

    @Insert("Insert into images (user_id,whiteboard,from_id,character_keywords,from_name,job_keywords)values(#{userId},#{whiteboard},#{fromId},#{characterKeywords},#{fromName},#{jobKeywords})")
    int saveImage(Images image);

//    保存关键词
    @Update("update friend_settings set counts = counts + 1 where content = #{keyword}")
    int updateKeywords(String keyword);

    @Insert("Insert into friend_settings (user_id,content)values(#{userId},#{keyword})")
    int addKeyword(Integer userId,String keyword);

//通过关键词获取
    @Select("SELECT * FROM friend_settings where content = #{keyword}")
    FriendSettings getKeyword(String keyword);

}