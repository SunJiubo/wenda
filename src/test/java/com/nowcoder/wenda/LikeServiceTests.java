package com.nowcoder.wenda;

import com.nowcoder.wenda.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by nowcoder on 2016/8/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WendaApplication.class)
public class LikeServiceTests {
    @Autowired
    LikeService likeService;

    @Before
    public void setUp() {
       System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");
    }

    @Test
    public void testLike() {
        System.out.println("testLike");
        likeService.like(123, 1, 1);
        Assert.assertEquals(1, likeService.getLikeStatus(123, 1, 1));

        likeService.dislike(123, 1, 1);
        Assert.assertEquals(-1, likeService.getLikeStatus(123, 1, 1));
    }

    @Test
    public void testXXX() {
        System.out.println("testXXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        System.out.println("testException");
        throw new IllegalArgumentException("异常发生了");
    }
}