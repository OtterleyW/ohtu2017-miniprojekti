package ohtu;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.File;
import ohtu.controller.KirjaVinkkiController;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Stepdefs {

    WebDriver driver;
    KirjaVinkkiController kontrol;

    public Stepdefs() {
        File file;
        if (System.getProperty("os.name").matches("Mac OS X")) {
            file = new File("lib/macgeckodriver");
        } else {
            file = new File("lib/geckodriver");
        }
        String absolutePath = file.getAbsolutePath();
        System.setProperty("webdriver.gecko.driver", absolutePath);
        this.driver = new FirefoxDriver();
        kontrol = new KirjaVinkkiController();

    }

    @Given("^command lisaakirja is selected$")
    public void command_lisaakirja_is_selected() throws Throwable {
        driver.get("http://localhost:8080");
        WebElement element = driver.findElement(By.partialLinkText("lisaa kirja"));
        element.click();
        Thread.sleep(200);
    }
    
    @Given("^user has selected command Muokkaa kirjaa$")
    public void command_muokkaakirjaa_is_selected() throws Throwable {
        driver.get("http://localhost:8080/vinkit");
        WebElement element = driver.findElement(By.partialLinkText("muokkaa"));
        element.click();
        Thread.sleep(200);
    }

    
    @When("^user has entered an writer \"([^\"]*)\" and title \"([^\"]*)\"$")
    public void when_user_has_entered_an_writer_and_title(String writer, String tittle) throws Throwable {
        Thread.sleep(200);
        WebElement element = driver.findElement(By.name("kirjoittaja"));
        element.sendKeys(writer);
        Thread.sleep(200);
        element = driver.findElement(By.name("otsikko"));
        element.sendKeys(tittle);
        Thread.sleep(200);
        element = driver.findElement(By.cssSelector("input[type='submit']"));
        element.click();
        Thread.sleep(500);
    }
    

    @When("^user has selected command takaisin$")
    public void user_has_selected_command_takaisin() throws Throwable {
        WebElement element = driver.findElement(By.linkText("Takaisin"));
        element.click();
    }
    

    @Then("^new book is added$")
    public void new_book_is_added() throws Throwable {
        pageHasContent("Lisätty kirja Topologia I kirjoittajalta Jussi Väisälä! ");

    }
    
        @Then("^existing book is modified$")
    public void existing_book_is_modified() throws Throwable {
        pageHasContent("Muokattu kirja");

    }

    @Then("^user is redirect to mainpage$")
    public void user_is_redirect_to_mainpage() throws Throwable {
        Thread.sleep(300);
        WebElement element = driver.findElement(By.partialLinkText("lisaa kirja"));
        element = driver.findElement(By.partialLinkText("listaa kirjavinkit"));
        
    }

    @Then("^system sent message sent error message$")
    public void system_sent_message_sent_error_message() throws Throwable {
//         pageHasContent("Book Topologia I from writer Jussi Väisälä exist already in database");
    }

    @After
    public void tearDown() {
        driver.quit();
    }

//    private void clickLinkWithText(String text) {
//        int trials = 0;
//        while (trials++ < 5) {
//            try {
//                WebElement element = driver.findElement(By.linkText(text));
//                element.click();
//                break;
//            } catch (Exception e) {
//                System.out.println(e.getStackTrace());
//            }
//        }
//    }
    private void pageHasContent(String content) {
        assertTrue(driver.getPageSource().contains(content));
    }
}
