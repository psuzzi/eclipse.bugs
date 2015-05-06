 
package e4.test.app.parts;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TwoOnePart {
	
	public static final String PART_ID = "e4.test.app.part.twoOne";
	
	@Inject
	EModelService modelService;
	MApplication application;
	MWindow window;
	
	private Text text;
	private StyledText styledText;
	
	@Inject
	public TwoOnePart(MApplication application, MWindow window) {
		this.application = application;
		this.window = window;
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Button btnSearch = new Button(composite, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		btnSearch.setText("Search");
		
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblList = new Label(composite, SWT.NONE);
		lblList.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblList.setText("list:");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		styledText = new StyledText(scrolledComposite, SWT.BORDER);
		scrolledComposite.setContent(styledText);
		scrolledComposite.setMinSize(styledText.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	private void search(){
		print(" --- Search Results --- ");
		List<MPart> elements = modelService.findElements(window, null, MPart.class, null, EModelService.IN_MAIN_MENU);
		for(MPart e : elements){
			print(""+e);
		}
		print(" --- --- --- ");
	}
	
	public void print(String s) {
		styledText.append(s + "\n");
	}
	
	
}