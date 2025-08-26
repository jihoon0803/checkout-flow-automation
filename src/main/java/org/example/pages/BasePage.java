package org.example.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);
    
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }
    
    protected WebElement waitForVisibilityAndGetElement(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Element not visible after timeout: {}", locator);
            throw e;
        }
    }
    
    protected void clickElement(By locator) {
        try {
            WebElement element = waitForVisibilityAndGetElement(locator);
            element.click();
            logger.info("Clicked element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to click element: {}", locator, e);
            throw e;
        }
    }
    
    protected void sendKeysToElement(By locator, String text) {
        try {
            WebElement element = waitForVisibilityAndGetElement(locator);
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text '{}' into element: {}", text, locator);
        } catch (Exception e) {
            logger.error("Failed to enter text into element: {}", locator, e);
            throw e;
        }
    }
} 