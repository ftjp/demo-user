package com.example.demo.auth;

/**
 * @author LJP
 * @date 2024/11/28 10:29
 */
public interface ResidConstanst {

    /**
     * accessToken:userName:access_token  -- 值为 userName
     */
    String ACCESS_TOKEN_PREFIX = "accessToken:userName:";
    /**
     * access_to_refresh:access_token  -- 值为 refresh_token
     */
    String ACCESS_TO_REFRESH_PREFIX = "access_to_refresh:";



    String USER_DETAILS_PREFIX = "userName:userDetails:";
    String USER_CONTEXT_PREFIX = "userName:UserInfoContext:";


}
