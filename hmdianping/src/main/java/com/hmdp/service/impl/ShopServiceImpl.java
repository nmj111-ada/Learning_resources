package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.CacheClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private CacheClient cacheClient;

    // 根据id查询商铺信息
    @Override
    public Result queryById(Long id) {
        //解决缓存穿透
        //Shop shop = cacheClient.queryWithPassThrough("cache:shop:", id, Shop.class, this::getById, 20L, TimeUnit.MINUTES);

        //1.从redis查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get("cache:shop:" + id);
        //2.判断是否存在
        if (StrUtil.isNotBlank(shopJson)) {
            //3.存在直接返回
            return Result.ok(JSONUtil.toBean(shopJson, Shop.class));
        }
        //4.不存在根据id查询数据库
        Shop shop = getById(id);
        //5.不存在返回错误
        if (shop == null) {
            return Result.fail("店铺不存在");
        }
        //6.存在，写入redis
        stringRedisTemplate.opsForValue().set("cache:shop:" + id, JSONUtil.toJsonStr(shop));
        //7.返回
        return Result.ok(shop);
    }
}
