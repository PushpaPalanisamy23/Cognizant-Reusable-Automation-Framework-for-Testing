package com.cognizant.framework.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

import com.cognizant.framework.FrameworkException;

public class WiniumDriverFactory {
                @SuppressWarnings("unused")
                private static Properties properties;
                private WiniumDriverFactory() {
                                
                }
                public static WiniumDriver getwiniumDriver(String winiumLocalHost,  String winiumAppPath) {
                                WiniumDriver driver = null;
                                try {
                                                DesktopOptions option = new DesktopOptions();
                                                option.setApplicationPath(winiumAppPath);
                                                try {
                                                                driver = new WiniumDriver(new URL(winiumLocalHost), option);
                                                } catch (MalformedURLException e) {
                                                                e.printStackTrace();
                                                } 
                                }catch(Exception ex){
                                                ex.printStackTrace();
                                                throw new FrameworkException(
                                                                                "The winium driver invocation created a problem , please check the capabilities");
                                }
                                return driver;
                }
}
