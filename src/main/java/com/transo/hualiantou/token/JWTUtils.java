package com.transo.hualiantou.token;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transo.hualiantou.utils.IPaddress;
import com.transo.hualiantou.utils.JsonUtil;
import io.jsonwebtoken.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JWT工具
 */
public class JWTUtils {
    //服务器的key 用于做加解密的key数据，如果可以使用客服端生成的key，当前定义可以不使用
    private static final String JWT_SECERT = "test_jwt_secert";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int JWT_ERRCODE_EXPIRE = 1005;//Token过期
    private static final int JWT_ERRCODE_FAIL = 1006;//验证不通过


    public static SecretKey generalKey() {
        try {
            //byte[] encodedKey = Base64.decode(JWT_SECERT);
            byte[] encodedKey = JWT_SECERT.getBytes("utf-8");
            SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 签发JWT，创建token的方法
     *
     * @param id        jwt 唯一的身份标识，主要用来作为一次性token
     * @param iss       jwt签发者
     * @param subject   jwt所面向的用户，payload中记录public claims  （用户）
     * @param ttlMillis 有效期，单位毫秒
     * @return token 一次性的 是为一个用户的有效登陆周期准备的一个token，用户退出 超时 token 失效
     * @throws Exception
     */
    public static String createJWT(String id, String iss, String subject, long ttlMillis) {
        //加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //当前时间
        long nowMillis = System.currentTimeMillis();
        //当前时间的日期格式
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();
        //创建JWT的构建器，就是使用指定的信息和加密算法，生成token的工具

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuer(iss)
                .setId(id)//设置身份标识（唯一）可以使用主键id/客户端ip/服务器生成的随机数据
                .setIssuedAt(now) //token 生成的时间
                .signWith(signatureAlgorithm, secretKey); //设定密匙和算法

        if (ttlMillis >= 0) {//有效期
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);//失效时间
            builder.setExpiration(expDate);
        }
        return builder.compact();//生成token
    }

    /**
     * 验证JWT
     *
     * @param jwtStr
     * @return
     */
    public static JWTResult validateJWT(String jwtStr, String IP) {

        JWTResult checkResult = new JWTResult();
        Claims claims = null;
        try {
            claims = parseJWT(jwtStr);
            if (claims.get("jti").equals(IP)) {
                checkResult.setSuccess(true);
                checkResult.setClaims(claims);
            } else {
                checkResult.setSuccess(false);
            }
        } catch (ExpiredJwtException e) {//超时
            checkResult.setErrCode(JWT_ERRCODE_EXPIRE);
            checkResult.setSuccess(false);
            e.printStackTrace();
        } catch (SignatureException e) {//失败
            checkResult.setErrCode(JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
            e.printStackTrace();
        } catch (Exception e) {
            checkResult.setErrCode(JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
            e.printStackTrace();

        }

        return checkResult;
    }

    /**
     * 解析JWT字符串
     *
     * @param jwtStr 就是token
     * @return
     * @throws Exception
     */
    private static Claims parseJWT(String jwtStr) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtStr)
                .getBody();//token中记录的payload数据（claims）
    }


    //解析Token
    public static UserSubject getUserSubject(String token){
        UserSubject userSubject = JsonUtil.jsonToPojo(parseJWT(token).getSubject(), UserSubject.class);
        return userSubject;
    }



    public static void main(String[] ages){
        UserSubject u=getUserSubject("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEzLFwicGhvbmVcIjpcIjMzMzMzMzMzMzMxXCIsXCJtZW1iZXJcIjpcIjBcIixcImNvZGVcIjpcImRjNjMwNjI1LTBmYmUtNDMxYS05ZDg5LWU4NGQ1NTVkMzkwZFwiLFwiYXV0aG9yaXR5XCI6bnVsbCxcInVzX2F1ZGl0aW5nX3R5cGVcIjpcIjBcIn0iLCJpc3MiOiJsb2dpbiIsImp0aSI6IjE5Mi4xNjguMTAyLjI1MSIsImlhdCI6MTU2NjI4NDY5MiwiZXhwIjoxNTY2Mjg2NDkyfQ.Gc-WWdweJlWYBayIVgIIocBPOeG_g4L3jFHI4o3FJgY");
        System.out.println(u.toString());
    }


    /**
     * 生成Subject信息
     *
     * @param subObj 要转换的对象
     * @return java对象-》json字符串出错时返回null
     */
    public static String generalSubject(Object subObj) {
        try {
            return MAPPER.writeValueAsString(subObj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    //验证Token
    public static boolean verifyToken(String token, HttpServletRequest request,RedisTemplate<String, String> redisTemplate) {
        try {
            if (!StringUtils.isEmpty(token)) {

                JWTResult jwtResult = validateJWT(token, IPaddress.getIpAddress(request));
                if (jwtResult.isSuccess()) {
                    String result=redisTemplate.boundValueOps(token).get();
                    //System.out.println(result);
                    if(!StringUtils.isEmpty(result)){
                        redisTemplate.boundValueOps(token).set("1",2, TimeUnit.HOURS);
                        return true;
                    }

                }
            }
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
