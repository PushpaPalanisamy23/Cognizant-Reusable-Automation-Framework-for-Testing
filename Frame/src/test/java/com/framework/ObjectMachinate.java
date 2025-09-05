package com.cognizant.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * This Class Contains Object Healing Functionality. To Opt for this feature,
 * please reach CRAFTHelpDesk
 * 
 * @author Cognizant
 */
public class ObjectMachinate {

	private WebDriver driver;

	public ObjectMachinate(WebDriver driver) {
		this.driver = driver;
	}

	public WebElement healObject(By userDefinedLocator) {

		WebElement element;

		element = driver.findElement(userDefinedLocator);

		return element;
	}

}
