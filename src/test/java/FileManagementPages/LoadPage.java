package FileManagementPages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.*;
import java.net.URL;

public class LoadPage extends BasePage {

    public static final String WEB_FORM_URL = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
    public static final String DOWNLOAD_FILES_URL = "https://bonigarcia.dev/selenium-webdriver-java/download.html";

    @FindBy(name = "my-file")
    private WebElement inputFile;

    @FindBy(xpath = "//button[text()='Submit']")
    private WebElement submitButton;

    public LoadPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("upload file")
    public void uploadFile(String fileName, Class<?> resourceClass) throws InterruptedException {
        URL url = resourceClass.getClassLoader().getResource(fileName);
        String absolutePath = null;
        if (url != null) {
            absolutePath = new File(url.getPath()).getAbsolutePath();
        } else {
            throw new InterruptedException("Resource haven't found");
        }
        inputFile.sendKeys(absolutePath);
    }

    @Step("press submit button")
    public void pressSubmitButton() {
        submitButton.click();
    }

    @Step("check that there is file's name in the link")
    public boolean isContainsFileNameInURL(String fileName) {
        if (driver.getCurrentUrl().contains(fileName)) {
            return true;
        } else {
            throw new RuntimeException("Link doesn't contain file name");
        }
    }

    @Step("open navigation page")
    public void openPage(String Url) {
        driver.get(Url);
    }

    @Step("Download file: {destination}")
    public void download(String link, File destination) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpUriRequestBase request = new HttpGet(link);
            client.execute(request, (HttpClientResponseHandler<Void>) response -> {
                try (InputStream inputStream = response.getEntity().getContent()) {
                    FileUtils.copyInputStreamToFile(inputStream, destination);
                }

                try (InputStream fileStream = new FileInputStream(destination)) {
                    Allure.addAttachment(destination.getName(), fileStream);
                }
                return null;
            });
        }
    }

    @Step("get link as String from element")
    public String getLinkFromElementToDownload(WebElement elementToDownload){
        return elementToDownload.getDomProperty("href");
    }

    @Step("check that downloaded file exists")
    public boolean IsFileDownloaded(File fileName){
        return fileName.exists();
    }

    @Step("get element to download")
    public WebElement elementToDownload(String elementLocator){
        return driver.findElement(By.xpath(elementLocator));
    }
}
