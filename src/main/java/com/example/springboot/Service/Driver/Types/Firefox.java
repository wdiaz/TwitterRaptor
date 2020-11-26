package com.example.springboot.Service.Driver.Types;

import java.io.IOException;
import java.util.Map;

public class Firefox implements IType{
    Map<String, String> fileNames;
    public Firefox() {
    }

    public Boolean shoot(String driver) throws IOException {
        return true;
    };
}
