package ru.netology;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardValidationTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSuccessValidation() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79998888888");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("div button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotSuccessName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("qwerty");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79998888888");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("div button")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("span.input_invalid[data-test-id='name'] .input__sub"))
                .getText()
                .trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotSuccessFirstField() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79998888888");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("div button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("span.input_invalid[data-test-id='name'] .input__sub"))
                .getText()
                .trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotSuccessFieldNumbers() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7999");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("div button")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span.input_invalid[data-test-id='phone'] .input__sub"))
                .getText()
                .trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSuccessValidationName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванова-Петрова Екатерина");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79996664444");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("div button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSuccessValidationThreeWords() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванова Петрова Елена");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79998888888");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("div button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotSuccessNameWithYo() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Рулёв Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79998888888");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("div button")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("span.input_invalid[data-test-id='name'] .input__sub"))
                .getText()
                .trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotSuccessNumbers() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+799згqt*&j");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("div button")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span.input_invalid[data-test-id='phone'] .input__sub"))
                .getText()
                .trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotSuccessNotNumberInField() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("div button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("span.input_invalid[data-test-id='phone'] .input__sub"))
                .getText()
                .trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotSuccessByCheckBox() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79996644444");
        driver.findElement(By.cssSelector("div button")).click();

        Boolean actual = driver.findElement(By.cssSelector(".input_invalid[data-test-id='agreement']")).isEnabled();

        assertEquals(true, actual);
    }

}
