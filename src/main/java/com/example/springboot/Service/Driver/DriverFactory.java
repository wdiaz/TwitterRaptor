package com.example.springboot.Service.Driver;

import com.example.springboot.Service.Driver.Types.Chrome;
import com.example.springboot.Service.Driver.Types.Firefox;
import com.example.springboot.Service.Driver.Types.IType;

public class DriverFactory {
    public static IType create(String driver) {
        switch (driver) {
            case "chrome":
                return new Chrome();
            case "firefox":
            case "gecko":
                return new Firefox();
            default:
                throw new IllegalArgumentException("Invalid Driver Specified.");
        }
    };
}
