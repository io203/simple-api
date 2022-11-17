package com.example.simpleapi.dao;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;


import com.example.simpleapi.model.Simple;

@Repository
public class UserDAO {
    private static final String NAMESPACE = "user.";

  

    /**
     * 유저정보 조회
     */
    @Cacheable(cacheNames = "userInfoCache", key = "#p0")
    public Simple selectOne() {
        return new Simple(100,"test-100", "contents-100");
    }

    /**
     * 로컬 캐시없이 유저정보 조회 (caffeine cache refresh용)
     */
    public Simple selectOneWithoutLocalCache() {
        return new Simple(100,"test-100 refresh용", "contents-100 refresh용");
		
    }
}
