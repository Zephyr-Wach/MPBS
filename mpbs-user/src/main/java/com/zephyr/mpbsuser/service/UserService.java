package com.zephyr.mpbsuser.service;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.LoginDTO;
import com.zephyr.mpbsuser.dto.RegisterDTO;
import com.zephyr.mpbsuser.dto.UpdateInfoDTO;
import com.zephyr.mpbsuser.dto.UpdatePasswordDTO;

public interface UserService {
    Result login( LoginDTO loginDTO);
    Result register(RegisterDTO registerDTO);
    Result checkByUserName(String userName) ;
    Result updatePassword(UpdatePasswordDTO updatePassword);
    Result getUserInfo();
    boolean updateInfo(String userId, UpdateInfoDTO info);
}
