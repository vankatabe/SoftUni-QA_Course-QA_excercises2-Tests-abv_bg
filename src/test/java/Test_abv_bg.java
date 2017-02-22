import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by vankatabe on 21-Feb-17.
 * Tests abv.bg.
 * Credentials and Variables must be filled-in before running the test!
 */
public class Test_abv_bg {

    private WebDriver driver;

    /* Credentials and Variables - Must be filled-in before running the test! */

    // Email credentials
    private static final String VALID_EMAIL = ""; // your email address in abv.bg
    private static final String VALID_PASSWORD = ""; // your email password in abv.bg

    // Email variables
    private static final String USER_NAME = "Poster Inc"; // your user name in the mail website
    private static final String LOGGED_IN_URL = "https://nm70.abv.bg/Mail.html"; // this URL could change from time to time to, e.g.: https://nm80.*

    // Version Control variables
    private static final String VC_URL = ""; // your version control website URL - include the path to the project repo
    private static final String VC_USERNAME = ""; // your version control website username
    private static final String VC_PASSWORD = ""; // your version control website password


    @Before
    public void SetUp() {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\vaneto\\Documents\\QA\\geckodriver-v0.14.0-win64\\geckodriver.exe");
        this.driver = new FirefoxDriver();
        this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    private void login() { // method for log in the website

        this.driver.get("https://www.abv.bg/"); // open URL

        // Enter credentials and click Login button
        WebElement usernameField = this.driver.findElement(By.id("username"));
        usernameField.clear();
        usernameField.sendKeys(VALID_EMAIL);

        WebElement passwordField = this.driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys(VALID_PASSWORD);
        WebElement loginButton = this.driver.findElement(By.id("loginBut"));
        loginButton.click();
    }

    @Test
    public void testLogin_validCredentials_expectedNavigationToWebmailHomepage() {

        this.login(); // call the login method
        this.driver.findElement(By.id("middlePagePanel")); // Wait for Home page to load
        // Assert if website URL is correct after log in
        Assert.assertEquals("URL should be " + LOGGED_IN_URL, LOGGED_IN_URL, this.driver.getCurrentUrl());
    }

    @Test
    public void testLogin_validCredentials_expectedDisplayUsernameAfterGreeting() {

        this.login(); // call the login method

        // Assert if username is correct after log in
        WebElement userName = this.driver.findElement(By.className("userName"));
        Assert.assertEquals("Username should be " + USER_NAME, USER_NAME, userName.getText());
    }

    @Test
    public void TestSendMail_AllRequiredFieldsPopulated_ShouldSendAndReceiveCorrectly() {

        this.login(); // call the login method

        // Check the number of unread emails before testing
        WebElement numberOfUnreadEmailsBefore = this.driver.findElement(By.cssSelector(".abv-unreadList > div:nth-child(2) > b:nth-child(2)"));
        String numberOfUnreadEmailsBeforeString = numberOfUnreadEmailsBefore.getText();
        numberOfUnreadEmailsBeforeString = numberOfUnreadEmailsBeforeString.replaceAll("\\D+", "");
        int numberOfUnreadEmailsToIntBefore = Integer.parseInt(numberOfUnreadEmailsBeforeString);

        // Compose email and send
        WebElement composeButton = this.driver.findElement(By.cssSelector("div.abv-button"));
        composeButton.click();

        WebElement toField = this.driver.findElement(By.cssSelector(".mailFieldsContainer > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2) > div:nth-child(1) > input:nth-child(1)"));
        toField.sendKeys(VALID_EMAIL);

        WebElement subjectField = this.driver.findElement(By.className("gwt-TextBox"));
        subjectField.sendKeys("Test mail sending Selenium");

        WebElement mailBodyField = this.driver.findElement(By.className("gwt-RichTextArea"));
        mailBodyField.sendKeys("Test body of the test mail");

        WebElement sendButton = this.driver.findElement(By.cssSelector("div.abv-button"));
        sendButton.click();

        this.driver.findElement(By.id("middlePagePanel")); // Wait for confirmation "email sent" page to load

        // Go to Inbox and assert From and Subject fields contain valid test email data
        this.driver.get("https://nm70.abv.bg/Mail.html#inbox:fid/10:pid/0");
        WebElement testEmailFrom = this.driver.findElement(By.xpath("//table[@id='inboxTable']/tbody/tr/td[2]/div"));
        Assert.assertEquals(USER_NAME, testEmailFrom.getText());

        WebElement testEmailSubject = this.driver.findElement(By.xpath("//table[@id='inboxTable']/tbody/tr/td[5]/div"));
        Assert.assertEquals("Test mail sending Selenium", testEmailSubject.getText());

        // Go to mail homepage and assert the number of unread emails is now +1
        this.driver.get(LOGGED_IN_URL);

        WebElement numberOfUnreadEmailsAfter = this.driver.findElement(By.cssSelector(".abv-unreadList > div:nth-child(2) > b:nth-child(2)"));
        String numberOfUnreadEmailsAfterString = numberOfUnreadEmailsAfter.getText();
        numberOfUnreadEmailsAfterString = numberOfUnreadEmailsAfterString.replaceAll("\\D+", "");
        int numberOfUnreadEmailsToIntAfter = Integer.parseInt(numberOfUnreadEmailsAfterString);

        Assert.assertEquals(numberOfUnreadEmailsToIntBefore + 1, numberOfUnreadEmailsToIntAfter);
    }

    @After
    public void TearDown() {
        //this.driver.quit();
    }

}
