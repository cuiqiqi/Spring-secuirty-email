package cn.com.taiji.dao;


import cn.com.taiji.domain.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface PermissionDao {
    public List<Permission> findAll();
    public List<Permission> findByAdminUserId(String userId);
}
