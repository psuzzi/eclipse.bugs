
package e4.test.app.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class SwitchPerspectiveHandler {

	public static final String PERSP_ONE = "";
	public static final String PERSP_TWO = "";

	@Execute
	public void execute(MPerspective activePerspective, MApplication app, EPartService partService,
			EModelService modelService, @Named("e4.test.app.command.param.perspectiveid") String perspectiveId) {
		
        List<MPerspective> perspectives = modelService.findElements(app, null, MPerspective.class, null);
        
        for (MPerspective perspective : perspectives) {
            if (!perspective.getElementId().equals(perspectiveId)) {
            	partService.switchPerspective(perspective);
            	break;
            }
        }

	}

}