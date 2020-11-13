package br.com.calcard.steps;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.calcard.context.TestContext;

public abstract class AbstractSteps {
	
	@Autowired
	protected TestContext context;
}
