package com.hmdp;

import com.hmdp.config.MvcConfig;
import com.hmdp.utils.UserHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoginInterceptorTests {
    private AnnotationConfigWebApplicationContext context;
    private MockMvc mvc;
    private StringRedisTemplate redis;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(new MockServletContext());
        context.register(TestConfig.class);
        context.refresh();
        redis = context.getBean(StringRedisTemplate.class);
        HashOperations<String, Object, Object> hash = mock(HashOperations.class);
        when(redis.opsForHash()).thenReturn(hash);
        when(hash.entries("login:token:valid-token")).thenReturn(
                Map.of("id", "1001", "nickName", "小明", "icon", ""));
        when(hash.entries("login:token:expired-token")).thenReturn(Collections.emptyMap());
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
        UserHolder.removeUser();
        context.close();
    }

    @Test
    void publicRequestWithTokenLoadsUserAndRefreshesLogin() throws Exception {
        mvc.perform(get("/shop/1").header("authorization", "valid-token"))
                .andExpect(status().isOk()).andExpect(content().string("1001"));
        verify(redis).expire("login:token:valid-token", 30, TimeUnit.MINUTES);
        assertNull(UserHolder.getUser());
    }

    @Test
    void protectedRequestWithTokenLoadsUserBeforeLoginCheck() throws Exception {
        mvc.perform(get("/user/me").header("authorization", "valid-token"))
                .andExpect(status().isOk()).andExpect(content().string("1001"));
        verify(redis).expire("login:token:valid-token", 30, TimeUnit.MINUTES);
        assertNull(UserHolder.getUser());
    }

    @Test
    void publicRequestWithoutTokenIsAllowed() throws Exception {
        mvc.perform(get("/shop/1")).andExpect(status().isOk())
                .andExpect(content().string("anonymous"));
        verify(redis, never()).expire(anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    void protectedRequestWithoutTokenIsRejected() throws Exception {
        mvc.perform(get("/user/me")).andExpect(status().isUnauthorized());
        assertNull(UserHolder.getUser());
    }

    @Test
    void publicRequestWithExpiredTokenIsAllowedWithoutRenewal() throws Exception {
        mvc.perform(get("/shop/1").header("authorization", "expired-token"))
                .andExpect(status().isOk()).andExpect(content().string("anonymous"));
        verify(redis, never()).expire(anyString(), anyLong(), any(TimeUnit.class));
        assertNull(UserHolder.getUser());
    }

    @Test
    void protectedRequestWithExpiredTokenIsRejected() throws Exception {
        mvc.perform(get("/user/me").header("authorization", "expired-token"))
                .andExpect(status().isUnauthorized());
        verify(redis, never()).expire(anyString(), anyLong(), any(TimeUnit.class));
        assertNull(UserHolder.getUser());
    }

    @Test
    void nextAnonymousRequestDoesNotInheritPreviousUser() throws Exception {
        mvc.perform(get("/user/me").header("authorization", "valid-token"))
                .andExpect(status().isOk());
        mvc.perform(get("/user/me")).andExpect(status().isUnauthorized());
    }

    @Configuration
    @EnableWebMvc
    @Import(MvcConfig.class)
    static class TestConfig {
        @Bean
        StringRedisTemplate stringRedisTemplate() {
            return mock(StringRedisTemplate.class);
        }

        @Bean
        TestController testController() {
            return new TestController();
        }
    }

    @RestController
    static class TestController {
        @GetMapping({"/shop/1", "/user/me"})
        String currentUser() {
            return UserHolder.getUser() == null ? "anonymous" : UserHolder.getUser().getId().toString();
        }
    }
}
