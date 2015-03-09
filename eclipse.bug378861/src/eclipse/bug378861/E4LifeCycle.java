/*******************************************************************************
 * Copyright (c) 2014 TwelveTone LLC and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Steven Spungin <steven@spungin.tv> - initial API and implementation
 *******************************************************************************/
package eclipse.bug378861;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import eclipse.bug378861.dialogs.LoginDialog;

/**
 * This is a stub implementation containing e4 LifeCycle annotated methods.<br />
 * There is a corresponding entry in <em>plugin.xml</em> (under the
 * <em>org.eclipse.core.runtime.products' extension point</em>) that references
 * this class.
 **/
@SuppressWarnings("restriction")
public class E4LifeCycle {

	@PostContextCreate
	void postContextCreate(IEclipseContext workbenchContext) {
	}

	@PreSave
	void preSave(IEclipseContext workbenchContext) {
	}

	@ProcessAdditions
	void processAdditions(IEclipseContext workbenchContext) {
		final Shell shell = new Shell(SWT.TOOL | SWT.NO_TRIM);
		// make is the equivalent of new, but with context injection
		// the problem is the EMenuService lives in a MPart context
		LoginDialog dialog = ContextInjectionFactory.make(LoginDialog.class, workbenchContext);
		if (dialog.open() != Window.OK) {
			// close application
			System.exit(-1);
		}
		
		// continue to the initial window
	}

	@ProcessRemovals
	void processRemovals(IEclipseContext workbenchContext) {
	}
}
