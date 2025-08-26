import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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

/**
 * Mobile E2E tests for Virtual Account Payment using Chrome mobile emulation.
 */
@Epic("Payment Processing")
@Feature("Virtual Account Payment - Mobile")
public class VirtualAccountPaymentMobile {
    private static final Logger logger = LoggerFactory.getLogger(VirtualAccountPaymentMobile.class);
    private static final String BASE_URL = "https://example.com/payment";
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    /**
     * Test data for payment scenarios.
     */
    private static final class TestData {
        static final String CUSTOMER_NAME = "김지훈";
        static final String PHONE_NUMBER = "010-4935-4537";
        static final String REFUND_ACCOUNT_HOLDER = "박정웅";
        static final String REFUND_ACCOUNT_NUMBER = "110306197943";
    }

    private WebDriver driver;
    private Utilities paymentPage;
    private WebDriverWait wait;

    /**
     * 크롬드라이버를 모바일 환경으로 설정하고, 테스트 환경을 초기화합니다.
     * 크롬 모바일 에뮬레이션을 사용하여 iPhone 11 Pro Max 환경을 설정합니다.
     */
    @BeforeEach
    public void setup() {
        logger.info("Setting up mobile test environment");
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", 414); // iPhone 11 Pro Max width
        deviceMetrics.put("height", 896); // iPhone 11 Pro Max height
        deviceMetrics.put("pixelRatio", 3.0);

        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceMetrics", deviceMetrics);
        mobileEmulation.put("userAgent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Mobile/15E148 Safari/604.1");

        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        driver.manage().window().maximize();
        paymentPage = new Utilities(driver);
    }

    /**
     * [Mobile] Happy path: 가상계좌 결제 테스트.
     */
    @Test
    @DisplayName("[Mobile] 가상계좌 결제 테스트")
    @Tags({@Tag("E2E"), @Tag("Payment"), @Tag("Mobile")})
    @Severity(SeverityLevel.CRITICAL)
    public void testVirtualAccountPayment() {
        try {
            logger.info("Starting mobile virtual account payment test");
            paymentPage.navigateToPaymentPage(BASE_URL);
            paymentPage.completeInitialSteps();
            paymentPage.fillCustomerInformation(TestData.CUSTOMER_NAME, TestData.PHONE_NUMBER);
            paymentPage.selectVirtualAccountPayment();
            paymentPage.selectBank("Shinhan");
            paymentPage.fillRefundAccountInformation(TestData.REFUND_ACCOUNT_HOLDER, TestData.REFUND_ACCOUNT_NUMBER);
            paymentPage.selectFinalBankMobile();
            paymentPage.completePaymentConfirmationMobile();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[text()='가상계좌 발급완료']")
            ));
            logger.info("Mobile payment test completed successfully");
        } catch (Exception e) {
            logger.error("Mobile test failed", e);
            throw e;
        }
    }

    /**
     * [Mobile] Negative: Invalid customer information input.
     */
    @Test
    @Tags({@Tag("E2E"), @Tag("Negative"), @Tag("Mobile")})
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Mobile] 이름과 휴대폰번호 잘못 입력")
    public void testInvalidCustomerInfoShowsError() {
        paymentPage.navigateToPaymentPage(BASE_URL);
        paymentPage.completeInitialSteps();
        paymentPage.fillCustomerInformation("test", "123-1234-2454");
        paymentPage.selectVirtualAccountPayment();
        // Assert error message is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//*[@id=\"__next\"]/div[1]/div/form/section[1]/div/div[2]/small")
        ));
    }

    /**
     * [Mobile] 요청사항 내용 입력 및 저장 확인.
     */
    @Test
    @DisplayName("[Mobile] 요청사항 내용 입력 확인")
    public void testRequestContentInput() {
        paymentPage.navigateToPaymentPage(BASE_URL);
        paymentPage.completeInitialSteps();
        paymentPage.clickRequestButton();
        paymentPage.fillRequestContent("요청사항 내용 입력 확인");
        paymentPage.saveRequestContent();
        logger.info("[Mobile] 요청사항 내용 입력 및 저장 완료");
        try {
            By successMessageLocator = By.xpath("//*[contains(text(), '요청사항을 저장했어요')]");
            WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessageLocator));
            String actualMessage = messageElement.getText();
            Assertions.assertEquals("요청사항을 저장했어요.", actualMessage, "성공 메시지가 예상과 다릅니다.");
            logger.info("성공 메시지 검증 완료!");
        } catch (Exception e) {
            Assertions.fail("저장 완료 메시지를 찾지 못했습니다.", e);
        }
    }

    /**
     * [Mobile] Negative: 환불계좌 정보 미입력 확인.
     */
    @Test
    @Tags({@Tag("E2E"), @Tag("Negative"), @Tag("Mobile")})
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Mobile] 환불계좌 정보 미입력 확인")
    public void testRefundIncorrectInput() {
        paymentPage.navigateToPaymentPage(BASE_URL);
        paymentPage.completeInitialSteps();
        paymentPage.fillCustomerInformation(TestData.CUSTOMER_NAME, TestData.PHONE_NUMBER);
        paymentPage.selectVirtualAccountPayment();
        paymentPage.selectBank("Shinhan");
        paymentPage.fillRefundAccountInformation("박정웅", "2"); // Invalid account number
        try {
            By errorMessageLocator = By.xpath("//*[contains(text(), '존재하지 않는 계좌이거나')]");
            WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
            String actualMessage = messageElement.getText();
            Assertions.assertEquals("존재하지 않는 계좌이거나, 본인의 계좌가 아닙니다.", actualMessage);
            logger.info("에러 메시지 검증 성공!");
        } catch (Exception e) {
            logger.error("에러 메시지를 찾지 못했습니다.", e);
            Assertions.fail("에러 메시지가 예상대로 나타나지 않았습니다.");
        }
    }

    @Test
    @Tags({@Tag("E2E"), @Tag("EdgeCase")})
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("환불계좌 정보 화면에서 뒤로 가기 버튼 클릭 후 결제하기 화면으로 돌아가기")
    public void testChangeRefundBankAccount() {
        paymentPage.navigateToPaymentPage(BASE_URL);
        paymentPage.completeInitialSteps();
        paymentPage.fillCustomerInformation(TestData.CUSTOMER_NAME, TestData.PHONE_NUMBER);
        paymentPage.selectVirtualAccountPayment();
        paymentPage.selectBank("Shinhan");

        paymentPage.clickBackButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='결제하기']")));
    }

    /**
     * Clean up after each test.
     */
    @AfterEach
    public void teardown() {
        logger.info("Cleaning up mobile test environment");
        if (driver != null) {
            driver.quit();
        }
    }
}
