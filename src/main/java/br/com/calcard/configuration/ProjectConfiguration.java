package br.com.calcard.configuration;

import org.jbehave.web.selenium.PropertyWebDriverProvider;
import org.jbehave.web.selenium.RemoteWebDriverProvider;
import org.jbehave.web.selenium.WebDriverProvider;
import org.jbehave.web.selenium.WebDriverScreenshotOnFailure;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Configuration
@ComponentScan({"br.com.calcard"})
@PropertySource("classpath:configs/env.properties")
public class ProjectConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer getPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public WebDriverProvider webDriverProvider() {
        if(System.getProperty("remoteTesting")==null || System.getProperty("remoteTesting").equals("false")){
            WebDriverProvider webDriverProvider = new PropertyWebDriverProvider();

            System.setProperty("browser", "chrome");
            if (System.getProperty("webdriver.chrome.driver") == null) {
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver");
            }

            return webDriverProvider;
        }
        else {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            if(System.getProperty("browser")==null){
                capabilities.setBrowserName("chrome");
            }
            else {
                capabilities.setBrowserName(System.getProperty("browser"));
            }
            if(System.getProperty("REMOTE_WEBDRIVER_URL")==null){
                String localhost = "selenium-hub:4444";
                System.setProperty("REMOTE_WEBDRIVER_URL", "http://localhost:4444/wd/hub");
            }
            return new RemoteWebDriverProvider(capabilities);
        }

    }

    @Bean
    public WebDriverScreenshotOnFailure screenshotOnFailureDriver() {
        return new WebDriverScreenshotOnFailure(webDriverProvider());
    }

    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
