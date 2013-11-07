package com.ihelpoo.api;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * @author: echowdx@gmail.com
 */
public class OoVersionTest {

    @Test
    public void test(){
        expect().body("update.android.versionCode", equalTo("1")).when().get("/versions.json");
    }

}
