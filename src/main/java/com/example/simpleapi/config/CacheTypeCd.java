package com.example.simpleapi.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.example.simpleapi.model.Simple;

import lombok.Getter;

@Getter
public enum CacheTypeCd {
    // COMMON_CACHE("commonCache", 64,120, TimeUnit.SECONDS),
    // USER_INFO_CACHE("userInfoCache", 500, 10, TimeUnit.MINUTES),
    // DEPT_INFO_CACHE("deptInfoCache", 200, 10, TimeUnit.MINUTES),
    // ;

    // private String cacheName;
    // private long maximumSize;
    // private long duration;  // ttl
    // private TimeUnit timeUnit;


    COMMON_CACHE("commonCache", 64, 10, TimeUnit.SECONDS, CacheRefreshStrategyCd.REFRESH, (param) -> {
        List<Simple> list = new ArrayList<>();
		
		for(int i=0 ; i< 10;i++) {
			list.add(new Simple(i+1,"test-"+i, "contents-"+i));
			 
		}
        System.out.println("------refresh data-----");
		 
		return list;
    }, Executors.newFixedThreadPool(1)),
    // USER_INFO_CACHE("userInfoCache", 500, 10, TimeUnit.MINUTES, CacheRefreshStrategyCd.REFRESH, (param) -> {
    //     UserDAO userDAO = BeanUtil.getBean(UserDAO.class);
    //     //public static final ObjectMapper OM = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
       
    //     return userDAO.selectOneWithoutLocalCache();
    // }, Executors.newFixedThreadPool(1)),
    DEPT_INFO_CACHE("deptInfoCache", 200, 10, TimeUnit.MINUTES, CacheRefreshStrategyCd.EXPIRE, null, null),
    ;

    private String cacheName;
    private long maximumSize;
    private long duration;  // ttl
    private TimeUnit timeUnit;
    private CacheRefreshStrategyCd strategyCd;
    private Function<Object, Object> dataLoadFunction;
    private Executor executor;

    CacheTypeCd(String cacheName, long maximumSize, long duration, TimeUnit timeUnit, CacheRefreshStrategyCd strategyCd, Function<Object, Object> dataLoadFunction, Executor executor) {
        this.cacheName = cacheName;
        this.maximumSize = maximumSize;
        this.duration = duration;
        this.timeUnit = timeUnit;
        this.strategyCd = strategyCd;
        if (CacheRefreshStrategyCd.REFRESH.equals(strategyCd)) {
            this.dataLoadFunction = Objects.requireNonNull(dataLoadFunction);
            this.executor = Objects.requireNonNullElseGet(executor, () -> Executors.newFixedThreadPool(1));
        }
    }

}
