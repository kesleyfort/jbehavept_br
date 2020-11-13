package br.com.calcard.configuration;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.ConsoleOutput;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.MarkUnmatchedStepsAsPending;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.jbehave.web.selenium.WebDriverHtmlOutput;
import org.springframework.context.ApplicationContext;

import java.util.Locale;
import java.util.Properties;

public abstract class AbstractStoryConfiguration extends JUnitStories {

    protected ApplicationContext context;

    public AbstractStoryConfiguration() {
        context = getAnnotatedApplicationContext();

        Embedder embedder = configuredEmbedder();
        String DEFAULT_STORY_TIMEOUT_SECS = "7200";
        embedder.embedderControls().doIgnoreFailureInStories(true)
                .useStoryTimeouts(DEFAULT_STORY_TIMEOUT_SECS)
                .doFailOnStoryTimeout(false)
                .doGenerateViewAfterStories(true)
                .doIgnoreFailureInView(false)
                .doVerboseFailures(true);
    }

    @Override
    public Configuration configuration() {
        Keywords keywords = new LocalizedKeywords(new Locale("pt"));
        Properties properties = new Properties();
        properties.setProperty("encoding", "UTF-8");
        StoryReporterBuilder reporterBuilder = new StoryReporterBuilder().withFormats(storyFormat())
                .withFailureTraceCompression(true).withKeywords(keywords).withViewResources(properties);


        return new MostUsefulConfiguration()
                .useKeywords(keywords)
                .useStepCollector(new MarkUnmatchedStepsAsPending(keywords))
                .useStoryParser(new RegexStoryParser(keywords))
                .useStoryReporterBuilder(reporterBuilder)
                .useDefaultStoryReporter(new ConsoleOutput(keywords))
                .useStoryControls(new StoryControls().doResetStateBeforeScenario(true))
                .useParameterControls(new ParameterControls().useDelimiterNamedParameters(true));
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new SpringStepsFactory(configuration(), context);
    }

    protected Format[] storyFormat() {
        Format[] formats = new Format[]{Format.IDE_CONSOLE, Format.STATS, WebDriverHtmlOutput.WEB_DRIVER_HTML};
        return formats;
    }

    public abstract ApplicationContext getAnnotatedApplicationContext();
}