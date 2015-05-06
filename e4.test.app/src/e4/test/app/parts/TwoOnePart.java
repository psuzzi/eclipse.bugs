
package e4.test.app.parts;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class TwoOnePart {

	public static final String PART_ID = "e4.test.app.part.twoOne";

	@Inject
	EModelService modelService;
	MApplication application;
	MWindow window;
	private StyledText styledText;

	private ComboViewer comboViewer;

	private Combo combo;

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

		comboViewer = new ComboViewer(composite, SWT.NONE);
		combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

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
		init();
	}

	/** defines where to search with description and search flag */
	enum SearchLocation{
		InActivePerspective(EModelService.IN_ACTIVE_PERSPECTIVE, "IN_ACTIVE_PERSPECTIVE"),
		InAnyPerspective(EModelService.IN_ANY_PERSPECTIVE, "IN_ANY_PERSPECTIVE"),
		InMainMenu(EModelService.IN_MAIN_MENU, "IN_MAIN_MENU"),
		InPart(EModelService.IN_PART, "IN_PART"),
		InSharedArea(EModelService.IN_SHARED_AREA, "IN_SHARED_AREA"),
		InTrim(EModelService.IN_TRIM, "IN_TRIM");

		public final String displayName;
		public final int searchFlag;

		private SearchLocation(int searchFlag, String displayName) {
			this.searchFlag = searchFlag;
			this.displayName = displayName;
		}
	}

	SearchLocation [] searchLocations = new SearchLocation[] { SearchLocation.InActivePerspective, SearchLocation.InAnyPerspective,
 SearchLocation.InMainMenu, SearchLocation.InPart,
			SearchLocation.InSharedArea, SearchLocation.InTrim };

	private void init(){
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				SearchLocation loc = (SearchLocation) element;
				return loc.displayName;
			}
		});
		comboViewer.setInput(searchLocations);
		comboViewer.setSelection(new StructuredSelection(SearchLocation.InAnyPerspective));
	}

	private void search(){

		ISelection selection = comboViewer.getSelection();
		if (selection.isEmpty()) {
			return;
		}
		IStructuredSelection sSelection = (IStructuredSelection) selection;
		SearchLocation loc = (SearchLocation) sSelection.getFirstElement();

		print(" --- Search Results --- ");
		List<MPart> elements = modelService.findElements(window, null, MPart.class, null, loc.searchFlag);
		for(MPart e : elements){
			print(""+e);
		}
		print(" --- --- --- ");
	}

	public void print(String s) {
		styledText.append(s + "\n");
	}


}