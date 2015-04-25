package ch.sbb.contextmenu.sample.project.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;

public class ContextMenuHandler {

	@Execute
	public void myExecuteMethod(MMenuElement element) {
		System.out.println("I clicked on a context menu entry: " + element.getLabel());
	}
}
