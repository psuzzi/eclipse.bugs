package ch.sbb.contextmenu.sample.project.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import ch.sbb.contextmenu.sample.project.model.MyModel;
import ch.sbb.contextmenu.sample.project.providers.MyContentProvider;
import ch.sbb.contextmenu.sample.project.providers.MyLabelProvider;

public class MyPart {
	private Composite innerParent;
    @Inject
    private Composite parent;
    @Inject
    private EMenuService menuService;
    @Inject
    private EModelService modelService;
    @Inject
    private MApplication application;
	MPopupMenu contextMenu;

    private static final String CONTRIBUTION_URI = "bundleclass://ch.sbb.contextmenu.sample.project/" //$NON-NLS-1$
            + "ch.sbb.contextmenu.sample.project.handlers.ContextMenuHandler"; //$NON-NLS-1$

    private MyModel createModel() {
		MyModel root = new MyModel(0, null);
		root.counter = 0;

		MyModel tmp;
		for (int i = 1; i < 10; i++) {
			tmp = new MyModel(i, root);
			root.child.add(tmp);
			for (int j = 1; j < i; j++) {
				tmp.child.add(new MyModel(j, tmp));
			}
		}
		return root;
	}


    public MPopupMenu createContextMenu() {
    	MPart part = retrievePart();
    	
    	for( MMenu m : part.getMenus() ){
    		if( m.getElementId().equals("popupMenu") ){
    			return (MPopupMenu)m;
    		}
    	}
    	
        MPopupMenu contextMenu = modelService.createModelElement(MPopupMenu.class);
        contextMenu.setElementId("popupMenu");

        MDirectMenuItem menuItem = modelService.createModelElement(MDirectMenuItem.class);
        menuItem.setLabel("My first Menu Item"); // start value
        menuItem.setContributionURI(CONTRIBUTION_URI);

        contextMenu.getChildren().add(menuItem);
        part.getMenus().add(contextMenu);
        return contextMenu;
    }

    private MPart retrievePart() {
        final MPart part = (MPart) modelService.find("contextmenuproject.part.mypart", application);
        return part;
	}


	/**
     * Dynamically create a menu item and assign it to the context menu
     *
     * @param contextMenu
     * @param itemLabel
     */
    public   void generateContextMenuItems(MPopupMenu contextMenu, String label, boolean clear) {
      if( clear ){
    	contextMenu.getChildren().clear();
    }
    	   System.out.println("Context menu children count: " +contextMenu.getChildren().size() + " so the items were deleted.");
    	 MDirectMenuItem menuItem = MMenuFactory.INSTANCE.createDirectMenuItem();
         menuItem.setLabel(label); // start value
         menuItem.setContributionURI(CONTRIBUTION_URI);
         contextMenu.getChildren().add(menuItem);
         System.out.println("Context menu children count: " +contextMenu.getChildren().size() + " so the item seems to be in now but doesn't get displayed.");
    }


    @PostConstruct
    private void createUi() {

        innerParent = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        innerParent.setLayout(layout);

        final GridTreeViewer viewer = new GridTreeViewer(innerParent,SWT.FULL_SELECTION);

		GridColumn column = new GridColumn(viewer.getGrid(),SWT.NONE);
		column.setWidth(200);
		column.setText("Column 1");
		column.setTree(true);

		column = new GridColumn(viewer.getGrid(),SWT.NONE);
		column.setWidth(200);
		column.setText("Column 2");

		viewer.setCellEditors(new CellEditor[]{new TextCellEditor(viewer.getGrid()), new TextCellEditor(viewer.getGrid())});
		viewer.setColumnProperties(new String[]{"col1","col2"});
		viewer.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				return true;
			}

			public Object getValue(Object element, String property) {
				return ((MyModel)element).counter+"";
			}

			public void modify(Object element, String property, Object value) {
				((MyModel)((GridItem)element).getData()).counter = Integer.parseInt(value.toString());
				viewer.update(((GridItem)element).getData(), null);
			}

		});
		viewer.setLabelProvider(new MyLabelProvider());
		viewer.setContentProvider(new MyContentProvider());
		viewer.setInput(createModel());


		  contextMenu =  createContextMenu();
		  menuService.registerContextMenu(viewer.getControl(), "popupMenu");
		viewer.getGrid().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent event) {
                Point mouseRightClickPosition = new Point(event.x, event.y);
                GridItem itemRightClicked = viewer.getGrid().getItem(mouseRightClickPosition);
                GridColumn rightClickedColumn = viewer.getGrid().getColumn(mouseRightClickPosition);
                int columnIndex = getColumnIndex(rightClickedColumn, viewer.getGrid());
                if (itemRightClicked == null) {
                    // clicked on the header of the table or outside the table. Don't show the menu.
                    columnIndex = -1;
                } else {
                    MyModel myModel = (MyModel) itemRightClicked.getData();

                    if (event.button == 3) { // right click

                        // dynamically create context menu entries depending on the column
                        switch (columnIndex) {
                            case 1: /* haltecode column */
                                if (myModel.getCounter() >1) {
                                  for( int i = myModel.getCounter(); i > 0; i--){
                                      generateContextMenuItems(contextMenu,
                                              "Menu Item for column: "+ columnIndex + " item: " + itemRightClicked.getText() +  " (" + i + ')', (i == myModel.getCounter()));
                                  }
                                } else {
                                    columnIndex = -1;
                                }
                                break;
                            default:
                                columnIndex = -1;
                        }
                    }
                }
            }

            public   int getColumnIndex(GridColumn column, Grid grid) {
                int index = 0;
                for (int i = 0; i < grid.getColumnCount(); i++) {
                    if (grid.getColumn(i).equals(column)) {
                        index = i;
                    }
                }
                return index;
            }
		});
    }
}

