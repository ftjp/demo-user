package com.example.demo.infruastructure.common.UserContext;

/**
 * @author LJP
 * @date 2024/11/29 9:37
 */
public class LocalUserContext {

    public static final ThreadLocal<UserInfoContext> userInfoThreadLocal = new ThreadLocal<>();

    public static void setUserInfo(UserInfoContext userInfo) {
        if (userInfoThreadLocal.get() != null) {
            removeUserInfo();
        }
        userInfoThreadLocal.set(userInfo);
    }

    public static UserInfoContext getUserInfo() {
        return userInfoThreadLocal.get();
    }

    public static void removeUserInfo() {
        userInfoThreadLocal.remove();
    }


}
