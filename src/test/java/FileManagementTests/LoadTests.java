package FileManagementTests;

import FileManagementPages.LoadPage;
import extensions.AllureExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static FileManagementPages.LoadPage.DOWNLOAD_FILES_URL;
import static FileManagementPages.LoadPage.WEB_FORM_URL;

@ExtendWith(AllureExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoadTests extends BaseTest{

    private LoadPage loadPage;

    @BeforeEach
    void setUpLoadTests(){
        loadPage = new LoadPage(driver);
    }

    @DisplayName("upload test")
    @Tags({@Tag("smoke"), @Tag("UI")})
    @Test
    void uploadFileTest() throws InterruptedException {
        loadPage.openPage(WEB_FORM_URL);

        String fileName = "TextToLoad.txt";

        loadPage.uploadFile(fileName, LoadTests.class);
        loadPage.pressSubmitButton();

        Thread.sleep(3000);

        boolean isContainsFileName = loadPage.isContainsFileNameInURL(fileName);

        Assertions.assertTrue(isContainsFileName, "There must be file's name in the new page's url");
    }

    @DisplayName("download test")
    @Tags({@Tag("smoke"), @Tag("UI")})
    @ParameterizedTest
    @MethodSource("dataProvider")
    void downloadTest(String fileName, String elementLocator) throws IOException {
        loadPage.openPage(DOWNLOAD_FILES_URL);

        File createdFile = new File(".", fileName);
        loadPage.download(loadPage.getLinkFromElementToDownload(loadPage.elementToDownload(elementLocator)), createdFile);

        Assertions.assertTrue(loadPage.IsFileDownloaded(createdFile), "File must have been downloaded");
    }

    public Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of("webdrivermanager.png", "//a[@download='webdrivermanager.png']"),
                Arguments.of("webdrivermanager.pdf", "//a[@download='webdrivermanager.pdf']"),
                Arguments.of("selenium-jupiter.png", "//a[@download='selenium-jupiter.png']"),
                Arguments.of("selenium-jupiter.pdf", "//a[@download='selenium-jupiter.pdf']")
        );
    }
}
