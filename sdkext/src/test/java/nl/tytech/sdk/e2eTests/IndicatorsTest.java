package nl.tytech.sdk.e2eTests;

import java.util.Collection;

import javax.security.auth.login.LoginException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import login.ProjectException;
import nl.tytech.core.client.event.EventManager;
import nl.tytech.core.net.serializable.MapLink;
import nl.tytech.data.engine.item.Indicator;
import nl.tytech.data.engine.item.Stakeholder;

public class IndicatorsTest {

	private GameField gameField;

	/**
	 * login, create Game field.
	 */
	@Before
	public void before() throws LoginException, ProjectException {
		gameField = new GameField();
	}

	@After
	public void after() throws InterruptedException, ProjectException {
		gameField.close();
	}

	@Test
	public void checkIndicators() throws InterruptedException {
		MyStakeholder municipality = gameField.addStakeholder(Stakeholder.Type.MUNICIPALITY);
		municipality.getEventHandler().waitForFirstUpdate(5000);

		Collection<Indicator> indicators = EventManager.<Indicator> getItemMap(MapLink.INDICATORS).values();

	}
}
