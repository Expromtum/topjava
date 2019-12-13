package ru.javawebinar.topjava;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import static ru.javawebinar.topjava.util.sqltracker.QueryCountInfoHolder.getQueryInfo;
import ru.javawebinar.topjava.util.sqltracker.AssertSqlCount;

public class TimingExtension implements
        BeforeTestExecutionCallback, AfterTestExecutionCallback, BeforeAllCallback, AfterAllCallback {

    private static final Logger log = LoggerFactory.getLogger("result");

    private StopWatch stopWatch;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        stopWatch = new StopWatch("Execution time of " + extensionContext.getRequiredTestClass().getSimpleName());
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        String testName = extensionContext.getDisplayName();
        log.info("\nStart " + testName);

        AssertSqlCount.reset();

        stopWatch.start(testName);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        stopWatch.stop();

        System.out.printf("\nSql count: " + getQueryInfo().countAll());
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        log.info('\n' + stopWatch.prettyPrint() + '\n');
    }
}
