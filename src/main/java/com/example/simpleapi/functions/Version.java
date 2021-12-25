package com.example.simpleapi.functions;

import java.util.function.Function;

public class Version implements Function<String, String>{

    @Override
    public String apply(String ver) {
       
        return "=====  version "+ ver;
    }
    
}
