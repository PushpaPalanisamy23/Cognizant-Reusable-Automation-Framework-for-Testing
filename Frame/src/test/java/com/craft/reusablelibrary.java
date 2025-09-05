
package com.cognizant.craft;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cognizant.framework.APIReusuableLibrary;
import com.cognizant.framework.CraftDataTable;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Status;
import com.cognizant.framework.selenium.CraftDriver;
import com.cognizant.framework.selenium.SeleniumReport;
import com.cognizant.framework.selenium.WebDriverUtil;


import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;

/**
* Abstract base class for reusable libraries created by the user
* 
 * @author Cognizant
*/
@SuppressWarnings("unused")
public abstract class ReusableLibrary {

                int responseStatus;
                int responseCode;
                private static HttpURLConnection httpURLConnect;

                protected Map<String, Object> perfectoCommand = new HashMap<>();
                Dimension winSize;
                /**
                * The {@link CraftDataTable} object (passed from the test script)
                */
                protected CraftDataTable dataTable;
                /**
                * The {@link SeleniumReport} object (passed from the test script)
                */
                protected SeleniumReport report;
                /**
                * The {@link CraftDriver} object
                */
                protected CraftDriver driver;

                protected WebDriverUtil driverUtil;

                /**
                * The {@link ScriptHelper} object (required for calling one reusable
                * library from another)
                */
                protected ScriptHelper scriptHelper;

                /**
                * The {@link Properties} object with settings loaded from the framework
                * properties file
                */
                protected Properties properties;
                /**
                * The {@link FrameworkParameters} object
                */
                protected FrameworkParameters frameworkParameters;
                
                protected APIReusuableLibrary apiDriver;

                /**
                * Constructor to initialize the {@link ScriptHelper} object and in turn the
                * objects wrapped by it
                * 
                 * @param scriptHelper
                *            The {@link ScriptHelper} object
                */
                public ReusableLibrary(ScriptHelper scriptHelper) {
                                this.scriptHelper = scriptHelper;
                                this.dataTable = scriptHelper.getDataTable();
                                this.report = scriptHelper.getReport();
                                this.driver = scriptHelper.getcraftDriver();
                                this.driverUtil = scriptHelper.getDriverUtil();
                                this.apiDriver = scriptHelper.getApiDriver();

                                properties = Settings.getInstance();
                                frameworkParameters = FrameworkParameters.getInstance();
                }

                /**
                * All reusuable Appium Functions with Perfecto
                */

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param context
                *            - Context of App like NATIVE_APP or WEB
                * @param appName
                *            - Name of the App as displayed in Mobile
                */
                protected void openApp(final String context, final String appName) {
                                if (context.equals("NATIVE_APP")) {
                                                final Map<String, Object> perfectoCommand = new HashMap<>();
                                                perfectoCommand.put("name", appName);
                                                driver.getAppiumDriver().executeScript("mobile:application:open",
                                                                                perfectoCommand);
                                }
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param context
                *            - Context of App like NATIVE_APP or WEB
                * @param appName
                *            - Identifier of the App.
                */
                protected void openAppWithIdentifier(final String context,
                                                final String identifer) {
                                if (context.equals("NATIVE_APP")) {
                                                perfectoCommand.put("identifier", identifer);
                                                driver.getAppiumDriver().executeScript("mobile:application:open",
                                                                                perfectoCommand);
                                                perfectoCommand.clear();
                                }
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param type
                *            - Type of report like pdf
                */
                protected byte[] downloadReport(final String type) throws IOException {
                                final String command = "mobile:report:download";
                                final Map<String, String> params = new HashMap<>();
                                params.put("type", type);
                                final String report = (String) (driver.getRemoteWebDriver())
                                                                .executeScript(command, params);
                                final byte[] reportBytes = OutputType.BYTES
                                                                .convertFromBase64Png(report);
                                return reportBytes;
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 */
                protected byte[] downloadWTReport() {
                                final String reportUrl = (String) driver.getAppiumDriver()
                                                                .getCapabilities().getCapability("windTunnelReportUrl");
                                String returnString = "<html><head><META http-equiv=\"refresh\" content=\"0;URL=";
                                returnString = returnString + reportUrl + "\"></head><body /></html>";

                                return returnString.getBytes();
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param context
                *            - Context of App like NATIVE_APP or WEB
                * @param appName
                *            - Name of the App.
                */
                protected void closeApp(final String context, final String appName) {
                                if (context.equals("NATIVE_APP")) {
                                                perfectoCommand.put("name", appName);
                                                try {
                                                                driver.getAppiumDriver().executeScript(
                                                                                                "mobile:application:close", perfectoCommand);
                                                } catch (final WebDriverException e) {
                                                }
                                }
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param context
                *            - Context of App like NATIVE_APP or WEB
                * @param appName
                *            - Identifier of the App.
                */
                protected void closeAppWithIdentifier(final String context,
                                                final String bundleId) {
                                if (context.equals("NATIVE_APP")) {
                                                perfectoCommand.put("identifier", bundleId);
                                                try {
                                                                driver.getAppiumDriver().executeScript(
                                                                                                "mobile:application:close", perfectoCommand);
                                                } catch (final WebDriverException e) {
                                                }
                                }
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param textToFind
                *            - text that has to be searched
                * @param timeout
                */
                protected Boolean textCheckpoint(final String textToFind,
                                                final Integer timeout) {
                                perfectoCommand.put("content", textToFind);
                                perfectoCommand.put("timeout", timeout);
                                final Object result = driver.getAppiumDriver().executeScript(
                                                                "mobile:checkpoint:text", perfectoCommand);
                                final Boolean resultBool = Boolean.valueOf(result.toString());
                                perfectoCommand.clear();
                                return resultBool;
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param textToFind
                *            - text that has to be searched
                * @param timeout
                */
                protected void textClick(final String textToFind, final Integer timeout) {
                                perfectoCommand.put("content", textToFind);
                                perfectoCommand.put("timeout", timeout);
                                driver.getAppiumDriver().executeScript("mobile:text:select",
                                                                perfectoCommand);
                                perfectoCommand.clear();

                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param label
                *            - text that has to be searched
                * @param threshold
                */
                protected void visualScrollToClick(final String label,
                                                final Integer threshold) {
                                perfectoCommand.put("label", label);
                                perfectoCommand.put("threshold", threshold);
                                perfectoCommand.put("scrolling", "scroll");
                                driver.getAppiumDriver().executeScript("mobile:button-text:click",
                                                                perfectoCommand);
                                perfectoCommand.clear();
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param label
                *            - text that has to be searched
                * @param timeout
                * @param threshold
                */
                protected void visualClick(final String label, final Integer timeout,
                                                final Integer threshold) {
                                perfectoCommand.put("label", label);
                                perfectoCommand.put("threshold", threshold);
                                perfectoCommand.put("timeout", timeout);
                                driver.getAppiumDriver().executeScript("mobile:button-text:click",
                                                                perfectoCommand);
                                perfectoCommand.clear();
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param label
                *            - text that has to be searched
                * @param timeout
                * @param threshold
                * @param labelDirection
                * @param labelOffset
                */
                protected void visualClick(final String label, final Integer timeout,
                                                final Integer threshold, final String labelDirection,
                                                final String labelOffset) {
                                perfectoCommand.put("label", label);
                                perfectoCommand.put("threshold", threshold);
                                perfectoCommand.put("timeout", timeout);
                                perfectoCommand.put("label.direction", labelDirection);
                                perfectoCommand.put("label.offset", labelOffset);
                                driver.getAppiumDriver().executeScript("mobile:button-text:click",
                                                                perfectoCommand);
                                perfectoCommand.clear();
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param imagePath
                */
                protected void imageClick(String imagePath) {
                                perfectoCommand.put("content", imagePath);
                                perfectoCommand.put("timeout", "5");
                                perfectoCommand.put("screen.top", "0%");
                                perfectoCommand.put("screen.height", "100%");
                                perfectoCommand.put("screen.left", "0%");
                                perfectoCommand.put("screen.width", "100%");
                                driver.executeScript("mobile:image:select", perfectoCommand);
                                perfectoCommand.clear();
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param imagePath
                */
                protected Boolean imageCheckpoint(String imagePath) {
                                perfectoCommand.put("content", imagePath);
                                perfectoCommand.put("threshold", "90");
                                perfectoCommand.put("screen.top", "0%");
                                perfectoCommand.put("screen.height", "100%");
                                perfectoCommand.put("screen.left", "0%");
                                perfectoCommand.put("screen.width", "100%");
                                Object result = driver.executeScript("mobile:image:find",
                                                                perfectoCommand);
                                final Boolean resultBool = Boolean.valueOf(result.toString());
                                perfectoCommand.clear();
                                return resultBool;
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param repositoryFile
                * @param handsetFile
                */
                protected void putFileOnDevice(final String repositoryFile,
                                                final String handsetFile) {
                                perfectoCommand.put("repositoryFile", repositoryFile);
                                perfectoCommand.put("handsetFile", handsetFile);
                                driver.getAppiumDriver().executeScript("mobile:media:put",
                                                                perfectoCommand);
                                perfectoCommand.clear();

                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param handsetFile
                * @param repositoryFile
                */
                protected void getFileOnDevice(final String handsetFile,
                                                final String repositoryFile) {
                                perfectoCommand.put("repositoryFile", repositoryFile);
                                perfectoCommand.put("handsetFile", handsetFile);
                                driver.getAppiumDriver().executeScript("mobile:media:get",
                                                                perfectoCommand);
                                perfectoCommand.clear();

                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param handsetFile
                */
                protected void deleteFromDevice(final String handsetFile) {
                                perfectoCommand.put("handsetFile", handsetFile);
                                driver.getAppiumDriver().executeScript("mobile:media:delete",
                                                                perfectoCommand);
                                perfectoCommand.clear();

                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param repositoryFile
                */
                protected void deleteFromRepository(final String repositoryFile) {
                                perfectoCommand.put("repositoryFile", repositoryFile);
                                driver.getAppiumDriver().executeScript("mobile:media:delete",
                                                                perfectoCommand);
                                perfectoCommand.clear();

                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param keyPress
                */
                protected void deviceKeyPress(final String keyPress) {

                                perfectoCommand.put("keySequence", keyPress);
                                driver.getAppiumDriver().executeScript("mobile:presskey",
                                                                perfectoCommand);
                                perfectoCommand.clear();
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param x1
                * @param y1
                * @param x2
                * @param y2
                */
                protected void swipe(final String x1, final String y1, final String x2,
                                                final String y2) {
                                final List<String> swipeCoordinates = new ArrayList<>();
                                swipeCoordinates.add(x1 + ',' + y1);
                                swipeCoordinates.add(x2 + ',' + y2);
                                perfectoCommand.put("location", swipeCoordinates);
                                driver.getAppiumDriver().executeScript("mobile:touch:drag",
                                                                perfectoCommand);
                                perfectoCommand.clear();
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param textToFind
                */
                protected void swipeTillText(String textToFind) {
                                perfectoCommand.put("content", textToFind);
                                perfectoCommand.put("scrolling", "scroll");
                                perfectoCommand.put("maxscroll", "10");
                                perfectoCommand.put("next", "SWIPE_UP");
                                driver.executeScript("mobile:text:select", perfectoCommand);
                                perfectoCommand.clear();
                }

                /**
                * Function Applicable to Pause the Script, Generic Application
                * 
                 * @param How_Long_To_Pause
                */
                public void PauseScript(int How_Long_To_Pause) {
                                // convert to seconds
                                How_Long_To_Pause = How_Long_To_Pause * 1000;

                                try {
                                                Thread.sleep(How_Long_To_Pause);
                                } catch (final InterruptedException ex) {
                                                Thread.currentThread().interrupt();
                                }
                }

                /**
                * All reusuable Selenium Functions with Perfecto
                */

                /**
                * Function to switch the Context
                * 
                 * @param driver
                * @RemoteWebDriver
                * @param context
                */
                protected static void switchToContext(RemoteWebDriver driver, String context) {
                                RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("name", context);
                                executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param driver
                * @param list
                */
                @SuppressWarnings("rawtypes")
                protected void scrollChecker(IOSDriver driver, String[] list) {
                                for (int i = 0; i < list.length; i++) {

                                                MobileElement me = (MobileElement) driver.findElement(By
                                                                                .xpath("//UIAPickerWheel[" + (i + 1) + "]"));
                                                int mget = getMonthInt(me.getText().split(",")[0]);

                                                if (i == 0) {
                                                                if (mget > getMonthInt(list[i])) {
                                                                                scrollAndSearch(driver, list[i], me, true);
                                                                } else {
                                                                                scrollAndSearch(driver, list[i], me, false);
                                                                }
                                                } else {
                                                                if (Integer.parseInt(me.getText().split(",")[0]) > Integer
                                                                                                .parseInt(list[i])) {
                                                                                scrollAndSearch(driver, list[i], me, true);
                                                                } else {
                                                                                scrollAndSearch(driver, list[i], me, false);
                                                                }
                                                }
                                }
                }

                // Used to get the integer for a month based on the string of the month
                private int getMonthInt(String month) {
                                int monthInt = 0;
                                switch (month) {
                                case "Jan":
                                                monthInt = 1;
                                                break;
                                case "January":
                                                monthInt = 1;
                                                break;
                                case "February":
                                                monthInt = 2;
                                                break;
                                case "Feb":
                                                monthInt = 2;
                                                break;
                                case "March":
                                                monthInt = 3;
                                                break;
                                case "Mar":
                                                monthInt = 3;
                                                break;
                                case "April":
                                                monthInt = 4;
                                                break;
                                case "Apr":
                                                monthInt = 4;
                                                break;
                                case "May":
                                                monthInt = 5;
                                                break;
                                case "June":
                                                monthInt = 6;
                                                break;
                                case "Jun":
                                                monthInt = 6;
                                                break;
                                case "July":
                                                monthInt = 7;
                                                break;
                                case "Jul":
                                                monthInt = 7;
                                                break;
                                case "August":
                                                monthInt = 8;
                                                break;
                                case "Aug":
                                                monthInt = 8;
                                                break;
                                case "September":
                                                monthInt = 9;
                                                break;
                                case "Sep":
                                                monthInt = 9;
                                                break;
                                case "October":
                                                monthInt = 10;
                                                break;
                                case "Oct":
                                                monthInt = 10;
                                                break;
                                case "November":
                                                monthInt = 11;
                                                break;
                                case "Nov":
                                                monthInt = 11;
                                                break;
                                case "December":
                                                monthInt = 12;
                                                break;
                                case "Dec":
                                                monthInt = 12;
                                                break;
                                }
                                return monthInt;
                }

                // Code here shouldn't be modified
                @SuppressWarnings("rawtypes")
                private void scrollAndSearch(IOSDriver driver, String value,
                                                MobileElement me, Boolean direction) {
                                String x = getLocationX(me);
                                String y = getLocationY(me);
                                while (!driver.findElementByXPath(getXpathFromElement(me)).getText()
                                                                .contains(value)) {
                                                swipe(driver, x, y, direction);
                                }
                }

                // Performs the swipe and search operation
                // Code here shouldn't be modified
                @SuppressWarnings("rawtypes")
                private void swipe(IOSDriver driver, String start, String end, Boolean up) {
                                String direction;
                                if (up) {
                                                direction = start + "," + (Integer.parseInt(end) + 70);
                                } else {
                                                direction = start + "," + (Integer.parseInt(end) - 70);
                                }

                                Map<String, Object> params1 = new HashMap<>();
                                params1.put("location", start + "," + end);
                                params1.put("operation", "down");
                                driver.executeScript("mobile:touch:tap", params1);

                                Map<String, Object> params2 = new HashMap<>();
                                List<String> coordinates2 = new ArrayList<>();

                                coordinates2.add(direction);
                                params2.put("location", coordinates2);
                                params2.put("auxiliary", "notap");
                                params2.put("duration", "3");
                                driver.executeScript("mobile:touch:drag", params2);

                                Map<String, Object> params3 = new HashMap<>();
                                params3.put("location", direction);
                                params3.put("operation", "up");
                                driver.executeScript("mobile:touch:tap", params3);
                }

                // Gets the objects X location in pixels
                private String getLocationX(MobileElement me) {
                                int x = me.getLocation().x;
                                int width = (Integer.parseInt(me.getAttribute("width")) / 2) + x;
                                return width + "";
                }

                // Gets the objects X location in pixels
                private String getLocationY(MobileElement me) {
                                int y = me.getLocation().y;
                                int height = (Integer.parseInt(me.getAttribute("height")) / 2) + y;
                                return height + "";
                }

                // Parses webelement to retrieve the xpath used for identification
                private String getXpathFromElement(MobileElement me) {
                                return (me.toString().split("-> xpath: ")[1]).substring(0, (me
                                                                .toString().split("-> xpath: ")[1]).length() - 1);
                }

                /**
                * Function Applicable only when the ExecutionMode used is <b>PERFECTO
                * 
                 * @param letter
                */
                protected void drawLetter(final String letter) {
                                final List<String> coordinates = new ArrayList<>();

                                switch (letter) {
                                case "A":

                                                break;
                                case "B":

                                                break;
                                case "C":

                                                break;
                                case "D":

                                                break;
                                case "E":
                                                coordinates.add("42%,40%");
                                                coordinates.add("42%,60%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                coordinates.add("42%,40%");
                                                coordinates.add("52%,40%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                coordinates.add("42%,48%");
                                                coordinates.add("52%,48%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                coordinates.add("42%,56%");
                                                coordinates.add("52%,56%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                break;
                                case "F":

                                                break;
                                case "G":

                                                break;
                                case "H":

                                                break;
                                case "I":

                                                break;
                                case "J":

                                                break;
                                case "K":

                                                break;
                                case "L":

                                                break;
                                case "M":

                                                break;
                                case "N":

                                                break;
                                case "O":

                                                break;
                                case "P":
                                                coordinates.add("30%,40%");
                                                coordinates.add("30%,60%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                coordinates.add("30%,40%");
                                                coordinates.add("40%,40%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                coordinates.add("38%,40%");
                                                coordinates.add("38%,52%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                coordinates.add("38%,48%");
                                                coordinates.add("28%,48%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                break;
                                case "Q":

                                                break;
                                case "R":
                                                coordinates.add("54%,40%");
                                                coordinates.add("54%,60%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                coordinates.add("54%,40%");
                                                coordinates.add("64%,40%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                coordinates.add("62%,40%");
                                                coordinates.add("62%,52%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                coordinates.add("62%,48%");
                                                coordinates.add("52%,48%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                coordinates.add("54%,48%");
                                                coordinates.add("64%,60%");
                                                perfectoCommand.put("location", coordinates);
                                                driver.executeScript("mobile:touch:drag", perfectoCommand);
                                                perfectoCommand.clear();
                                                coordinates.clear();
                                                break;
                                case "S":

                                                break;
                                case "T":

                                                break;
                                case "U":

                                                break;
                                case "V":

                                                break;
                                case "W":

                                                break;
                                case "X":

                                                break;
                                case "Y":

                                                break;
                                case "Z":

                                                break;
                                }
                }

                /**
                * Function to check the bro
                * 
                 * @param Url
                */
                protected void brokenLinkValidator(String Url) {
                                urlLinkStatus(validationOfLinks(Url));
                }

                private String[] validationOfLinks(String urlToValidate) {
                                String[] responseArray = new String[3];
                                try {
                                                URL url = new URL(urlToValidate);
                                                httpURLConnect = (HttpURLConnection) url.openConnection();
                                                httpURLConnect.setConnectTimeout(3000);
                                                httpURLConnect.connect();
                                                responseStatus = httpURLConnect.getResponseCode();
                                                responseCode = responseStatus / 100;
                                } catch (Exception e) {
                                }
                                responseArray[0] = urlToValidate;
                                responseArray[1] = String.valueOf(responseCode);
                                responseArray[2] = String.valueOf(responseStatus);
                                return responseArray;
                }

                private void urlLinkStatus(String[] responseArray) {
                                try {
                                                String responseValue = responseArray[1];
                                                responseCode = Integer.valueOf(responseValue);
                                                String responseStatus = responseArray[2];
                                                switch (responseCode) {
                                                case 2:
                                                                report.updateTestLog("URL", "Response code : "
                                                                                                + responseStatus + " - OK", Status.PASS);
                                                                break;
                                                case 3:
                                                                report.updateTestLog("URL", "Unknown Responce Code",
                                                                                                Status.FAIL);
                                                                break;
                                                case 4:
                                                                report.updateTestLog("URL", "Response code : "
                                                                                                + responseStatus + " - Client error", Status.FAIL);
                                                                break;

                                                case 5:
                                                                report.updateTestLog("URL", "Response code : "
                                                                                                + responseStatus + " - Internal Server Error",
                                                                                                Status.FAIL);
                                                                break;
                                                default:
                                                                report.updateTestLog("URL", "Unknown Responce Code",
                                                                                                Status.FAIL);

                                                                break;
                                                }

                                } catch (Exception e) {

                                } finally {
                                                httpURLConnect.disconnect();

                                }
                }

                /**
                * Function to validate Alexa report
                * 
                 * @param utterances
                * @param expectedValue
                * @param actualValue
                * 
                 */
                protected void updateReport(String utterances, String expectedValue,
                                                String actualValue) {

                                if (expectedValue.equalsIgnoreCase(actualValue)) {
                                                report.updateTestLog(utterances, expectedValue, actualValue,
                                                                                Status.PASS);
                                } else {
                                                report.updateTestLog(utterances, expectedValue, actualValue,
                                                                                Status.FAIL);
                                }
                }
                public void waitFOR(By Element) {
       /* ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
               public Boolean apply(WebDriver driver) {
          return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
      }
  };*/
        
        try {
               WebDriverWait wait = new WebDriverWait (driver.getWebDriver(),30);
               wait.until(ExpectedConditions.presenceOfElementLocated(Element));
               wait.until(ExpectedConditions.elementToBeClickable(Element));
               wait.until(ExpectedConditions.visibilityOfElementLocated(Element));
//               wait.until(expectation);
        }catch(Exception e) {
               System.out.println(" waitFOR "+e);
        }
}
                public void String_compare(String actualvalue,String expectedvalue){
                                
                                if (actualvalue.contains(expectedvalue)) {
                                                report.updateTestLog("Checking the data", "Data found for : "+actualvalue , Status.DONE);
                                                System.out.println("actual:"+actualvalue+"expected:"+expectedvalue+"are same");
                                }else{
                                                report.updateTestLog("Checking the data", "Data not found for : "+actualvalue , Status.FAIL);
                                                System.out.println("actual:"+actualvalue+"expected:"+expectedvalue+"are not same");
                                }
                }
                
                public void Click(By element, String nameofelement){ 
                                
                                try{
                                                waitFOR(element);
                                                Javascript_Click_Scroll(element,"Scroll");
                                                Actions actions = new Actions(driver.getWebDriver());
                                                waitFOR(element);
                                                actions.moveToElement(driver.findElement(element));
                                                actions.click();
                                                actions.build().perform();
                                                report.updateTestLog("Check Presence of Element and click ", nameofelement+" is Present and clicked", Status.DONE);
                                }catch(Exception e){
                                                System.out.println(nameofelement+" is not clicked");
                                                report.updateTestLog("Check Presence of Element and click ", nameofelement+" is not Displayed", Status.FAIL);
                                }
                                
                }
                
                
                public void Javascript_Click_Scroll( By element,String Operation) {
                                try{
                                                if(Operation.equalsIgnoreCase("Scroll")){
                                                ((JavascriptExecutor)driver.getWebDriver()).executeScript("arguments[0].scrollIntoView(true);",driver.findElement(element));
                                                }else if(Operation.equalsIgnoreCase("Click")){
                                                    ((JavascriptExecutor)driver.getWebDriver()).executeScript("arguments[0].click();",driver.findElement(element));
                                                }
                                }catch(Exception e){
                                                System.out.println("Javascript_Click_Scroll  error "+e.toString());
                                                report.updateTestLog("Scroll to view", "Element is not Displayed unable to "+Operation, Status.FAIL);
                                }
                                                
                                }

                public void SendKeys(By element, String value) {
                                try{
                                                waitFOR(element);
                                                Javascript_Click_Scroll(element, "Scroll");
                                                Actions actions = new Actions(driver.getWebDriver());
                                                actions.moveToElement(driver.findElement(element));
                                                actions.click();
                                                actions.sendKeys(value);
                                                actions.build().perform();           
                                }catch(Exception e){
                                                System.out.println(value+"Edit box is not displayed"+e.toString());
                                                report.updateTestLog("Text Field","Text Field is not Displayed", Status.FAIL);
                                }
                                
                }
                
                public void isDisplayed(By element, String element_name){
                                try{
                                                waitFOR(element);
                                                Javascript_Click_Scroll(element, "Scroll");
                                                if (driver.findElement(element).isDisplayed()) {
                                                                report.updateTestLog("Open " + element_name, element_name+" opened successfully", Status.PASS);
                                                                System.out.println("Open "+element_name);
                                                }else{
                                                                report.updateTestLog("Open "+element_name, element_name+" not opened", Status.FAIL);                                     
                                                                System.out.println("unable to Open"+element_name);
                                                }
                                }catch(Exception e){
                                                System.out.println(element_name+"Page element is not displyed");
                                                report.updateTestLog("Check_Page", element+"Page element is not displyed"+e.toString(), Status.FAIL);
                                }
                }
public boolean isDisplayed_boolean(By element, String element_name){
                try{
                                                if (driver.findElement(element).isDisplayed()) {
                                                                System.out.println("Element Displayed "+element_name);
                                                                return true;
                                                }else{
                                                                return false;
                                                }
                                }catch(Exception e){
                                                return false;
                                }
                }

public boolean isDisplayed_boolean_special(String element, String element_name){
                try{
                                                if (driver.findElement(By.xpath(element)).isDisplayed()) {
                                                                System.out.println("Element Displayed "+element_name);
                                                                return true;
                                                }else{
                                                                return false;
                                                }
                                }catch(Exception e){
                                                return false;
                                }
                }
                public void Mouseover(By element){
                                
                                try{
                                                waitFOR(element);        
                                                Javascript_Click_Scroll(element, "Scroll");
                                                Actions action=new Actions(driver.getWebDriver());
                                                action.moveToElement(driver.findElement(element)).build().perform();
                                                report.updateTestLog("Mousehover the element", "Mousehover is successful", Status.DONE);
                                                System.out.println("Mousehover is successful");
                                }catch(Exception e){
                                                report.updateTestLog("Mousehover", "Unable to Find Element", Status.FAIL);
                                }
                                
                }
                
                                
                public void SelectDropdown(By element, String value, String Type){
                                try{
                                                waitFOR(element);
                                                Javascript_Click_Scroll(element, "Scroll");       
                                                Select elm=new Select(driver.findElement(element));
                                                switch(Type){
                                                case "ByText":
                                                                System.out.println(value);
                                                                elm.selectByVisibleText(value);
                                                                report.updateTestLog("Selecting a dropdown ", value+" is selected", Status.PASS);
                                                                System.out.println(value+" is selected");
                                                                break;
                                                case "ByValue":
                                                                elm.selectByValue(value);
                                                                report.updateTestLog("Selecting a dropdown ", value+" is selected", Status.PASS);
                                                                System.out.println(value+" is selected");
                                                                break;
                                                case "ByIndex":
                                                                int strTemp= Integer.parseInt(value);
                                                                elm.selectByIndex(strTemp);
                                                                report.updateTestLog("Selecting a dropdown ", value+" is selected", Status.PASS);
                                                                System.out.println(value+" is selected");
                                                                break;
                                                default :
                                                                System.out.println(value+" Type given is not found");
                                                                report.updateTestLog("SelectDropdown", value+" type gien is not found", Status.FAIL);
                                                                break;
                                                }
                                }catch(Exception e){
                                                report.updateTestLog("SelectDropdown ", value+" Select box is not displayed ", Status.FAIL);
                                }
                                
                }
                
                
                public void ElementExist(By element, String value){
                                try{         
                                                waitFOR(element);
                                                Javascript_Click_Scroll(element, "Scroll");       
                                                if(driver.findElement(element).isDisplayed()){
                                                                report.updateTestLog("Existence of element", value+" is displayed successfully", Status.PASS);
                                                                System.out.println(value+" is displayed successfully");
                                                }else{
                                                                report.updateTestLog("Existence of element", value+" is not displayed", Status.FAIL);
                                                                System.out.println(value+" is not displayed");
                                                }
                                }catch(Exception e){
                                                System.out.println(value+" is not displayed"+e.toString());
                                                report.updateTestLog("Check Presence of Element", value+" Element is not displayed", Status.FAIL);
                                }
                }
                
                public void isTextDisplayed(By element, String nameofelement, String valueofelement){
                try{         
                                waitFOR(element);
                                Javascript_Click_Scroll(element, "Scroll");
                                if (driver.findElement(element).isDisplayed()) {
                                                report.updateTestLog("Existence of element", nameofelement+" Element is displayed successfully", Status.DONE);
                                                System.out.println(nameofelement+" exists");
                                }else{
                                                report.updateTestLog("Existence of element", nameofelement+" Element is not displayed", Status.FAIL);                                             
                                                System.out.println(nameofelement+" does not exists");
                                }
                                
                                if (driver.findElement(element).getText().equalsIgnoreCase(valueofelement)) {
                                                report.updateTestLog("Text Presence", "Data found : "+valueofelement , Status.DONE);
                                                System.out.println("actual:"+driver.findElement(element).getText()+"expected:"+valueofelement+"are same");
                                }else{
                                                report.updateTestLog("Text Presence", "Data not found : "+valueofelement , Status.FAIL);
                                                System.out.println("actual:"+driver.findElement(element).getText()+"expected:"+valueofelement+"are not same");
                                }
                }catch(Exception e){
                                report.updateTestLog("Existence of element", nameofelement+" Element is not displayed", Status.FAIL);
                }
                }
                public void Javascript_Click_Scroll_WebElement( WebElement element,String Operation) {
                                try{
                                                if(Operation.equalsIgnoreCase("Scroll")){
                                                                ((JavascriptExecutor)driver.getWebDriver()).executeScript("arguments[0].scrollIntoView(true);",element);
                                                }else if(Operation.equalsIgnoreCase("Click")){
                                                                ((JavascriptExecutor)driver.getWebDriver()).executeScript("arguments[0].click();",element);
                                                }
                                }catch(Exception e){
                                                System.out.println("Javascript_Click_Scroll  error "+e.toString());
                                                report.updateTestLog("Scroll to view", "Element is not Displayed unable to "+Operation, Status.FAIL);
                                }
                                                
                                }
                public void highLightElement(By element){
                                JavascriptExecutor js=(JavascriptExecutor)driver.getWebDriver(); 

                                js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", driver.findElement(element));

                                try {
                                                Thread.sleep(500);
                                }catch (InterruptedException e) {
                                                System.out.println(e.getMessage());
                                }
                                js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", driver.findElement(element)); 
                }
                public void dowait(By Element,String Element_Name, int timeout){ 
                                int x=0;
                                boolean displayed=false;
                                String strflag="false" ;
                todo:     do {
                                                displayed = isDisplayed_boolean(Element, Element_Name);
                                                if(displayed){
                                                                report.updateTestLog("Check Presence of Element",Element_Name+ " element is Displayed", Status.PASS);
                                                                strflag="true";
                                                }
                                                x=x+1;
                                                try {
                                                                Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                }
                                                if(strflag.equalsIgnoreCase("true")){
                                                                break todo;
                                                }
                                }while (x<=timeout);
                                if(!displayed & x>timeout){
                                                System.out.println("Element is not Displayed");
//                                             report.updateTestLog("Element validation",Element_Name+ " element is not Displayed", Status.FAIL);
                                }
                }
                public String window_handle(){
                                String parentwindow = null;
                                try{
                                                parentwindow = driver.getWindowHandle();
                                                Thread.sleep(5000);
                                                for(String subWindow:driver.getWindowHandles()){
                                                                driver.switchTo().window(subWindow);
                                                }
                                                driver.manage().window().maximize();
                                                return parentwindow;
                                }catch(Exception e){
                                                System.out.println("Window handle exception "+e.toString());
                                                report.updateTestLog("Switch to new Tab or Window", "Switch to new Tab or Window-unable find a new Window/Tab ", Status.FAIL);
                                                return parentwindow;
                                }
                }
public void Click_element(WebElement element, String nameofelement){      
                                
                                try{
                                                Actions actions = new Actions(driver.getWebDriver());
                                                actions.moveToElement(element);
                                                actions.click();
                                                actions.build().perform();
                                                report.updateTestLog("Check Presence of Element and click ", nameofelement+" is clicked", Status.DONE);
                                }catch(Exception e){
                                                System.out.println(nameofelement+" is not clicked"+e.toString());
                                                report.updateTestLog("Check Presence of Element and click ", nameofelement+" is not clicked", Status.FAIL);
                                }
                                
                }               
public void scrollToView( WebElement element) {
                try {
                                JavascriptExecutor js=(JavascriptExecutor)driver.getWebDriver(); 
                                js.executeScript("arguments[0].scrollIntoView(true);", element);
                } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                }
}
public void scrollToView( By element) {
                try {
                                JavascriptExecutor js=(JavascriptExecutor)driver.getWebDriver(); 
                                js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(element));
                } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                }
}
public void click (By OR) {
                try {
                                Actions actions = new Actions(driver.getWebDriver());
                                waitFOR(OR);
                                actions.moveToElement(driver.findElement(OR));
                                actions.click();
                                actions.build().perform();                                           
                }catch(Exception e) {
                                System.out.println(" sendKeys "+e);
                }
}
public void Fill_Editbox(By element, String value) {
                
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(element));
                wait.until(ExpectedConditions.elementToBeClickable(element));
                driver.findElement(element).clear();
                try {
                                Thread.sleep(2000);
                } catch (InterruptedException e) {
                                e.printStackTrace();
                }
                driver.findElement(element).sendKeys(value);
                try {
                                Thread.sleep(2000);
                } catch (InterruptedException e) {
                                e.printStackTrace();
                }
                String nameofelement=driver.findElement(element).getText();
                if (nameofelement.isEmpty()){nameofelement="Element";}
                report.updateTestLog("Text Field", value+" is entered in the "+nameofelement, Status.DONE);
                System.out.println(value+" is entered in the "+nameofelement);
                
}

public void Check_Page(By element, String pagename){
                
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
                
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(element));
                if (driver.findElement(element).isDisplayed()) {
                                report.updateTestLog("Check Presence of Page", pagename+" page opened successfully", Status.PASS);
                                System.out.println("Open "+pagename);
                }else{
                                report.updateTestLog("Check Presence of Page", pagename+" page is not Displayed", Status.FAIL);                                     
                                System.out.println("unable to Open"+pagename);
                }
}

public void Mousehover(By element){
                
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
                
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(element));                  
                Actions action=new Actions(driver.getWebDriver());
                action.moveToElement(driver.findElement(element)).build().perform();
                report.updateTestLog("Mousehover", "Mouseover is successful", Status.PASS);
                System.out.println("Mouseover is successful");
                
}

public void DriverWait(By OR ){
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
                
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(OR));
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(OR));
                wait.until(ExpectedConditions.elementToBeClickable(OR));
                report.updateTestLog("Waiting for element", "Element appeared successfully", Status.PASS);
                System.out.println("Element appeared successfully");
                
}

public void SelectDropdownByValue(By element, String value){
                
                Select elm=new Select(driver.findElement(element));
                elm.selectByValue(value);
                elm.selectByVisibleText(value);
                report.updateTestLog("Selecting a dropdown ", value+" is selected", Status.PASS);
                System.out.println(value+" is selected");
}

public void SelectDropdownByText(By element, String value){
                
                Select elm=new Select(driver.findElement(element));
                elm.selectByVisibleText(value);
                report.updateTestLog("Selecting a dropdown ", value+" is selected", Status.PASS);
                System.out.println(value+" is selected");
}
public void RetrieveValue(String actualvalue,String expectedvalue){
                if (actualvalue.contains(expectedvalue)) {
                                report.updateTestLog("Checking the data", "Data found for : "+actualvalue , Status.DONE);
                                System.out.println("actual:"+actualvalue+"expected:"+expectedvalue+"are same");
                }else{
                                report.updateTestLog("Checking the data", "Data not found for : "+actualvalue , Status.FAIL);
                                System.out.println("actual:"+actualvalue+"expected:"+expectedvalue+"are not same");
                }
}

public void Check_ClickElement(By element, String nameofelement){                              
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 60);
                
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
                wait.until(ExpectedConditions.elementToBeClickable(element));                        
                driver.findElement(element).click();
                try {
                                Thread.sleep(250);
                } catch (InterruptedException e) {
                                e.printStackTrace();
                }
                System.out.println(nameofelement+" is clicked");
                report.updateTestLog("Check Presence of Element and click ", nameofelement+" is clicked", Status.DONE);
                
}

public void Java_ClickElement(By element, String nameofelement){
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
                
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
                wait.until(ExpectedConditions.elementToBeClickable(element));
                
                WebElement elem = driver.findElement(element);                        
                String js = "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';";
                ((JavascriptExecutor) driver.getWebDriver()).executeScript(js, elem);
                elem.click();
                
                report.updateTestLog("Check Presence of Element ", nameofelement+" is clicked", Status.DONE);
                System.out.println(nameofelement+" is clicked");
                try {
                                Thread.sleep(250);
                } catch (InterruptedException e) {
                                e.printStackTrace();
                }
}
public void Scrollview( By OR,String Operation) {
                try{
                                if(Operation.equalsIgnoreCase("Scroll")){
                                ((JavascriptExecutor)driver.getWebDriver()).executeScript("arguments[0].scrollIntoView(true);",driver.findElement(OR));
                                }else if(Operation.equalsIgnoreCase("Click")){
                                                ((JavascriptExecutor)driver.getWebDriver()).executeScript("arguments[0].click();",driver.findElement(OR));
                                }
                }catch(Exception e){
                                System.out.println("Scrollview  error "+e);
                                report.updateTestLog("Scroll to view ", "Element is not Displayed unable to "+Operation, Status.FAIL);
                }
                                
                }
public void ScrollviewElement( WebElement OR,String Operation) {
                try{
                                if(Operation.equalsIgnoreCase("Scroll")){
                                                ((JavascriptExecutor)driver.getWebDriver()).executeScript("arguments[0].scrollIntoView(true);",OR);
                                }else if(Operation.equalsIgnoreCase("Click")){
                                                ((JavascriptExecutor)driver.getWebDriver()).executeScript("arguments[0].click();",OR);
                                }
                }catch(Exception e){
                                System.out.println("Scrollview  error "+e);
                                report.updateTestLog("Scroll to view ", "Element is not Displayed unable to "+Operation, Status.FAIL);
                }
                                
                }



public boolean Check_Page_bool(By element, String pagename){
                boolean Check_Page=false;
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
                try{
                                if (driver.findElement(element).isDisplayed()) {
                                report.updateTestLog("Check Presence of Page " , pagename+" Page is opened successfully", Status.PASS);
                                System.out.println("Open "+pagename);
                                Check_Page=true;
                                }else{
                                                report.updateTestLog("Check Presence of Page ", pagename+" Page is not opened", Status.FAIL);                                     
                                                System.out.println("unable to Open"+pagename);
                                                Check_Page=false;
                                }
                }catch(Exception e){
                                Check_Page=false;
                }
                return Check_Page;
}


public void DriverWait(By element, String nameofthelement ){
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
                
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(element));
                wait.until(ExpectedConditions.elementToBeClickable(element));
                report.updateTestLog("Waiting for element", nameofthelement+" displayed successfully", Status.DONE);
                System.out.println(nameofthelement+" displayed successfully");
                
}





public void Check_RetrieveValue(By element, String nameofelement, String valueofelement){
                
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
                
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(element));
                if (driver.findElement(element).isDisplayed()) {
                                report.updateTestLog("Check Presence of Element ", nameofelement+" Element is displayed", Status.DONE);
                                System.out.println(nameofelement+" exists");
                }else{
                                report.updateTestLog("Check Presence of Element ", nameofelement+" Element is not displayed", Status.FAIL);                                     
                                System.out.println(nameofelement+" does not exists");
                }
                
                if (driver.findElement(element).getText().equalsIgnoreCase(valueofelement)) {
                                report.updateTestLog("Checking the Presence of text", "Data found : "+valueofelement , Status.DONE);
                                System.out.println("actual:"+driver.findElement(element).getText()+"expected:"+valueofelement+"are same");
                }else{
                                report.updateTestLog("Checking the Presence of text", "Data not found : "+valueofelement , Status.FAIL);
                                System.out.println("actual:"+driver.findElement(element).getText()+"expected:"+valueofelement+"are not same");
                }
                
}

public boolean isFileDownloaded(String filepath, String filename){
                
                boolean flag = false;                      
                File dir = new File(filepath);
                File[] dir_contents=dir.listFiles();
                
                for(int i=0;i<dir_contents.length;i++){
                                if(dir_contents[i].getName().equalsIgnoreCase(filename)){
                                                dir_contents[i].delete();
                                                return flag=true;                                                              
                                }                                                                                               
                }
                return flag=false;
}
public void wait_method(By OR){
                try{
                                WebDriverWait wait=new WebDriverWait(driver.getWebDriver(),30);
                                wait.until(ExpectedConditions.presenceOfElementLocated(OR));
                                wait.until(ExpectedConditions.elementToBeClickable(OR));
                                wait.until(ExpectedConditions.visibilityOfElementLocated(OR));
                                
                }catch(Exception e){
                                System.out.println("Wait method exception  "+e);
                                
                }
                                }
public void CheckText(By element,String value){
                
                try{
                                
                                String WebText=driver.findElement(element).getText();
                if(WebText.trim().equalsIgnoreCase(value.trim())){
                                report.updateTestLog("Sharepoint",value+" matched Sucessfully" , Status.PASS);
                }
                else {
                                report.updateTestLog("Sharepoint",value+" not matched Sucessfully" , Status.FAIL);
                }
                                                
                }catch(Exception e){
                                report.updateTestLog("Text Validation", "Unable to find Element", Status.FAIL);
                }
                
}
public void Click_Element(WebElement element, String nameofelement){     
                                
                                try{
                                                Actions actions = new Actions(driver.getWebDriver());
                                                actions.moveToElement(element);
                                                actions.click();
                                                actions.build().perform();
                                                report.updateTestLog("Check Presence of Element and click ", nameofelement+" is clicked", Status.DONE);
                                }catch(Exception e){
                                                System.out.println(nameofelement+" is not clicked"+e.toString());
                                                report.updateTestLog("Check Presence of Element and click ", nameofelement+" is not clicked", Status.FAIL);
                                }
                                
                }               
public void PageIsReady(){
                
                try {
                                
                                
                                
                                                                for (int i=1; i<250; i++){ 
                                                
                                                                                
                                if(((Long)((JavascriptExecutor)driver.getWebDriver()).executeScript("return jQuery.active") == 0)){
                                                               
                                                break;
                                }
                                else{
                                                int waittime=3*i;
                                                Thread.sleep(3000);                                      
                                                System.out.println("Page Is not loaded *** waited till the wait time of "+ waittime +" seconds***");
                                                
                                }               
                                }
                } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                }
                }
public void sql_Connect_DeimpClient() {
                
                try {
                                String cell;
                                String jenkinsRun = properties.getProperty("JenkinsCred");
                                String Username = System.getenv("DB_Username");
                                String Password = System.getenv("DB_Password");
                                Connection con;

                                // System.out.println(jenkinsRun);
                                // step1 Establish the connection by loading the driver class
                                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                                // step2 create the connection object based on the environment where execution
                                // is done

                                if (jenkinsRun.equalsIgnoreCase("True")) {
                                                System.out.println("DB connecting via SQL Server Authentication");

                                                con = DriverManager.getConnection(
                                                                                "jdbc:sqlserver://lrqct1-qwde.alight-com-ad.aws.alight.com:13163;database=Hub010_ReedServices",
                                                                                Username, Password);
                                } else {
                                                System.out.println("DB connecting via Windows Authentication");
                                                con = DriverManager.getConnection(
                                                                                "jdbc:sqlserver://lrqct1-qwde.alight-com-ad.aws.alight.com:13163;database=Hub010_ReedServices;integratedSecurity=true;");
                                }

                                if (con != null) {
                                                System.out.println("Database Connected");
                                } else {
                                                System.out.println("Database connection Failed");
                                                throw new FrameworkException("Exception in DB Connection");
                                }

                                // step3 create the statement object
                                Statement stmt = con.createStatement();

                                // step4 execute query
                                try {
                                                String clientName = dataTable.getData("Employee_Search_CC", "Client");
                                                //By above- update your respective suite's data table sheet and column 
                                                String query = "select DeimplementationDate from tblClient where clientname like '" + clientName + "'";
                                                System.out.println(query);
                                                ResultSet rs = stmt.executeQuery(query); // DTC
                                                System.out.println("Query Executed");

                                                if (rs != null) {

                                                                while (rs.next()) {
                                                                                for (int i = 1; i <= 1; i++) {

                                                                                                if (rs.getString(i) == null) {
                                                                                                                cell = rs.getString(i);
                                                                                                                System.out
                                                                                                                                                .println("Client is not deimplemented as the Deimplementation Date is " + cell);
                                                                                                                report.updateTestLog("Deimplmentation Status",
                                                                                                                                                clientName + " is not deimplemented as the date was NULL", Status.PASS);
                                                                                                } else {
                                                                                                                cell = rs.getString(i);
                                                                                                                System.out.println(clientName + " client is deimplemented on " + cell);
                                                                                                                report.updateTestLog("Deimplmentation Status",
                                                                                                                                                clientName + " is deimplemented on " + cell, Status.FAIL);
                                                                                                                throw new FrameworkException(
                                                                                                                                                "Exception in the Deimplementation Client validate method");
                                                                                                }

                                                                                }

                                                                }
                                                } else {
                                                                System.out.println("Result Set is null after executing the Query");
                                                }
                                } catch (Exception e) {
                                                System.out.println("This test case is not validating with any client");
                                                report.updateTestLog("Client Deimplementation", "This test case is not using any client for validation",
                                                                                Status.DONE);
                                }

                                // step5 close the connection object
                                con.close();

                }

                catch (Exception e) {
                                System.out.println(e);
                                throw new FrameworkException("Exception in the Deimplementation Client validate method");
                }
}
}
