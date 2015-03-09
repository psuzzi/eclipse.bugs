 
package eclipse.bug378861.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class InitialPart {
	
	@Inject
	EMenuService menuService;
	
	public static final String POPUP_MENU_ID = "eclipse.bug378861.popupmenu.0";
	
	@Inject
	public InitialPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		System.out.println( "APart postConstruct, initialized, menu service:" + menuService);
		TableViewer viewer = new TableViewer(parent, SWT.FULL_SELECTION|SWT.MULTI);
		menuService.registerContextMenu(viewer.getControl(), POPUP_MENU_ID);
	}
	
	
	
	
}