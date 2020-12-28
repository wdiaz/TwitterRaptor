package com.example.springboot.Service.Driver.Types;

import java.io.IOException;
import java.util.Map;

public interface IType {
    Boolean shoot(String url) throws IOException, InterruptedException;
    Map getFileMap();

    void close();
    void quit();
}
