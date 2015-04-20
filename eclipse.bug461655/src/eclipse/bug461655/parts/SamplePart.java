/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package eclipse.bug461655.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SamplePart {

	private static final String POPUP_MENU_ID = "eclipse.bug461655.popupmenu";
	private static final String PART_ID = "sample.part";


	@Inject
	private MApplication application;

	@Inject
	private EModelService modelService;

	/** used to output messages */
	private StyledText styledText;

	/** used to generate menu items id */
	int progressive = 0;

	@Inject
	public SamplePart() {

	}

	@PostConstruct
	public void createComposite(Composite parent, EMenuService menuService) {
		parent.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblRclickOnThis = new Label(composite, SWT.NONE);
		lblRclickOnThis.setText("r-click on this composite to see the context menu");

		Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				add();
			}
		});
		btnAdd.setText("Add");

		Button btnRemove = new Button(composite, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				remove();
			}
		});
		btnRemove.setText("Remove");

		styledText = new StyledText(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		styledText.setAlwaysShowScrollBars(false);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		//
		print(String.format("Register context menu %s with service %s", POPUP_MENU_ID, menuService));
		menuService.registerContextMenu(composite, POPUP_MENU_ID);
	}

	/** return the part with given id */
	private MPart getPart(String partId) {
		MUIElement findElement = modelService.find(partId, application);
		if (findElement != null && findElement instanceof MPart) {
			return (MPart) findElement;
		}
		return null;
	}

	/** get the popup with given id, in the referenced part */
	private MPopupMenu getPopupMenu(MPart myPart, String menuId) {
		for (MMenu mmenu : myPart.getMenus()) {
			if (menuId.equals(mmenu.getElementId()) && mmenu instanceof MPopupMenu) {
				return (MPopupMenu) mmenu;
			}
		}
		return null;
	}

	/** Add context menu item using the progressive to generate id */
	public void add() {
		progressive++;
		String id = "added.menuitem" + progressive;
		print(">>add: " + id);
		MPart myPart = getPart(PART_ID);
		MPopupMenu popupMenu = getPopupMenu(myPart, POPUP_MENU_ID);
		//
		MHandledMenuItem menuItem = MMenuFactory.INSTANCE.createHandledMenuItem();
		menuItem.setLabel("Menu Item " + progressive);
		menuItem.setElementId(id);
		//
		popupMenu.getChildren().add(menuItem);
		//
		print("found menu: " + popupMenu);
	}

	public void remove() {
		//
		String id = "added.menuitem" + progressive;
		//
		print(">>remove: " + id);
		MPart myPart = getPart(PART_ID);
		MPopupMenu popupMenu = getPopupMenu(myPart, POPUP_MENU_ID);
		//
		if (progressive <= 0) {
			print("no more generated MenuItem");
			return;
		}
		//
		for (MMenuElement menuChild : popupMenu.getChildren()) {
			if (id.equals(menuChild.getElementId())) {
				popupMenu.getChildren().remove(menuChild);
				print("found: " + menuChild);
				break;
			}
		}
		//
		progressive--;
	}

	public void print(String s) {
		styledText.append(s + "\n");
	}
}