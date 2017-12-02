package ohtu;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.File;
import java.util.List;
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

    @Given("^command lisaavideo is selected$")
    public void command_lisaavideo_is_selected() throws Throwable {
        driver.get("http://localhost:8080");
        WebElement element = driver.findElement(By.linkText("Lisää videovinkki"));
        element.click();
        Thread.sleep(1000);
    }

    @Given("^user has selected command poista$")
    public void user_has_selected_command_poista() throws Throwable {
        driver.get("http://localhost:8080/vinkit");
        WebElement element = driver.findElement(By.partialLinkText("Poista"));
        element.click();
        Thread.sleep(1000);
    }

    @Given("^user is sellected command heakirjat$")
    public void user_is_sellected_command_heakirjat() throws Throwable {
        driver.get("http://localhost:8080");
        WebElement element = driver.findElement(By.partialLinkText("Näytä kirjavinkit"));
        element.click();
        Thread.sleep(1000);

    }

    @Given("^command lisaakirja is selected$")
    public void command_lisaakirja_is_selected() throws Throwable {
        driver.get("http://localhost:8080");
        WebElement element = driver.findElement(By.partialLinkText("Lisää kirjavinkki"));
        element.click();
        Thread.sleep(1000);
    }

    @Given("^user has selected command Muokkaa kirjaa$")
    public void command_muokkaakirjaa_is_selected() throws Throwable {
        driver.get("http://localhost:8080/vinkit");
        WebElement element = driver.findElement(By.partialLinkText("Muokkaa"));
        element.click();
        Thread.sleep(1000);
    }

    @Given("^user tries to edit a non-existing book$")
    public void edit_non_existing_book() throws Throwable {
        driver.get("http://localhost:8080/99999/muokkaa");
    }

    @Given("^command merkitseluetuksi is selected$")
    public void mark_hint_to_read() throws Throwable {
        driver.get("http://localhost:8080/vinkit");
        WebElement element = driver.findElement(By.partialLinkText("Merkitse luetuksi"));
        element.click();
        Thread.sleep(1000);
    }

    @Given("^command merkitselukemattomaksi is selected$")
    public void mark_hint_to_unread() throws Throwable {
        driver.get("http://localhost:8080/vinkit");
        WebElement element = driver.findElement(By.partialLinkText("Merkitse lukemattomaksi"));
        element.click();
        Thread.sleep(1000);
    }
    @When("^user click element lisaa$")
    public void user_click_element_lisaa() throws Throwable {
        pageHasContent("Lisää video");
        WebElement element = driver.findElement(By.cssSelector("input[type='submit']"));
        element.click();
        Thread.sleep(1000);
        }

    @When("^user has entered \"([^\"]*)\" and a url \"([^\"]*)\"$")
    public void user_has_entered_and_a_url(String otsikko, String linkki) throws Throwable {
        Thread.sleep(1000);
        pageHasContent("Lisää video");
        WebElement element = driver.findElement(By.name("otsikko"));
        element.clear();
        element.sendKeys(otsikko);
        Thread.sleep(1000);
        element = driver.findElement(By.name("url"));
        element.clear();
        element.sendKeys(linkki);
        Thread.sleep(1000);
        element = driver.findElement(By.cssSelector("input[type='submit']"));
        element.click();
        Thread.sleep(1000);
    }

    @When("^user click element Takaisin$")
    public void user_click_element_Takaisin() throws Throwable {
        pageHasContent("Lisää video");
        WebElement element = driver.findElement(By.linkText("Takaisin"));
        element.click();
        Thread.sleep(1000);
    }

    @When("^user click the element Takaisin listaukseen$")
    public void user_click_the_element_Takaisin_listaukseen() throws Throwable {
        pageHasContent("Poista kirja");
        WebElement element = driver.findElement(By.linkText("Takaisin vinkkilistaukseen"));
        element.click();
        Thread.sleep(1000);
    }

    @When("^user click element poista$")
    public void user_click_element_poista() throws Throwable {
        pageHasContent("Poista kirja");
        WebElement element = driver.findElement(By.cssSelector("input[type='submit']"));
        element.click();
        Thread.sleep(1000);
    }

    @When("^user has entered an writer \"([^\"]*)\" and title \"([^\"]*)\"$")
    public void when_user_has_entered_an_writer_and_title(String writer, String tittle) throws Throwable {
        Thread.sleep(1000);
        WebElement element = driver.findElement(By.name("kirjoittaja"));
        element.clear();
        element.sendKeys(writer);
        Thread.sleep(1000);
        element = driver.findElement(By.name("otsikko"));
        element.clear();
        element.sendKeys(tittle);
        Thread.sleep(1000);
        element = driver.findElement(By.cssSelector("input[type='submit']"));
        element.click();
        Thread.sleep(1000);
    }

    @When("^user has selected command takaisin$")
    public void user_has_selected_command_takaisin() throws Throwable {
        WebElement element = driver.findElement(By.linkText("Takaisin"));
        element.click();
    }

    @When("^page has list of all books and command Takaisin sivulle is selected$")
    public void all_existing_books_are_listed() throws Throwable {
        pageHasContent("Lisätyt lukuvinkit");
        WebElement element = driver.findElement(By.id("listId"));
        List<WebElement> kirjaLista = driver.findElements(By.tagName("td"));
        element = driver.findElement(By.linkText("Takaisin pääsivulle"));
        element.click();

    }

    @Then("^null video is not added$")
    public void null_video_is_not_added() throws Throwable {
        pageHasContent("Videon nimi tai url ei voi olla tyhjä!");
    }

    @Then("^new video is added$")
    public void new_video_is_added() throws Throwable {
        pageHasContent("Lisätty video");
    }

    @Then("^user is redirected to listing page$")
    public void user_is_redirected_to_listing_page() throws Throwable {
        pageHasContent("Lisätyt lukuvinkit");
    }

    @Then("^selected book is deleted$")
    public void selected_book_is_deleted() throws Throwable {
        pageHasContent("Poistettu kirja");

    }

    @Then("^new book is added$")
    public void new_book_is_added() throws Throwable {
        pageHasContent("Lisätty kirja Topologia I kirjoittajalta Jussi Väisälä!");
    }

    @Then("^new book is not added and error is shown$")
    public void new_book_is_not_added() throws Throwable {
        pageHasContent("Kirjan nimi tai kirjailija ei voi olla tyhjä!");
    }

    @Then("^book is not edited and error is shown$")
    public void book_is_not_edited() throws Throwable {
        pageHasContent("Kirjan nimi tai kirjailija ei voi olla tyhjä!");
    }

    @Then("^existing book is modified$")
    public void existing_book_is_modified() throws Throwable {
        pageHasContent("Muokattu kirja");

    }

    @Then("^user is redirect to mainpage$")
    public void user_is_redirect_to_mainpage() throws Throwable {
        Thread.sleep(1000);
        WebElement element = driver.findElement(By.partialLinkText("Lisää kirjavinkki"));
        element = driver.findElement(By.partialLinkText("Näytä kirjavinkit"));

    }

    @Then("^system sent message sent error message$")
    public void system_sent_message_sent_error_message() throws Throwable {
//         pageHasContent("Book Topologia I from writer Jussi Väisälä exist already in database");
    }

    @Then("^user will end up on the error page$")
    public void end_up_on_the_error_page() throws Throwable {
        pageHasContent("Tapahtui virhe");
    }

    @Then("^user return to brevious page$")
    public void user_can_return_brevious_page() throws Throwable {
        Thread.sleep(1000);
        WebElement element = driver.findElement(By.partialLinkText("Lisää kirjavinkki"));
        element = driver.findElement(By.partialLinkText("Näytä kirjavinkit"));
    }

    @Then("^the hint is marked as read$")
    public void the_hint_is_marked_as_read() throws Throwable {
        pageHasContent("Merkitse lukemattomaksi");
    }

    @Then("^the hint is marked as unread$")
    public void the_hint_is_marked_as_unread() throws Throwable {
        pageHasContent("Merkitse luetuksi");
    }

    @After
    public void tearDown() {
        driver.quit();
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
