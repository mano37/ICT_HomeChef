package com.homechef.ict.ict_homechef;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class JsonUtilTest {

    private JsonUtil ut;
    @Before
    public void setUp() throws Exception {

        String[] sArr = {"131313131", "google"};

        System.out.println(sArr[0] + sArr[1]);

        ut = new JsonUtil(":80/auth/login",sArr);



    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getJson() {

        System.out.println(ut.getJson().toString());

    }

}