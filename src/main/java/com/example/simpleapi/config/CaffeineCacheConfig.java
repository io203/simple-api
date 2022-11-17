package com.example.simpleapi.config;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.extern.slf4j.Slf4j;


@EnableCaching // 캐시 기능을 활성화한다.
@Configuration
@Slf4j
public class CaffeineCacheConfig {

  // @Bean
  // public CacheManager cacheManager() {
  //   SimpleCacheManager cacheManager = new SimpleCacheManager();

  //   // @formatter:off
  //     List<CaffeineCache> caches = Arrays.stream(CacheTypeCd.values())
  //         .map(cacheType -> {
  //             Caffeine<Object, Object> caffeine = Caffeine.newBuilder().recordStats()
  //                                                         .maximumSize(cacheType.getMaximumSize())
  //                                                         .expireAfterWrite(cacheType.getDuration(), cacheType.getTimeUnit());
  //             return new CaffeineCache(cacheType.getCacheName(), caffeine.build());
  //         }).collect(Collectors.toList());
  //     // @formatter:on

  //   cacheManager.setCaches(caches);
  //   return cacheManager;
  // }
  @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        // @formatter:off
        List<CaffeineCache> caches = Arrays.stream(CacheTypeCd.values())
            .map(cacheType -> {
                Caffeine<Object, Object> caffeine = Caffeine.newBuilder().recordStats().maximumSize(cacheType.getMaximumSize());

                switch (cacheType.getStrategyCd()) {
                    case EXPIRE:
                        caffeine.expireAfterWrite(cacheType.getDuration(), cacheType.getTimeUnit());
                        return new CaffeineCache(cacheType.getCacheName(), caffeine.build());
                    case REFRESH:
                        caffeine.refreshAfterWrite(cacheType.getDuration(), cacheType.getTimeUnit())
                                .executor(cacheType.getExecutor());
                        return new CaffeineCache(cacheType.getCacheName(), caffeine.buildAsync(this.cacheLoader(cacheType.getDataLoadFunction())).synchronous());
                    default:
                        throw new IllegalArgumentException("Not support cache refresh strategy. | request strategy : " + cacheType.getStrategyCd());
                }
            }).collect(Collectors.toList());
        // @formatter:on

        cacheManager.setCaches(caches);
        return cacheManager;
    }

    private CacheLoader<Object, Object> cacheLoader(Function<Object, Object> dataLoadFunction) {
        return new CacheLoader<>() {
            CompletableFuture<Object> task = null;

            @Nullable
            @Override
            public Object load(Object key) throws Exception {
                log.debug("### get new data. first call/ | key : {}", key.toString());
                return dataLoadFunction.apply(key);
            }

            @Override
            public CompletableFuture<?> asyncReload(Object key, Object oldValue, Executor executor) throws Exception {
                if (task == null) {
                    task = CompletableFuture.supplyAsync(() -> dataLoadFunction.apply(key));
                } else {
                    if (task.isDone()) {
                        task = CompletableFuture.supplyAsync(() -> dataLoadFunction.apply(key));
                    } else {
                        log.debug("### new data is getting... Now old data return. | oldValue : {}", oldValue);
                        return CompletableFuture.completedFuture(oldValue);
                    }
                }

                executor.execute(task::toCompletableFuture);

                return task;
            }
        };
    }
}
 
