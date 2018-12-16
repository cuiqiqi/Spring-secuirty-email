package cn.com.taiji.service;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class EmailCodeService {
    private String code;

}
