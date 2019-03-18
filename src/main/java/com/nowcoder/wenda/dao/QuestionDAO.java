package com.nowcoder.wenda.dao;

import com.nowcoder.wenda.model.Question;
import com.nowcoder.wenda.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface QuestionDAO {
    //注意空格
    String TABLE_NAME = "  question  ";
    String INSERT_FIELDS = " title,content,created_date,user_id,comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into" , TABLE_NAME , " ( ",INSERT_FIELDS ,
            " ) values(#{title},#{content},#{createdDate},#{userId},#{commentCount}) "})
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offest,
                                         @Param("limit") int limit);

}
