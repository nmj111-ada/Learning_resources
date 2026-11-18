package com.hmdp;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserLoginTests {
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void loginWritesStringHashValuesAndSkipsNullAndPrivateFields() {
        StringRedisTemplate redis = mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = mock(ValueOperations.class);
        HashOperations<String, Object, Object> hash = mock(HashOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        when(redis.opsForHash()).thenReturn(hash);
        when(values.get("login:code:13800138000")).thenReturn("123456");

        UserServiceImpl service = spy(new UserServiceImpl());
        ReflectionTestUtils.setField(service, "stringRedisTemplate", redis);
        QueryChainWrapper<User> query = mock(QueryChainWrapper.class);
        doReturn(query).when(service).query();
        when(query.eq("phone", "13800138000")).thenReturn(query);
        User user = new User().setId(1001L).setNickName("小明")
                .setPhone("13800138000").setPassword("private").setIcon(null);
        when(query.one()).thenReturn(user);

        //使用真实字符串序列化器，检查登录流程写入的值能否被Redis模板序列化
        RedisSerializer serializer = new StringRedisTemplate().getHashValueSerializer();
        doAnswer(invocation -> {
            Map<String, Object> userMap = invocation.getArgument(1);
            userMap.values().forEach(serializer::serialize);
            assertEquals("1001", userMap.get("id"));
            assertEquals("小明", userMap.get("nickName"));
            assertFalse(userMap.containsKey("icon"));
            assertFalse(userMap.containsKey("phone"));
            assertFalse(userMap.containsKey("password"));
            return null;
        }).when(hash).putAll(startsWith("login:token:"), anyMap());

        LoginFormDTO form = new LoginFormDTO();
        form.setPhone("13800138000");
        form.setCode("123456");
        Result result = service.login(form, null);
        assertTrue(result.getSuccess());
        assertNotNull(result.getData());
        verify(hash).putAll(eq("login:token:" + result.getData()), anyMap());
    }
}
