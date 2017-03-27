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
 * For Tests scenarios description please read README.md in the root folder of the project
 * Credentials and Variables must be filled-in before running the test!
 */
public class Test_abv_bg {

    private WebDriver driver;

    /* Credentials and Variables - Must be filled-in before running the test! */

    // Email credentials
    private static final String VALID_EMAIL = " "; // your email address in abv.bg
    private static final String VALID_PASSWORD = " "; // your email password in abv.bg

    // Email variables
    private static final String USER_NAME = "Poster Inc"; // your user name in the mail website
    private static final String LOGGED_IN_URL = "https://nm70.abv.bg/Mail.html"; // this URL could change from time to time to, e.g.: https://nm80.*

    // Version Control variables
    private static final String VC_URL = "https://github.com/vankatabe/SoftUni-QA_Course-QA_excercises2-Tests-abv_bg/issues/new"; // your version control website URL - include the path to the project repo
    private static final String VC_USERNAME = " "; // your version control website username
    private static final String VC_PASSWORD = " "; // your version control website password

    //Path to Firefox Selenium driver on your machine
    private static final String LOCAL_PATH_TO_SELENIUM_DRIVER = "C:\\Users\\vaneto\\Documents\\QA\\geckodriver-v0.15.0-win64\\geckodriver.exe";
    private String loginIssueTitleString = "Login functionality does not work";

    @Before
    public void SetUp() {
        System.setProperty("webdriver.gecko.driver", LOCAL_PATH_TO_SELENIUM_DRIVER);
        this.driver = new FirefoxDriver();
        this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        loginIssueTitleString = "Login functionality does not work";
    }

    private void login() { // method for log in the webmail website

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

    private void loginVc() { // method for log in the Version control website - GitHub

        this.driver.get("https://github.com/login"); // open Sign in URL

        // Enter credentials and click Sign in button
        WebElement usernameField = this.driver.findElement(By.id("login_field"));
        usernameField.clear();
        usernameField.sendKeys(VC_USERNAME);

        WebElement passwordField = this.driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys(VC_PASSWORD);
        WebElement loginButton = this.driver.findElement(By.xpath("//div[@id='login']/form/div[4]/input[3]"));
        loginButton.click();
        this.driver.findElement(By.id("user-links")); // Wait for Home page to load
        this.driver.get(VC_URL);

    }

    private void submitIssue(String issueStringToAppend, String failedTestName, String expectedResult, String actualResult){ //method to submit issue in VCS

        WebElement issueTitle = this.driver.findElement(By.id("issue_title"));
        issueTitle.clear();
        issueTitle.sendKeys(loginIssueTitleString + " - " + issueStringToAppend);

        WebElement issueBody = this.driver.findElement(By.id("issue_body"));
        issueBody.clear();
        issueBody.sendKeys("Failed test ID string: " + failedTestName + "\r\n");
        issueBody.sendKeys("Expected result: " + expectedResult + "\r\n" + "Actual result: " + actualResult);

        WebElement submitIssueButton = this.driver.findElement(By.xpath("//form[@id='new_issue']/div[2]/div/div/div/div[3]/button"));
        submitIssueButton.click();
    }

    @Test
    public void testLogin_validCredentials_expectedNavigationToWebmailHomepage() {
        String testName = new Object() {}.getClass().getEnclosingMethod().getName(); // get the current test (method) name

        this.login(); // call the login method
        this.driver.findElement(By.id("middlePagePanel")); // Wait for Home page to load
        String loggedInUrl = this.driver.getCurrentUrl();

        // Submit issue in VCS if URL is wrong - to test only this excerpt of the code, you can concatenate some dummy symbol to 'getCurrentUrl()' on the next row
        boolean validLoggedInUrl = LOGGED_IN_URL.equals(loggedInUrl);
        if (!validLoggedInUrl){
            String loginIssueTitleStringAppend = "URL does not change to the expected value after login";
            this.loginVc();
            this.submitIssue(loginIssueTitleStringAppend, testName, LOGGED_IN_URL, loggedInUrl); // name of failed test, expected result, actual result
        }

        // Assert if website URL is correct after log in
        Assert.assertEquals("URL should be " + LOGGED_IN_URL, LOGGED_IN_URL, loggedInUrl);
    }

    @Test
    public void testLogin_validCredentials_expectedDisplayUsernameAfterGreeting() {
        String testName = new Object() {}.getClass().getEnclosingMethod().getName(); // get the current test (method) name

        this.login(); // call the login method

        WebElement userName = this.driver.findElement(By.className("userName"));
        String loggedInUsername = userName.getText();

        // Submit issue in VCS if username is wrong - to test only this excerpt of the code, you can concatenate some dummy symbol to 'getCurrentUrl()' on the next row
        boolean validLoggedInUsername = USER_NAME.equals(loggedInUsername);
        if (!validLoggedInUsername){
            String loginIssueTitleStringAppend = "wrong username shown after login";
            this.loginVc();
            this.submitIssue(loginIssueTitleStringAppend, testName, USER_NAME, loggedInUsername); // name of failed test, expected result, actual result
        }

        // Assert if username is correct after log in
        Assert.assertEquals("Username should be " + USER_NAME, USER_NAME, loggedInUsername);
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
        this.driver.quit();
    }

}
