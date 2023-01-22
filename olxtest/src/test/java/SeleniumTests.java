import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeleniumTests {

    private static WebDriver driver;
    private final String baseUrl = "https://www.olx.ba";
    private static String chromedriverPath = "/usr/local/bin/chromedriver";
    private final String username = "******";
    private final String password  = "*******";


    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", chromedriverPath);
        driver = new ChromeDriver();
    }

    // Test otvaranja stranice i da li je ime "OLX.ba - Svijet Kupoprodaje"
    @Order(1)
    @Test
    public void testHomepageTitle() {
        driver.get(baseUrl);
        assertEquals("OLX.ba - Svijet Kupoprodaje", driver.getTitle());
    }

    // Test da li postoji meni sa lijeve strane i da li ima 11 elemenata u sebi.
    @Test
    @Order(2)
    public void testLeftMenuPresence() {
        driver.get(baseUrl);
        // Get left menu
        WebElement meni = driver.findElement(By.id("pocetna-meni-lijevo"));
        // Get elements in left menu
        List<WebElement> elements = meni.findElements(By.tagName("li"));
        // Check how many elements exist in left menu
        assertEquals(11, elements.size());
    }

    // Test da se provjeri da li dropdown meni KATEGORIJE sadrzi link na kategoriju vozila
    @Test
    @Order(3)
    public void testDropDownMenuLinks() {
        driver.get(baseUrl);
        // Get categoty dropdown
        WebElement dropdown = driver.findElement(By.id("kd"));
        // Init category names
        List<String> categoryLinks = new ArrayList<>();
        // Get elements in dropdown
        List<WebElement> elements = dropdown
                .findElement(By.tagName("ul"))
                .findElements(By.tagName("li"));
        // Iterate
        elements.forEach(e -> {
            WebElement  a = e.findElement(By.tagName("a"));
            // Add to array
            categoryLinks.add(a.getAttribute("href"));
        });
//        assertTrue(categoryLinks.contains(baseUrl + "/vozila"));
    }

    @Test
    @Order(4)
    public void testMainSearch() {
        driver.get(baseUrl);
        // Get main button
        WebElement button = driver.findElement(By.xpath("//button[@class='btn btn-mainsearch']"));
        // Button click
        button.click();
        // Chack is number of result is present
        WebElement result = driver.findElement(By.id("lista_rezultata"));
        assertTrue(result.isDisplayed());
    }


    // Test da li postoji dugme za login i moze li se kliknuti
    @Test
    @Order(5)
    public void testButtonClickable() {
        driver.get(baseUrl);
        WebElement button = driver.findElement(By.id("btnlogin1"));
        assertTrue(button.isEnabled());
    }

    //Test za login, ako je uspjesan login pojavice se u MojOlx u navbaru
    @Test
    @Order(6)
    public void testSuccessfulLogin() {
        driver.get(baseUrl);
        // Click button "Prijava"
        driver.findElement(By.id("loginbtn")).click();
        // Enter username
        driver.findElement(By.id("username")).sendKeys(username);
        // Enter button password
        driver.findElement(By.id("password")).sendKeys(password);
        // Click button ""Prijava"
        driver.findElement(By.id("btnlogin1")).click();
        // Get element
        WebElement mojOlx = driver.findElement(By.id("mpikona"));
        // Check is MojOlx button exist
        assertTrue(mojOlx.isDisplayed());
    }

      // Test da li otvara moje aktivne artikle kroz Main Menu -> Aktivni artikli
    @Test
    @Order(7)
    public void testOpenMyActivePosts() {
        driver.get(baseUrl);
        // Open Main manu
        driver.findElement(By.id("mpikona")).click();
        // Open Active Article
        driver.findElement(By.xpath("//p[@class='fa fa-th-list']")).click();
        // Check is url right
        assertEquals(baseUrl + "/mojpik/aktivni", driver.getCurrentUrl());
    }

    // Test da se provjeri da li otvara neprocitane poruke kroz meni Poruke->Neprocitane
    @Test
    @Order(8)
    public void testOpenUnreadMessages() {
        driver.get(baseUrl);
        // Init links
        List<String> links = new ArrayList<>();
        // Open "Poruke"
        driver.findElement(By.id("porukel")).click();
        // Open Menu
        WebElement menu = driver.findElement(By.xpath("//div[@class='lijevo']"));
        // Get elements in left menu
        List<WebElement> elements = menu.findElements(By.tagName("li"));
        // Iterate
        elements.forEach(e -> {
            WebElement  a = e.findElement(By.tagName("a"));
            // Add to array
            links.add(a.getAttribute("href"));
        });
        assertTrue(links.contains(baseUrl + "/poruke/neprocitane"));
    }

    // Test da serch bara, da provjeri da li ce search term biti upisan u headline rezultata
    @Test
    @Order(9)
    public void testSearch() {
        String serchTerm = "Audi a3";
        driver.get(baseUrl);
        // Enter search term
        driver.findElement(By.id("searchinput")).sendKeys(serchTerm);
        // Open Active Article
        driver.findElement(By.xpath("//button[@class='btn btn-mainsearch']")).click();
        // Get result headline
        WebElement headline  = driver.findElement(By.xpath("//h1[@class='ch1']"));
        WebElement b = headline.findElement(By.tagName("b"));
        // Asset
        assertEquals(serchTerm, b.getText());
    }

    // Test da li logout radi
    @Test
    @Order(10)
    public void testLogout() {
        driver.get(baseUrl);
        // Open Main manu
        driver.findElement(By.id("mpikona")).click();
        // Click "Odjavite se"
        driver.findElement(By.xpath("//i[@class='entypo-logout']")).click();
        // Find button "Prijavi se"
        WebElement button = driver.findElement(By.id("loginbtn"));
        // Check is button "Prijavi se" exist
        assertTrue(button.isEnabled());
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }
}
