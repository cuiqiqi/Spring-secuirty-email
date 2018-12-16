package cn.com.taiji.dao;


import cn.com.taiji.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserDao {
    User findByUserName(String user_name);
    User findByUserEmail(String email);
    List<User> listAll();
}
