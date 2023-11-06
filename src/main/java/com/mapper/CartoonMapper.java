package com.mapper;


import com.pojo.Cartoon;
import com.pojo.MyJob;
import com.utils.StringToArrayTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartoonMapper {

//   添加进cartoon表
@Insert("INSERT INTO cartoons (my_name, user_id, birth, ad, dis_ad, pre_me, post_me,user_job,area) VALUES (#{myName}, #{userId}, #{birth}, '${ad[0]}', '${disAd[0]}', #{preMe}, #{postMe},#{userJob},#{area})")
int addCartoon(Cartoon cartoon);


//     添加进my_job表
     @Insert("Insert into my_job (user_id,job_name,des,picture,isPost)values(#{userId},#{jobName},#{des},#{picture},#{isPost})")
     int addMyJob(MyJob myJob);


//     通过userid去分别获取

     @Select("SELECT * FROM cartoons where user_id  = #{userId}")
     @Results({
             @Result(property = "ad", column = "ad", typeHandler = StringToArrayTypeHandler.class),
             @Result(property = "disAd", column = "dis_ad", typeHandler = StringToArrayTypeHandler.class)
     })
     Cartoon getCartoon(Integer userId);

     @Select("SELECT * FROM my_job WHERE user_id = #{userId}")
     List<MyJob> getJobList(Integer userId);

@Delete("Delete from cartoons where user_id = #{userId}")
     int deleteCartoon(Integer userId);

@Delete("Delete from my_job where user_id = #{userId}")
     int deleteJobList(Integer userId);
}
