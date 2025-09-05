package com.cognizant.framework.selenium;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
* Enumeration to represent the mode of execution
* 
 * @author Cognizant
*/
public enum ExecutionMode {
                /**
                * Execute API
                */
                API,
                /**
                * Execute on the local machine
                */
                LOCAL,

                /**
                * Execute on a selenium grid
                */
                GRID,
                
                CROSSBROWSER,
                /**
                * Execute on a Perfecto MobileCloud device using {@link RemoteWebDriver}
                */
                PERFECTO,

                /**
                * Execute on a mobile device using Appium
                */
                MOBILE,
                /**
                * Execute on SauceLabs
                */
                SAUCELABS,
                /**
                * Execute on TESTOBJECT
                */
                TESTOBJECT,
                /**
                * Execute on a Fastest for Cross browser testing
                */
                FASTEST,
                /**
                * 
                 * Execute using Winium
                */
                WINIUM,
                /**
                * Execute using BROWSERSTACK
                */
                BROWSERSTACK;
                
                

}
