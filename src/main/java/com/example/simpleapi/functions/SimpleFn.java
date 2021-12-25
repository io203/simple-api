package com.example.simpleapi.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.example.simpleapi.model.Simple;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleFn implements Function<String, List<Simple>>{
    @Override
    public List<Simple> apply(String count) {
        List<Simple> list = new ArrayList<>();

        int num = Integer.parseInt(count);
		
		for(int i=0 ; i< num;i++) {
			list.add(new Simple(i+1,"test-"+i, "contents-"+i));
			log.info("for "+i);
		}
		log.info(list.toString());
		return list;
    }
    
}
