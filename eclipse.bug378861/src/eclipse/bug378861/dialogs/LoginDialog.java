package eclipse.bug378861.dialogs;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Login dialog that should use a Menu
 * @author p.suzzi
 *
 */
public class LoginDialog extends TitleAreaDialog {

	// Widgets
	private Text textUsername;
	private Text textPassword;

	@Inject
	MApplication application;

	@Inject
	EMenuService menuService;

	public LoginDialog() {
		this(null);
	}

	@Inject
	public LoginDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle("Login");
		setMessage("Please provide credentials");
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		System.out.println("APP: " + application);
		System.out.println("MNU: " + menuService);

		Composite container = new Composite(area, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		new Label(container, SWT.NULL).setText("Username");
		textUsername = new Text(container, SWT.BORDER);
		textUsername.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(container, SWT.NULL).setText("Password");
		textPassword = new Text(container, SWT.PASSWORD | SWT.BORDER);
		textPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// register menu to part
		// menuService.registerContextMenu(area, InitialPart.POPUP_MENU_ID);

		return area;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 300);
	}
	
}
