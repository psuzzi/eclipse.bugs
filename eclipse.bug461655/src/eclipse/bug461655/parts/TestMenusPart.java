
package eclipse.bug461655.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.swt.widgets.Composite;

/**
 * Test part to verify bug 461655 and 374568
 *
 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=461655
 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=374568
 * @author p.suzzi
 *
 */
public class TestMenusPart {

	public static String PART_ID = "eclipse.bug461655.part.testMenus";//$NON-NLS-1$

	private Composite parent;
	private MMenuFactory factory;

	@Inject
	public TestMenusPart(final Composite parent) {
		this.parent = parent;
		this.factory = MMenuFactory.INSTANCE;

	}

	@PostConstruct
	public void postConstruct(Composite parent) {

	}




}