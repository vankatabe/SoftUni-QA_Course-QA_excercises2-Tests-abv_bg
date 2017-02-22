package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class Abv {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://nm70.abv.bg/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testAbv() throws Exception {
    driver.findElement(By.cssSelector("div > div.fl")).click();
    driver.get(baseUrl + "/Mail.html#message:fid/10:pid/0:mid/14981806667:tid/60:r/1");
    driver.findElement(By.cssSelector("div.abv-letterLinksHolder > div.abv-letterMItem")).click();
    driver.findElement(By.cssSelector("div > div.fl")).click();
    driver.findElement(By.cssSelector("div > div.fl")).click();
    driver.findElement(By.cssSelector("div > div.fl")).click();
    assertEquals("Test mail sending Selenium", driver.findElement(By.xpath("//table[@id='inboxTable']/tbody/tr/td[5]/div")).getText());
    assertEquals("Poster Inc", driver.findElement(By.xpath("//table[@id='inboxTable']/tbody/tr/td[2]/div")).getText());
    assertEquals("Poster Inc", driver.findElement(By.xpath("//table[@id='inboxTable']/tbody/tr/td[2]/div")).getText());
    assertEquals("Poster Inc", driver.findElement(By.xpath("//table[@id='inboxTable']/tbody/tr/td[2]/div")).getText());
    driver.findElement(By.cssSelector("div > div.fl")).click();
    // ERROR: Caught exception [unknown command []]
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
