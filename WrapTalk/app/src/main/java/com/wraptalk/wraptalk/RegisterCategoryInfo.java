package com.wraptalk.wraptalk;

/**
 * Created by jiyoungpark on 15. 11. 4..
 */
public class RegisterCategoryInfo {

    String categoryName;

    static RegisterCategoryInfo g_registerCategoryInfo = new RegisterCategoryInfo();

    private RegisterCategoryInfo() {

    }

    public static RegisterCategoryInfo getInstance() {
        return g_registerCategoryInfo;
    }
}
