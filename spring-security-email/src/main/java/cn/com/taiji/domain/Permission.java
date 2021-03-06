package cn.com.taiji.domain;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Permission extends Audited {
    private String id;
    //权限名称
    private String permission_name;
    //权限描述
    private String permission_descritpion;
    //授权链接
    private String url;
    //父节点id
    private String pid;

}
