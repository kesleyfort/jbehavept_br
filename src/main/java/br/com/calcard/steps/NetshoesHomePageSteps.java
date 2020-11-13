package br.com.calcard.steps;

import br.com.calcard.pages.NetshoesPage;
import br.com.calcard.utils.Utils;
import org.apache.log4j.Logger;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.web.selenium.WebDriverProvider;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class NetshoesHomePageSteps extends AbstractSteps {
    @Autowired
    NetshoesPage netshoesPage;
    @Autowired
    Utils utils;
    @Value("${home.url}")
    private String NETSHOES_HOMEPAGE_URL;

    protected Logger LOG = Logger.getLogger(this.getClass());

    @Autowired
    protected WebDriverProvider webDriverProvider;

    @Given("eu acesse a página do Netshoes")
    public void givenCustomerAccessHomePage() {
        LOG.info("Navegando para a página: " + NETSHOES_HOMEPAGE_URL);
        webDriverProvider.get().get(NETSHOES_HOMEPAGE_URL);
    }

    @Then("eu devo ver a barra de busca")
    public void thenProductListIsDisplayed() {
        Assert.assertTrue("A barra de busca deveria ter sido exibida, mas não foi."
                , netshoesPage.searchBar.isDisplayed());
    }

    @When("eu pesquiso por $something")
    public void searchForSomething(@Value("something") String item) {
        netshoesPage.searchBar.sendKeys(item);
        netshoesPage.searchBar.submit();

    }

    @Then("eu devo ver a lista de resultados")
    public void seeSomethingsList() {
        Assert.assertTrue("Lista de resultados deveria ter sido exibida, mas não foi."
                ,         utils.waitForElement(By.id("item-list")).isDisplayed());
    }

    @When("eu clico em um item aleatório")
    public void iClickOnAItem() {
        Random random = new Random();
        netshoesPage.resultsFromSearch.get(random.nextInt(netshoesPage.resultsFromSearch.size())).click();
    }

    @Then("eu devo selecionar um tamanho aleatório")
    public void iShouldSelectTheItemsSize() {
        Random random = new Random();
        List<WebElement> productSizeList = utils.waitForElements(By.xpath(netshoesPage.productSizeOptionsXpath));
        if(productSizeList.size() > 0) {
            int index = random.nextInt(productSizeList.size());
            productSizeList.get(index).click();
        } else
            Assert.fail("Lista de tamanho do produto está vazio");
    }

    @Then("eu devo adicionar o item ao carrinho")
    public void iShouldAddAItemToTheCart() {
        utils.waitForElement(By.id("buy-button-now")).click();
    }

    @When("eu preencho o CEP")
    public void iFillTheCEPField() {
        WebElement cep = utils.waitForElement(By.id("cep"));
        cep.sendKeys("80010180");
        cep.submit();

    }

    @Then("eu vejo o preço do frete")
    public void iShouldSeeTheShippingValue() {
        Assert.assertTrue("Botão Buy now deveria ter sido exibido, mas não foi."
                , netshoesPage.shippingValue.isDisplayed());
    }

}
