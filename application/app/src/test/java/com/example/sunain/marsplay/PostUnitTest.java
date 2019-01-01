package com.example.sunain.marsplay;

import com.example.sunain.marsplay.Model.Post;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PostUnitTest {

    public static final String KEY_TEST_STRING = "IfuvAsd";
    public static final String AUTH_TEST_STRING = "Ifuvdvd";
    public static final String TITLE_TEST_STRING = "Ifuvnfr";
    public static final String CONTENT_TEST_STRING = "dvsdgsIfuv";
    public static List<String> LIST_TEST;

    private Post p;
    @Before
    public void createLogHistory() {
        LIST_TEST=new ArrayList<>();
        LIST_TEST.add("item 1");
        LIST_TEST.add("item 2");
        p=new Post();
        p.setKey(KEY_TEST_STRING);
        p.setAuth(AUTH_TEST_STRING);
        p.setTitle(TITLE_TEST_STRING);
        p.setContent(CONTENT_TEST_STRING);
        p.setUriList(LIST_TEST);
    }

    @Test
    public void key_isCorrect()
    {
        assertEquals(p.getKey(),KEY_TEST_STRING);
    }

    @Test
    public void auth_isCorrect()
    {
        assertEquals(p.getAuth(),AUTH_TEST_STRING);
    }

    @Test
    public void title_isCorrect()
    {
        assertEquals(p.getTitle(),TITLE_TEST_STRING);
    }

    @Test
    public void content_isCorrect()
    {
        assertEquals(p.getContent(),CONTENT_TEST_STRING);
    }

    @Test
    public void list_isCorrect()
    {
        assertEquals(p.getUriList(),LIST_TEST);
    }

}
