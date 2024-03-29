package com.ihelpoo.common;

import com.ihelpoo.common.util.Pair;

public interface Constant {
    String OO_USER_COOKIE = "ooid";
    String SELECTED_SCHOOL = "selected-school";
    String IHELPOO_XML_ROOT = "ihelpoo";
    String IMG_STORAGE_ROOT = "http://img.ihelpoo.cn";
    String R_HOST = "42.62.50.238";
    Integer DEFAULT_PAGESIZE = 20;
    Integer DEFAULT_PAGEINDEX = 0;


    Pair<String, Integer> HS_WR = new Pair<String, Integer>("10.6.1.208", 9999);
    Pair<String, Integer> HS_R = new Pair<String, Integer>("10.6.5.68", 9998);
    String REDIS_HOST = "localhost";
    int DEFAULT_PAGE_SIZE = 20;
    int DEFAULT_PAGE_INDEX = 0;
}
