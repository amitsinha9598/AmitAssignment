import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class AmazonTest {

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            driver.get("https://www.amazon.in");

            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("twotabsearchtextbox")));
            searchBox.clear();
            searchBox.sendKeys("Wrist Watches", Keys.ENTER);

            wait.until(ExpectedConditions.urlContains("k="));
            waitForPageLoad(driver, wait);

            try {
                String currentUrl = driver.getCurrentUrl();
                driver.get(currentUrl + "&low-price=4000&high-price=8000");
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='a-price-whole']")));
                waitForPageLoad(driver, wait);
                System.out.println("Price filter (4000-8000) applied.");
            } catch (Exception e) {
                System.err.println("Failed to apply price filter: " + e.getMessage());
            }

            applyFilter(driver, wait, "Analogue");
            applyFilter(driver, wait, "Leather");
            applyFilter(driver, wait, "Titan");
            applyFilter(driver, wait, "25% Off or more");

            extractProductDetails(driver, wait);

            
        } finally {
            driver.quit();
        }
    }

    private static void waitForPageLoad(WebDriver driver, WebDriverWait wait) {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    public static void applyFilter(WebDriver driver, WebDriverWait wait, String filterName) {
        try {
            waitForPageLoad(driver, wait);

            By locator = By.xpath("//span[normalize-space(text())='" + filterName + "']");

            if (driver.findElements(locator).isEmpty()) {
                List<WebElement> seeMoreLinks = driver.findElements(By.xpath("//span[contains(text(),'See more')]"));
                for (WebElement seeMore : seeMoreLinks) {
                    try {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", seeMore);
                    } catch (Exception ignored) {}
                }
            }

            WebElement filterOption = wait.until(ExpectedConditions.elementToBeClickable(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", filterOption);

            String urlBeforeClick = driver.getCurrentUrl();

            try {
                filterOption.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", filterOption);
            }

            try {
                wait.until(driverInstance ->
                        !driverInstance.getCurrentUrl().equals(urlBeforeClick) || isStale(driverInstance, filterOption));
            } catch (Exception ignored) {
            }

            waitForPageLoad(driver, wait);

            System.out.println("Filter '" + filterName + "' applied.");

        } catch (Exception e) {
            System.err.println("Filter '" + filterName + "' not found or click failed: " + e.getMessage());
        }
    }

    private static boolean isStale(WebDriver driver, WebElement element) {
        try {
            element.isEnabled();
            return false;
        } catch (StaleElementReferenceException e) {
            return true;
        }
    }

    private static void extractProductDetails(WebDriver driver, WebDriverWait wait) {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");

            WebElement sellingPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//span[@class='a-price-whole'])[1]")));

            String mrpText = "Not Available";
            List<WebElement> mrpElements = driver.findElements(
                    By.xpath("(//span[contains(@class,'a-text-price')]//span[@aria-hidden='true'])[1]"));
            if (!mrpElements.isEmpty()) {
                mrpText = mrpElements.get(0).getText();
            } else {
                List<WebElement> strikeElements = driver.findElements(By.xpath("(//span[@data-a-strike='true'])[1]"));
                if (!strikeElements.isEmpty()) {
                    mrpText = strikeElements.get(0).getText();
                }
            }

            String discountText = "Not Available";
            List<WebElement> discountElements = driver.findElements(By.xpath("(//span[contains(text(),'% off')])[1]"));
            if (!discountElements.isEmpty()) {
                discountText = discountElements.get(0).getText();
            }

            System.out.println("\n--- Product Details ---");
            System.out.println("Price    : Rs. " + sellingPrice.getText());
            System.out.println("MRP      : " + mrpText);
            System.out.println("Discount : " + discountText);
            System.out.println("-----------------------");

        } catch (Exception e) {
            System.err.println("Error extracting product details: " + e.getMessage());
        }
    }
}