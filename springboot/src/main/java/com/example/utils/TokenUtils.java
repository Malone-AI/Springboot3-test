package com.example.utils;


import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class TokenUtils {
    /**
     * 生成Token
     */
    public static String createToken(String data, String sign) {
        return JWT.create().withAudience(data) // 将userId-role 保存到 token 里面作为载荷
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1)) // 1 天后 token 过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥，
    }
}
