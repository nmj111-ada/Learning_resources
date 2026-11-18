package com.hmdp.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 使用 Jackson 的 ObjectMapper 来转 JSON
    private static final ObjectMapper mapper = new ObjectMapper();

    //查询店铺类型
    @Override
    public List<ShopType> queryTypeList() {
        String key = "shopType:list";
        try {
            // 1. 从 Redis 获取：作为单个 String（JSON 数组）取出来
            String jsonStr = stringRedisTemplate.opsForValue().get(key);

            // 2. 判断是否存在：用 StringUtils 判空
            if (StrUtil.isNotBlank(jsonStr)) { // 这里用 Hutool 的 StrUtil，或者用 StringUtils.hasText
                // 把 JSON 字符串反序列化为 List<ShopType> 返回
                return mapper.readValue(jsonStr, new TypeReference<List<ShopType>>() {});
            }

            // 3. 缓存不存在，查询数据库
            List<ShopType> shopTypes = this.list();

            // 4. 如果数据库也没数据，直接返回空
            if (CollectionUtils.isEmpty(shopTypes)) {
                return shopTypes;
            }

            // 5. 将数据库查出的 List 序列化为 JSON 字符串，存入 Redis（用 opsForValue）
            String jsonToRedis = mapper.writeValueAsString(shopTypes);
            stringRedisTemplate.opsForValue().set(key, jsonToRedis);

            return shopTypes;

        } catch (Exception e) {
            throw new RuntimeException("查询店铺类型失败", e);
        }
    }
}
