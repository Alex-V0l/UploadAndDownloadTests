package extensions;

import AllureSteps.AllureSteps;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AllureExtension implements AfterTestExecutionCallback{

   AllureSteps allureSteps = new AllureSteps();

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isPresent()) allureSteps.captureScreenshotSpoiler();
    }
}
