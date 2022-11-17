package com.example.simpleapi.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.benmanes.caffeine.cache.Cache;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CacheInfoController {
    @Autowired
    private CacheManager cacheManager;

    /**
     * cache의 정보들을 조회
     */

    @GetMapping("/localCache/details")
    public ResponseEntity<Map<String, ConcurrentMap<Object, Object>>> localCacheValueInfo() {

        Collection<String> cacheNames = cacheManager.getCacheNames();
        Map<String, ConcurrentMap<Object, Object>> response = new HashMap<>(cacheNames.size());
        for (String cacheName : cacheNames) {
            log.info("cacheName : {}", cacheName);
            CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
            if (cache != null) {
                Cache<Object, Object> nativeCache = cache.getNativeCache();
                response.put(cacheName, nativeCache.asMap());
            }
        }

        return ResponseEntity.ok(response);
    }

    /**
     * cache의 키 상태 정보들을 조회
     */
  
    @GetMapping("/localCache/status")
    public ResponseEntity<List<Map<String, String>>> localCacheStatInfo() {

        Collection<String> cacheNames = cacheManager.getCacheNames();
        List<Map<String, String>> response = new ArrayList<>(cacheNames.size());
        for (String cacheName : cacheNames) {
            CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
            if (cache != null) {
                Cache<Object, Object> nativeCache = cache.getNativeCache();
                response.add(Map.of(cacheName, nativeCache.stats().toString()));
            }
        }

        return ResponseEntity.ok(response);
    }

    /**
     * cache의 모든 캐시를 제거
     */

    @PostMapping("/localCache/clearAll")
    public ResponseEntity<HashMap> clearAllCache() {

        List<String> cacheNameList = new LinkedList<>();

        for (String cacheName : cacheManager.getCacheNames()) {
            cacheNameList.add(cacheName);
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        }

        HashMap<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("msg", "success remove all cache");
        rtnMap.put("removedCacheNameList", cacheNameList);
        return ResponseEntity.ok(rtnMap);
    }

    /**
     * 특정 캐시 1건을 clear
     */
    @PostMapping("/localCache/clear")
    public ResponseEntity<String> clearTargetCache(String cacheName) {

        try {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear(); //해당 캐시 clear

            return ResponseEntity.ok(String.format("'%s' clear success.", cacheName));

        } catch (Exception e) {
            log.error("Exception msg:{} | stack trace:", e.getMessage(), e);
            return new ResponseEntity<>(String.format("'%s' cache clear FAIL", cacheName), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
