package com.cognizant.framework.selenium;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Settings;

/**
 * Factory class for creating the {@link WebDriver} object as required
 * 
 * @author Cognizant
 */
public class WebDriverFactory {
	private static Properties properties;

	private WebDriverFactory() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the appropriate {@link WebDriver} object based on the
	 * parameters passed
	 * 
	 * @param browser
	 *            The {@link Browser} to be used for the test execution
	 * @return The corresponding {@link WebDriver} object
	 * @throws InterruptedException 
	 */

	public static WebDriver getWebDriver(Browser browser) {
		WebDriver driver = null;
		Boolean exflag = true;
		properties = Settings.getInstance();
		switch (browser) {
		case CHROME:
			dolab:        do{
                try {

                    // Takes the system proxy settings automatically
                    System.out.println(properties.getProperty("ChromeDriverPath"));
                    System.setProperty("webdriver.chrome.driver", properties.getProperty("ChromeDriverPath"));
                    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                    capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
                    capabilities.setCapability("ignoreZoomSetting", true);
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--start-maximized");
                    options.addArguments("--disable-notifications");
                    options.addArguments("enable-automation");
                   // options.addArguments("--remote-debugging-port=9222");
                    //options.addArguments("--headless");
                    options.addArguments("--window-size=1920,1080");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-extensions");
                    options.addArguments("--dns-prefetch-disable");
                    options.addArguments("--disable-gpu");
                    options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                    driver = new ChromeDriver(capabilities);
                    driver.manage().deleteAllCookies();
                    exflag=false;
                }
                catch(Exception e) 
                {
                    System.out.println("Encountered driver exception, Trying to Lauch again!!..."+e);
                    //i+=1;
                    driver=null;                
                }

            }while(exflag);
            break;

			/*// Takes the system proxy settings automatically
			System.setProperty("webdriver.chrome.driver", properties.getProperty("ChromeDriverPath"));
			System.out.println(properties.getProperty("ChromeDriverPath"));
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("enable-automation");
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-extensions");
            options.addArguments("--dns-prefetch-disable");
            options.addArguments("--disable-gpu");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            driver = new ChromeDriver(options);
			break; */

		case FIREFOX:
			// Takes the system proxy settings automatically
			System.setProperty("webdriver.gecko.driver", properties.getProperty("GeckoDriverPath"));
			driver = new FirefoxDriver();
			break;

		case GHOST_DRIVER:
			// Takes the system proxy settings automatically (I think!)

			System.setProperty("phantomjs.binary.path", properties.getProperty("PhantomJSPath"));
			driver = new PhantomJSDriver();
			break;

		case INTERNET_EXPLORER:
			// Takes the system proxy settings automatically

			System.setProperty("webdriver.ie.driver",
					properties.getProperty("InternetExplorerDriverPath"));
			InternetExplorerOptions optionsIE  = new InternetExplorerOptions();
			optionsIE.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "https://www.guardianlife.com/");
			optionsIE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(optionsIE);
			break;

		case EDGE:
			// Takes the system proxy settings automatically
			 System.out.println(properties.getProperty("EdgeDriverPath"));
			System.setProperty("webdriver.edge.driver", properties.getProperty("EdgeDriverPath"));
			driver = new EdgeDriver();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
			
			// Checking if edge browser requires profile sign in
			Boolean Signin = driver.findElements(By.xpath("//span[contains(text(),'Sign in')]")).size()>0;
			
			if(Signin)
			{
				WebElement Signin_page = driver.findElement(By.xpath("//span[contains(text(),'Sign in')]"));
				Signin_page.click();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
			//((JavascriptExecutor) driver.executeScript("window.focus();");
			Robot robot;
			try { 
				robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				robot.delay(300);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for(String windowhandles:driver.getWindowHandles())
				{
					driver.switchTo().window(windowhandles);
				}
				} catch (AWTException e1) {
				e1.printStackTrace();
				}
		  	}
		  	else {
		  		//Driver will be initiated successfully without profile sign in required
		  	}
			driver.manage().window().maximize();
	
			break;

		case SAFARI:
			// Takes the system proxy settings automatically

			driver = new SafariDriver();
			break;

		default:
			throw new FrameworkException("Unhandled browser!");
		}

		return driver;
	}

	private static DesiredCapabilities getProxyCapabilities() {
		properties = Settings.getInstance();
		String proxyUrl = properties.getProperty("ProxyHost") + ":" + properties.getProperty("ProxyPort");

		Proxy proxy = new Proxy();
		proxy.setProxyType(ProxyType.MANUAL);
		proxy.setHttpProxy(proxyUrl);
		proxy.setFtpProxy(proxyUrl);
		proxy.setSslProxy(proxyUrl);

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);

		return desiredCapabilities;
	}

	/**
	 * Function to return the {@link RemoteWebDriver} object based on the parameters
	 * passed
	 * 
	 * @param browser        The {@link Browser} to be used for the test execution
	 * @param browserVersion The browser version to be used for the test execution
	 * @param platform       The {@link Platform} to be used for the test execution
	 * @param remoteUrl      The URL of the remote machine to be used for the test
	 *                       execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getRemoteWebDriver(Browser browser, String browserVersion, Platform platform,
			String remoteUrl) {
		// For running RemoteWebDriver tests in Chrome and IE:
		// The ChromeDriver and IEDriver executables needs to be in the PATH of
		// the remote machine
		// To set the executable path manually, use:
		// java -Dwebdriver.chrome.driver=/path/to/driver -jar
		// selenium-server-standalone.jar
		// java -Dwebdriver.ie.driver=/path/to/driver -jar
		// selenium-server-standalone.jar

		properties = Settings.getInstance();

		boolean proxyRequired = Boolean.parseBoolean(properties.getProperty("ProxyRequired"));

		DesiredCapabilities desiredCapabilities = null;
		if (proxyRequired) {
			desiredCapabilities = getProxyCapabilities();
		} else {
			desiredCapabilities = new DesiredCapabilities();
		}

		desiredCapabilities.setBrowserName(browser.getValue());

		if (browserVersion != null) {
			desiredCapabilities.setVersion(browserVersion);
		}
		if (platform != null) {
			desiredCapabilities.setPlatform(platform);
		}

		desiredCapabilities.setJavascriptEnabled(true); // Pre-requisite for
														// remote execution

		URL url = getUrl(remoteUrl);

		return new RemoteWebDriver(url, desiredCapabilities);
	}

	public static WebDriver getRemoteWebDriverCBTMob(Browser browser, String browserVersion,
			MobileExecutionPlatform mobileOs, String osVersion, String deviceName, String remoteUrl, String userName,
			String AuthKey) {
// For running RemoteWebDriver tests in Chrome and IE:
// The ChromeDriver and IEDriver executables needs to be in the PATH of the remote machine
// To set the executable path manually, use:
// java -Dwebdriver.chrome.driver=/path/to/driver -jar selenium-server-standalone.jar
// java -Dwebdriver.ie.driver=/path/to/driver -jar selenium-server-standalone.jar
		String username = userName.replaceAll("@", "%40");
		String authkey = AuthKey;
		properties = Settings.getInstance();
		boolean proxyRequired = Boolean.parseBoolean(properties.getProperty("ProxyRequired"));
		if (proxyRequired) {
			System.setProperty("http.proxyHost", properties.getProperty("ProxyHost"));
			System.setProperty("http.proxyPort", properties.getProperty("ProxyPort"));
		}

		DesiredCapabilities desiredCapabilities = null;
		desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("name", properties.getProperty("ProjectName"));
		desiredCapabilities.setCapability("build", "1.0");
		desiredCapabilities.setBrowserName(browser.getValue());
		desiredCapabilities.setCapability("deviceName", deviceName);
		desiredCapabilities.setCapability("platformVersion", osVersion);
		desiredCapabilities.setCapability("platformName", mobileOs);
		desiredCapabilities.setCapability("deviceOrientation", "portrait");
		desiredCapabilities.setCapability("record_video", "true");
		desiredCapabilities.setCapability("record_network", "true");
		desiredCapabilities.setJavascriptEnabled(true);
		String hubAddress = "http://" + username + ":" + authkey + remoteUrl;
		URL url = getUrl(hubAddress);
		return new RemoteWebDriver(url, desiredCapabilities);
	}

	public static WebDriver getRemoteWebDriverCBT(Browser browser, String browserVersion, String OSapiName,
			String remoteUrl, String userName, String AuthKey) {
// For running RemoteWebDriver tests in Chrome and IE:
// The ChromeDriver and IEDriver executables needs to be in the PATH of the remote machine
// To set the executable path manually, use:
// java -Dwebdriver.chrome.driver=/path/to/driver -jar selenium-server-standalone.jar
// java -Dwebdriver.ie.driver=/path/to/driver -jar selenium-server-standalone.jar
		String username = userName.replaceAll("@", "%40");
		System.out.println(username);
		String authkey = AuthKey;
		properties = Settings.getInstance();
		boolean proxyRequired = Boolean.parseBoolean(properties.getProperty("ProxyRequired"));
		if (proxyRequired) {
			System.setProperty("http.proxyHost", properties.getProperty("ProxyHost"));
			System.setProperty("http.proxyPort", properties.getProperty("ProxyPort"));
		}
		DesiredCapabilities desiredCapabilities = null;
		desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("name", properties.getProperty("ProjectName"));
		System.out.println(OSapiName.replaceAll("_", " "));
		desiredCapabilities.setCapability("platform", OSapiName.replaceAll("_", " "));
		System.out.println(browser.getValue().replaceAll("_", " "));
		desiredCapabilities.setCapability("browserName", browser.getValue().replaceAll("_", " "));
//	desiredCapabilities.setCapability("version", browserVersion );
		desiredCapabilities.setCapability("screenResolution", "1366x768");
		desiredCapabilities.setCapability("record_video", "true");
		desiredCapabilities.setCapability("record_network", "true");
		desiredCapabilities.setJavascriptEnabled(true);
		String hubAddress = "http://" + username + ":" + authkey + remoteUrl;
		System.out.println(hubAddress);
		URL url = getUrl(hubAddress);

		return new RemoteWebDriver(url, desiredCapabilities);
	}

	private static URL getUrl(String remoteUrl) {
		URL url;
		try {
			url = new URL(remoteUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified remote URL is malformed");
		}
		return url;
	}

	/**
	 * Function to return the {@link RemoteWebDriver} object based on the parameters
	 * passed
	 * 
	 * @param browser   The {@link Browser} to be used for the test execution
	 * @param remoteUrl The URL of the remote machine to be used for the test
	 *                  execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getRemoteWebDriver(Browser browser, String remoteUrl) {
		return getRemoteWebDriver(browser, null, null, remoteUrl);
	}

	/**
	 * Function to return the {@link ChromeDriver} object emulating the device
	 * specified by the user
	 * 
	 * @param deviceName The name of the device to be emulated (check Chrome Dev
	 *                   Tools for a list of available devices)
	 * @return The corresponding {@link ChromeDriver} object
	 */
	@SuppressWarnings("deprecation")
	public static WebDriver getEmulatedWebDriver(String deviceName) {
		DesiredCapabilities desiredCapabilities = getEmulatedChromeDriverCapabilities(deviceName);

		properties = Settings.getInstance();
		System.setProperty("webdriver.chrome.driver", properties.getProperty("ChromeDriverPath"));

		return new ChromeDriver(desiredCapabilities);
	}

	private static DesiredCapabilities getEmulatedChromeDriverCapabilities(String deviceName) {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", deviceName);

		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);

		DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

		return desiredCapabilities;
	}

	/**
	 * Function to return the {@link RemoteWebDriver} object emulating the device
	 * specified by the user
	 * 
	 * @param deviceName The name of the device to be emulated (check Chrome Dev
	 *                   Tools for a list of available devices)
	 * @param remoteUrl  The URL of the remote machine to be used for the test
	 *                   execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getEmulatedRemoteWebDriver(String deviceName, String remoteUrl) {
		DesiredCapabilities desiredCapabilities = getEmulatedChromeDriverCapabilities(deviceName);
		desiredCapabilities.setJavascriptEnabled(true); // Pre-requisite for
														// remote execution

		URL url = getUrl(remoteUrl);

		return new RemoteWebDriver(url, desiredCapabilities);
	}

	/**
	 * Function to return the {@link ChromeDriver} object emulating the device
	 * attributes specified by the user
	 * 
	 * @param deviceWidth      The width of the device to be emulated (in pixels)
	 * @param deviceHeight     The height of the device to be emulated (in pixels)
	 * @param devicePixelRatio The device's pixel ratio
	 * @param userAgent        The user agent string
	 * @return The corresponding {@link ChromeDriver} object
	 */
	@SuppressWarnings("deprecation")
	public static WebDriver getEmulatedWebDriver(int deviceWidth, int deviceHeight, float devicePixelRatio,
			String userAgent) {
		DesiredCapabilities desiredCapabilities = getEmulatedChromeDriverCapabilities(deviceWidth, deviceHeight,
				devicePixelRatio, userAgent);

		properties = Settings.getInstance();
		System.setProperty("webdriver.chrome.driver", properties.getProperty("ChromeDriverPath"));

		return new ChromeDriver(desiredCapabilities);
	}

	private static DesiredCapabilities getEmulatedChromeDriverCapabilities(int deviceWidth, int deviceHeight,
			float devicePixelRatio, String userAgent) {
		Map<String, Object> deviceMetrics = new HashMap<String, Object>();
		deviceMetrics.put("width", deviceWidth);
		deviceMetrics.put("height", deviceHeight);
		deviceMetrics.put("pixelRatio", devicePixelRatio);

		Map<String, Object> mobileEmulation = new HashMap<String, Object>();
		mobileEmulation.put("deviceMetrics", deviceMetrics);
		// mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 4.2.1;
		// en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko)
		// Chrome/18.0.1025.166 Mobile Safari/535.19");
		mobileEmulation.put("userAgent", userAgent);

		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);

		DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		return desiredCapabilities;
	}

	/**
	 * Function to return the {@link RemoteWebDriver} object emulating the device
	 * attributes specified by the user
	 * 
	 * @param deviceWidth      The width of the device to be emulated (in pixels)
	 * @param deviceHeight     The height of the device to be emulated (in pixels)
	 * @param devicePixelRatio The device's pixel ratio
	 * @param userAgent        The user agent string
	 * @param remoteUrl        The URL of the remote machine to be used for the test
	 *                         execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getEmulatedRemoteWebDriver(int deviceWidth, int deviceHeight, float devicePixelRatio,
			String userAgent, String remoteUrl) {
		DesiredCapabilities desiredCapabilities = getEmulatedChromeDriverCapabilities(deviceWidth, deviceHeight,
				devicePixelRatio, userAgent);
		desiredCapabilities.setJavascriptEnabled(true); // Pre-requisite for
														// remote execution

		URL url = getUrl(remoteUrl);

		return new RemoteWebDriver(url, desiredCapabilities);
	}

}