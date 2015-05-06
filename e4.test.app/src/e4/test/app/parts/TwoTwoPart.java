 
package e4.test.app.parts;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import org.eclipse.swt.widgets.Composite;

public class TwoTwoPart {
	
	public static String ELEMENT_ID = "e4.test.app.part.twoTwo";
	
	@Inject
	public TwoTwoPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
	}
	
	
	
	
}