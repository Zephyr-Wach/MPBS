package com.zephyr.mpbsuser.service;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.LoginDTO;
import com.zephyr.mpbsuser.dto.RegisterDTO;

public interface UserService {
    Result login( LoginDTO loginDTO);
    Result register(RegisterDTO registerDTO);
    Result checkByUserName(String userName) ;
}
