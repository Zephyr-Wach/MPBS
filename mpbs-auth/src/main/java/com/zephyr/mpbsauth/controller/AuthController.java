package com.zephyr.mpbsauth.controller;

import com.zephyr.mpbscommon.utils.JwtUtil;
import com.zephyr.mpbscommon.utils.Result;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/refresh")
    public Result refreshAccessToken(@RequestBody Map<String, String> map) {
        String refreshToken = map.get("refreshToken");

        if (!JwtUtil.validateToken(refreshToken)) {
            return Result.failure(401, "RefreshToken 已过期，请重新登录");
        }

        Claims claims = JwtUtil.parseToken(refreshToken);
        String userId = claims.getSubject();
        String userPermission = claims.get("permission", String.class); // 读取权限

        String newAccessToken = JwtUtil.generateToken(userId, userPermission); // 用原权限生成新 token
        String newRefreshToken = JwtUtil.generateRefreshToken(userId, userPermission);

        return Result.success(new HashMap<String, Object>() {{
            put("accessToken", newAccessToken);
            put("refreshToken", newRefreshToken);
        }});
    }
}




