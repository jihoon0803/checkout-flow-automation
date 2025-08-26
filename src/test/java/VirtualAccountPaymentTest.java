import java.time.Duration;

import org.example.pages.Utilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Epic("Payment Processing")
@Feature("Virtual Account Payment")
public class VirtualAccountPaymentTest {
    private static final Logger logger = LoggerFactory.getLogger(VirtualAccountPaymentTest.class);
    private static final String BASE_URL = "https://example.com/payment";
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    // Test data
    private static final class TestData {
        static final String CUSTOMER_NAME = "김지훈";
        static final String PHONE_NUMBER = "010-4935-4537";
        static final String REFUND_ACCOUNT_HOLDER = "박정웅";
        static final String REFUND_ACCOUNT_NUMBER = "110306197943";
    }

    private WebDriver driver;
    private Utilities paymentPage;
    private WebDriverWait wait;

    @BeforeEach
    public void setup() {
        logger.info("Setting up desktop test environment");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        driver.manage().window().maximize();
        paymentPage = new Utilities(driver);
    }

    @Test
    @DisplayName("가상계좌 결제 테스트")
    @Tags({@Tag("E2E"), @Tag("Payment")})
    @Severity(SeverityLevel.CRITICAL)
    public void testVirtualAccountPayment() {
        try {
            logger.info("Starting desktop virtual account payment test");

            // Navigate and complete initial steps
            paymentPage.navigateToPaymentPage(BASE_URL);
            paymentPage.completeInitialSteps();

            // 고객 정보 입력
            paymentPage.fillCustomerInformation(TestData.CUSTOMER_NAME, TestData.PHONE_NUMBER);

            // 가상 계좌 결제 선택, 은행 선택
            paymentPage.selectVirtualAccountPayment();
            paymentPage.selectBank("Shinhan");

            // 환불계좌 정보 입력
            paymentPage.fillRefundAccountInformation(TestData.REFUND_ACCOUNT_HOLDER, TestData.REFUND_ACCOUNT_NUMBER);

            // 결제 확인
            paymentPage.switchToPaymentIframe();
            paymentPage.selectFinalBank();
            paymentPage.completePaymentConfirmation();

            // iframe에서 결제 완료 후 메인 페이지로 돌아가기
            driver.switchTo().defaultContent();
            // Verify payment confirmation
            logger.info("Waiting for payment confirmation message");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='가상계좌 발급완료']")));
            logger.info("Payment functionality test completed successfully");

        } catch (Exception e) {
            logger.error("Test failed", e);
            throw e;
        }
    }

    // --- 이하 나머지 테스트 메서드는 동일, BASE_URL/클래스명만 교체해서 사용 ---
    
    @AfterEach
    public void teardown() {
        logger.info("Cleaning up test environment");
        if (driver != null) {
            driver.quit();
        }
    }
}
