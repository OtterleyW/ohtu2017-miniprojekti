package ohtu;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.File;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.web.servlet.ModelAndView;

public class Stepdefs {

    WebDriver driver;

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
    }

    @After
    public void tearDown() {
        driver.quit();
    }

//    @Given("^user is at the main page$")
//    public void user_is_at_the_main_page() throws Throwable {
//        driver.get("http://localhost:" + 8080 + "/" );
//        Thread.sleep(1000);        
//    }
//
//    @When("^a link is clicked$")
//    public void a_link_is_clicked() throws Throwable {
//        Thread.sleep(1000);  
//        clickLinkWithText("linkki" );
//        Thread.sleep(1000);  
//    }    
//   
//    @Then("^\"([^\"]*)\" is shown$")
//    public void is_shown(String arg1) throws Throwable {
//        assertTrue(driver.findElement(By.tagName("body"))
//                .getText().contains(arg1));
//    }
    @Given("^command lisaakirja is sellected$")
    public void command_lisaakirja_is_sellected() throws Throwable {
        driver.get("http://localhost:" + 8080 + "/");
        Thread.sleep(100);
        WebElement element = driver.findElement(By.linkText("Lisää kirja"));
        element.click();
        Thread.sleep(100);

    }

    @When("^user has entered an writer \"([^\"]*)\" and title \"([^\"]*)\"$")
    public void when_user_has_entered_an_writer_and_title(String kirjoittaja, String otsikko) throws Throwable {
        WebElement element = driver.findElement(By.name("kirjoittaja"));
        element.sendKeys(kirjoittaja);
        Thread.sleep(100);
        element = driver.findElement(By.name("otsikko"));
        element.sendKeys(otsikko);
        Thread.sleep(200);
//        element = driver.findElement(By.("Lisää"));
        element.submit();
        Thread.sleep(400);
    }
    @When("^user has sellected command takaisin$")
    public void user_has_sellected_command_takaisin() throws Throwable {
         WebElement element = driver.findElement(By.linkText("Takaisin"));
            element.click();
        
         }

    @Then("^user is redirect to mainpage$")
    public void user_is_redirect_to_mainpage() throws Throwable {
        pageHasContent("Lisää kirja");
   }
    @Then("^system sent message sent error message$")
    public void system_sent_message_sent_error_message() throws Throwable {
        
         }


    


    @Then("^new book is added$")
    public void new_book_is_added() throws Throwable {
        WebElement element = driver.findElement(By.linkText("Takaisin"));
        pageHasContent("Lisätty kirja Topologia I kirjoittajalta Jussi Väisälä!");
        element.click();
        Thread.sleep(200);
    }
    @Given("^user has sellected command Muokkaa kirjaa$")
public void user_has_sellected_command_Muokkaa_kirjaa() throws Throwable {
//        driver.get("http://localhost:" + 8080 + "/'>muokkaa_kirja<");
//       WebElement element = driver.findElement(By.linkText("Muokkaa kirjaa"));
//       element.click();
//       Thread.sleep(200);
    }

    @When("^user has entered writer \"([^\"]*)\" and title \"([^\"]*)\"$")
    public void user_has_entered_writer_and_title(String kirjoittaja, String otsikko) throws Throwable {
//        WebElement element = driver.findElement(By.name("kirjoittaja"));
//        element.sendKeys(kirjoittaja);
//        element = driver.findElement(By.name("otsikko"));
//        element.sendKeys(otsikko);
//        element.submit();
    }

    @When("^exixting book is modified$")
    public void exixting_book_is_modified() throws Throwable {
        }


    private void clickLinkWithText(String text) {
        int trials = 0;
        while (trials++ < 5) {
            try {
                WebElement element = driver.findElement(By.linkText(text));
                element.click();
                break;
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
    }

    private void pageHasContent(String content) {
        assertTrue(driver.getPageSource().contains(content));
    }

}
