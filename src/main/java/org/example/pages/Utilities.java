package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utilities extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

    /**
     * 페이지 요소를 정리하는 내부 클래스 입니다
     */
    private static final class Elements {
        // --- Initial Steps Elements ---
        static final By INITIAL_BUTTON = By.id("button-1");
        static final By SECOND_BUTTON = By.cssSelector("form div:nth-child(2) button:nth-child(2)");

        // --- Customer Information Elements ---
        static final By NAME_FIELD = By.xpath("/html/body/div/div[1]/div/form/section[1]/div/div[1]/div/div/input"); // 이름 입력 필드
        static final By PHONE_FIELD = By.xpath("/html/body/div/div[1]/div/form/section[1]/div/div[2]/div/div/input"); // 전화번호 입력 필드

        // --- Request Specific Elements ---
        static final By REQUEST_BUTTON = By.xpath("/html/body/div/div[1]/div/form/section[1]/div/div[3]/div/button/span[2]"); // 요청사항 버튼 필드
        static final By REQUEST_CONTENT_FIELD = By.cssSelector("textarea[name='memo']"); // 요청사항 입력 필드
        static final By REQUEST_SAVE_BUTTON = By.xpath("/html/body/div/div[3]/div/div[2]/button"); // 요청사항 저장 버튼

        // --- Payment Method Selection Elements ---
        static final By VIRTUAL_ACCOUNT_ICON = By.xpath("//*[@id=\"icon-account-가상계좌\"]"); // 결제 수단 가상계좌 아이콘
        static final By PAYMENT_BUTTON = By.xpath("/html/body/div/div[1]/div/form/div[2]/div[2]/button"); // 100원 결제 버튼

        // --- Iframe Related Elements ---
        static final By PAYMENT_IFRAME = By.id("___tosspayments_iframe___");

        // --- Bank Selection & Refund Information Elements ---
        static final By SHINHAN_BANK_ICON = By.cssSelector("img[src*='icn-bank-shinhan.svg']"); // 은행 선택 요소
        static final By ACCOUNT_HOLDER_FIELD = By.xpath("/html/body/div/div[1]/div/form/div[2]/div/div/input"); // 환불 이름 입력 필드
        static final By ACCOUNT_NUMBER_FIELD = By.xpath("/html/body/div/div[1]/div/form/div[3]/div/div/input"); // 환불 계좌 번호 입력 필드
        static final By BANK_SELECT_BUTTON = By.xpath("/html/body/div/div[2]/div/div[2]/button"); // 은행 선택 버튼
        static final By SHINHAN_BANK_BUTTON = By.xpath("//*[@id=\"__next\"]/div/main/section/section/ul/a[2]/li/span"); // 신한은행 버튼 (Desktop Browser)
        static final By SHINHAN_BANK_BUTTON_MOBILE = By.xpath("/html/body/div/div[1]/section/div[3]/ul/div[11]/li/div");

        // --- Confirmation & Agreement Elements ---
        static final By NO_ISSUANCE_OPTION_PHONE = By.xpath("//div[contains(text(), '소득공제용')]"); // 소득공제용 발급안함 선택 (Desktop Browser)
        static final By NO_ISSUANCE_OPTION = By.xpath("//div[contains(text(), '발급안함')]"); // 발급안함 선택 (Desktop Browser)
        static final By PRIVACY_AGREEMENT = By.xpath("//input[@aria-label='[필수] 서비스 이용 약관, 개인정보 처리 동의']"); // 개인정보 처리 동의 체크박스 (Desktop Browser)
        static final By CONFIRMATION_BUTTON = By.xpath("//button[text()='확인']"); // 확인 버튼 (Desktop Browser)
        static final By NO_USAGE_BUTTON = By.xpath("/html/body/div/div/main/div/section/div[3]/span/button"); // 사용 안함 버튼 (Desktop Browser)

       // --- Confirmation(Mobile) Elements ---
        static final By NO_ISSUANCE_OPTION_PHONE_MOBILE = By.xpath("/html/body/div[1]/div[1]/form/div[4]/div[1]/div[2]/button"); // 소득공제용 발급안함 선택 (Mobile Browser)
        static final By NO_ISSUANCE_OPTION_MOBILE = By.xpath("/html/body/div[5]/div/div/div/div[3]/div/ul/label[4]/li"); // 발급안함 선택 (Mobile Browser)
        static final By PRIVACY_AGREEMENT_MOBILE = By.xpath("/html/body/div[1]/div[1]/form/div[5]/div[1]/div[2]/div/div/div[1]/div[2]/div/span"); // 개인정보 처리 동의 체크박스 (Mobile Browser)
        static final By CONFIRMATION_BUTTON_MOBILE = By.xpath("/html/body/div[1]/div[2]/div/div/div[2]/div/button"); // 확인 버튼 (Mobile Browser)
        static final By NO_USAGE_BUTTON_MOBILE = By.xpath("/html/body/div[1]/div[2]/div/div/div[2]/span/button"); // 사용 안함 버튼 (Mobile Browser)

       
        // --- Payment Confirmation Text ---
        static final By PAYMENT_CONFIRMATION_TEXT = By.xpath("//div[contains(text(), '신한은행으로 100원 무통장입금')]"); // 마지막 확인 텍스트 요소
        
        //other elements can be added here as needed
        static final By BACK_BUTTON = By.xpath("/html/body/div/div[1]/div/nav/button[1]/span/span");
    }

    /**
     * Constructor for the Utilities class.
     * @param driver The WebDriver instance.
     */
    public Utilities(WebDriver driver) {
        super(driver);
    }

    // --- Navigation Methods ---

    /**
     * Navigates the WebDriver to the specified URL.
     * @param url The URL of the payment page.
     */
    public void navigateToPaymentPage(String url) {
        driver.get(url);
        logger.info("Navigated to payment page: {}", url);
    }

    // --- Initial Steps Methods ---

    /**
     * 첫 번째 단계에서 버튼을 클릭하여 (결제하기 버튼, 확인)
     */
    public void completeInitialSteps() {
        clickElement(Elements.INITIAL_BUTTON);
        clickElement(Elements.SECOND_BUTTON);
        logger.info("Completed initial steps.");
    }

    // --- Request Related Methods ---

    /**
     * Clicks the request button to open the request input field.
     */
    public void clickRequestButton() {
        clickElement(Elements.REQUEST_BUTTON);
        logger.info("Clicked the request button.");
    }


    /**
     * Fills the request content field with the provided text.
     * @param content The text to enter into the request field.
     */
    public void fillRequestContent(String content) {
        sendKeysToElement(Elements.REQUEST_CONTENT_FIELD, content);
        logger.info("Filled request content: {}", content);
    }

    /**
     * Clicks the save button for the request content.
     */
    public void saveRequestContent() {
        clickElement(Elements.REQUEST_SAVE_BUTTON);
        logger.info("Saved request content.");
    }

    /**
     * Clicks the back button to return to the previous page.
     */

    public void SelectclickBackButton() {
        clickElement(Elements.BACK_BUTTON);
        logger.info("Clicked the back button.");
    }

    // --- Customer Information Methods ---

    /**
     * Fills the customer's name and phone number fields.
     * @param name The customer's name.
     * @param phoneNumber The customer's phone number.
     */
    public void fillCustomerInformation(String name, String phoneNumber) {
        sendKeysToElement(Elements.NAME_FIELD, name);
        sendKeysToElement(Elements.PHONE_FIELD, phoneNumber);
        logger.info("Filled customer information.");
    }

    // --- Payment Selection Methods ---

    /**
     * Selects the virtual account payment method and clicks the payment button.
     */
    public void selectVirtualAccountPayment() {
        clickElement(Elements.VIRTUAL_ACCOUNT_ICON);
        clickElement(Elements.PAYMENT_BUTTON);
        logger.info("Selected virtual account payment method.");
    }

    /**
     * Selects Shinhan Bank by clicking its icon.
     */
    public void selectShinhanBank() {
        clickElement(Elements.SHINHAN_BANK_ICON);
        logger.info("Selected Shinhan bank.");
    }

    /**
     * Selects Shinhan Bank using a mobile-specific locator.
     */
    public void selectShinhanBankMobile() {
        clickElement(Elements.SHINHAN_BANK_BUTTON_MOBILE);
        logger.info("Selected Shinhan bank (mobile-specific method).");
    }

    /**
     * Fills the refund account holder's name and account number, then clicks the bank select button.
     * @param holderName The name of the account holder for the refund.
     * @param accountNumber The account number for the refund.
     */
    public void fillRefundAccountInformation(String holderName, String accountNumber) {
        sendKeysToElement(Elements.ACCOUNT_HOLDER_FIELD, holderName);
        sendKeysToElement(Elements.ACCOUNT_NUMBER_FIELD, accountNumber);
        clickElement(Elements.BANK_SELECT_BUTTON);
        logger.info("Filled refund account information.");
    }

    // --- Iframe Handling Methods ---

    /**
     * Switches the WebDriver's focus to the payment iframe.
     * Waits for the iframe to be present before switching.
     */
    public void switchToPaymentIframe() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(Elements.PAYMENT_IFRAME));
            driver.switchTo().frame("___tosspayments_iframe___");
            logger.info("Switched to payment iframe.");
        } catch (Exception e) {
            logger.error("Failed to switch to payment iframe", e);
            throw e; // Re-throw the exception to indicate failure
        }
    }

    // --- Payment Confirmation Methods ---

    /**
     * Completes the payment confirmation steps for desktop/general view.
     * Includes selecting no issuance options, agreeing to privacy, and clicking confirmation buttons.
     */
    public void completePaymentConfirmation() {
        clickElement(Elements.NO_ISSUANCE_OPTION_PHONE);
        clickElement(Elements.NO_ISSUANCE_OPTION);
        clickElement(Elements.PRIVACY_AGREEMENT);
        clickElement(Elements.CONFIRMATION_BUTTON);
        clickElement(Elements.NO_USAGE_BUTTON);
        logger.info("Completed payment confirmation steps.");
    }

    /**
     * Completes the payment confirmation steps for mobile view.
     * Includes selecting no issuance options, agreeing to privacy, and clicking confirmation buttons.
     */
    public void completePaymentConfirmationMobile() {
        clickElement(Elements.NO_ISSUANCE_OPTION_PHONE_MOBILE);
        clickElement(Elements.NO_ISSUANCE_OPTION_MOBILE);
        clickElement(Elements.PRIVACY_AGREEMENT_MOBILE);
        clickElement(Elements.CONFIRMATION_BUTTON_MOBILE);
        clickElement(Elements.NO_USAGE_BUTTON_MOBILE);
        logger.info("Completed payment confirmation steps for mobile.");
    }

    /**
     * Selects the final bank button after other payment details are filled.
     */
    public void selectFinalBank() {
        WebElement bankButton = waitForVisibilityAndGetElement(Elements.SHINHAN_BANK_BUTTON);
        bankButton.click();
        logger.info("Selected final bank.");
    }

    /**
     * Checks if the payment confirmation text is displayed.
     * @return true if the payment confirmation text is displayed, false otherwise.
     */
    public boolean isPaymentConfirmed() {
        WebElement confirmationText = waitForVisibilityAndGetElement(Elements.PAYMENT_CONFIRMATION_TEXT);
        boolean isConfirmed = confirmationText.isDisplayed();
        logger.info("Payment confirmation status: {}", isConfirmed);
        return isConfirmed;
    }
}