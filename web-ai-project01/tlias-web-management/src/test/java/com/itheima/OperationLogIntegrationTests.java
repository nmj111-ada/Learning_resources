package com.itheima;

import com.itheima.util.CurrentHolder;
import com.itheima.util.JwtUtils;
import com.itheima.util.LogValueUtils;
import com.itheima.filter.TokenFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/** 使用隔离的 H2 数据库验证真实 HTTP 链路，不依赖也不改动本机 MySQL 数据。 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:operationlog;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa", "spring.datasource.password=",
        "tlias.jwt.secret=integration-test-only-key-32-bytes-long",
        "mybatis.configuration.map-underscore-to-camel-case=true",
        "mybatis.configuration.log-impl=org.apache.ibatis.logging.nologging.NoLoggingImpl"
})
class OperationLogIntegrationTests {
    @LocalServerPort private int port;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private ObjectMapper json;
    @Autowired private JwtUtils jwt;
    @Autowired private TokenFilter filter;
    @Autowired private LogValueUtils logValues;
    private final HttpClient http = HttpClient.newHttpClient();

    // 每个用例建立最小表结构，保证测试可以独立重复执行。
    @BeforeEach
    void prepareDatabase() {
        jdbc.execute("drop table if exists operate_log");
        jdbc.execute("drop table if exists dept");
        jdbc.execute("drop table if exists emp");
        jdbc.execute("create table emp(id int primary key, username varchar(100), name varchar(100), password varchar(100), dept_id int)");
        jdbc.execute("create table dept(id int auto_increment primary key, name varchar(100), create_time timestamp, update_time timestamp)");
        jdbc.execute("create table operate_log(id int auto_increment primary key, operate_emp_id int, operate_time timestamp, class_name varchar(255), method_name varchar(100), method_params varchar(2000), return_value varchar(2000), cost_time bigint)");
        jdbc.update("insert into emp(id, username, name, password) values(7, 'learner', '学习用户', 'test-only-password')");
    }

    // 验证登录签发令牌、真实过滤器放行、AOP 落库、分页接口和前端字段名。
    @Test
    void loginThenWriteAndReadLog() throws Exception {
        var login = request("POST", "/login", null, "{\"username\":\"learner\",\"password\":\"test-only-password\"}");
        assertEquals(200, login.statusCode());
        String token = json.readTree(login.body()).path("data").path("token").asText();
        assertEquals(7, jwt.parseEmployeeId(token));
        assertEquals(0, jdbc.queryForObject("select count(*) from operate_log", Integer.class));
        var create = request("POST", "/depts", token, "{\"name\":\"联调部门\"}");
        assertEquals(1, json.readTree(create.body()).path("code").asInt());
        var page = request("GET", "/log/page", token, null);
        JsonNode data = json.readTree(page.body()).path("data");
        assertEquals(1, data.path("total").asInt());
        JsonNode row = data.path("rows").get(0);
        assertEquals(7, row.path("operateEmpId").asInt());
        assertEquals("学习用户", row.path("operateEmpName").asText());
        assertEquals("com.itheima.controller.DeptController", row.path("className").asText());
        assertEquals("add", row.path("methodName").asText());
        assertTrue(row.path("methodParams").asText().contains("联调部门"));
        assertTrue(row.path("returnValue").asText().contains("code"));
        assertFalse(row.path("operateTime").asText().isBlank());
        assertTrue(row.path("costTime").asLong() >= 0);
        assertEquals(1, jdbc.queryForObject("select count(*) from operate_log", Integer.class));
    }

    // 无令牌、伪造令牌和过期令牌均返回 401，不能调用业务或写日志。
    @Test
    void rejectMissingInvalidAndExpiredToken() throws Exception {
        assertEquals(401, request("GET", "/log/page", null, null).statusCode());
        assertEquals(401, request("POST", "/depts", "invalid", "{\"name\":\"禁止创建\"}").statusCode());
        var key = Keys.hmacShaKeyFor("integration-test-only-key-32-bytes-long".getBytes(StandardCharsets.UTF_8));
        String expired = Jwts.builder().subject("7").expiration(new Date(1)).signWith(key).compact();
        assertEquals(401, request("GET", "/log/page", expired, null).statusCode());
        String forged = Jwts.builder().subject("7").expiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Jwts.SIG.HS256.key().build()).compact();
        assertEquals(401, request("GET", "/log/page", forged, null).statusCode());
        assertEquals(0, jdbc.queryForObject("select count(*) from dept", Integer.class));
        assertEquals(0, jdbc.queryForObject("select count(*) from operate_log", Integer.class));
        var login = request("POST", "/login", null, "{\"username\":\"learner\",\"password\":\"wrong\"}");
        assertEquals(0, json.readTree(login.body()).path("code").asInt());
    }

    // 验证两页数据、倒序、默认分页及已删除员工的历史日志仍然保留。
    @Test
    void paginationAndDeletedEmployee() throws Exception {
        for (int i = 0; i < 12; i++) {
            jdbc.update("insert into operate_log(operate_emp_id, method_name) values(?, ?)", 999, "action" + i);
        }
        String token = jwt.generateToken(7);
        JsonNode first = json.readTree(request("GET", "/log/page", token, null).body()).path("data");
        assertEquals(12, first.path("total").asInt());
        assertEquals(10, first.path("rows").size());
        assertEquals("action11", first.path("rows").get(0).path("methodName").asText());
        assertTrue(first.path("rows").get(0).path("operateEmpName").isNull());
        JsonNode second = json.readTree(request("GET", "/log/page?page=2&pageSize=10", token, null).body()).path("data");
        assertEquals(2, second.path("rows").size());
        assertEquals(0, json.readTree(request("GET", "/log/page?page=0", token, null).body()).path("code").asInt());
    }

    // 即使业务抛异常也清理当前用户，并验证敏感字段不会进入操作日志。
    @Test
    void clearThreadLocalOnFailureAndRedactSecrets() throws Exception {
        var request = new MockHttpServletRequest("GET", "/depts");
        request.setServletPath("/depts");
        request.addHeader("token", jwt.generateToken(7));
        assertThrows(IllegalStateException.class, () -> filter.doFilter(request, new MockHttpServletResponse(), (req, res) -> {
            assertEquals(7, CurrentHolder.getCurrentId());
            throw new IllegalStateException("模拟业务异常");
        }));
        assertNull(CurrentHolder.getCurrentId());
        String logged = logValues.serialize(new Object[]{Map.of("password", "do-not-log-password",
                "nested", Map.of("token", "do-not-log-token"), "phone", "13800000000", "name", "保留姓名")});
        assertFalse(logged.contains("do-not-log"));
        assertFalse(logged.contains("13800000000"));
        assertTrue(logged.contains("保留姓名"));
    }

    // 部门有员工时删除失败，原有业务异常照常返回，且不记为一次成功操作。
    @Test
    void failedOperationDoesNotCreateSuccessLog() throws Exception {
        jdbc.update("insert into dept(id, name) values(3, '有员工部门')");
        jdbc.update("update emp set dept_id = 3 where id = 7");
        var response = request("DELETE", "/depts?id=3", jwt.generateToken(7), null);
        assertEquals(0, json.readTree(response.body()).path("code").asInt());
        assertEquals(1, jdbc.queryForObject("select count(*) from dept where id = 3", Integer.class));
        assertEquals(0, jdbc.queryForObject("select count(*) from operate_log", Integer.class));
    }

    // 使用真实 HTTP 而非绕过 Filter 的直接方法调用，覆盖前后端交互边界。
    private HttpResponse<String> request(String method, String path, String token, String body) throws Exception {
        var builder = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path));
        if (token != null) { builder.header("token", token); }
        if (body != null) { builder.header("Content-Type", "application/json"); }
        builder.method(method, body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
        return http.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
