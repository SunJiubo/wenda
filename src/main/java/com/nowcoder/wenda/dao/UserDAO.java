package com.nowcoder.wenda.dao;

import com.nowcoder.wenda.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Component
@Mapper
public interface UserDAO {
    // 注意空格
    String TABLE_NAME = " wenda.user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

//    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
//            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(@Param("user")User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where name=#{name}"})
    User selectByName(String name);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Update({"update ", TABLE_NAME, " set salt=#{salt} where id=#{id}"})
    void updateSalt(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);
}
