package gov.nasa.pds.web.ui.actions.dataManagement;

import gov.nasa.pds.web.ui.actions.BaseViewAction;

/**
 * Skeleton for release notes page
 * 
 * @author jagander
 */
public class ReleaseNotes extends BaseViewAction {

	private static final long serialVersionUID = 1L;

	/**
	 * Main method of the action. In this case it just sets the title.
	 */
	@Override
	protected String executeInner() throws Exception {

		// set the title key
		setTitle("releaseNotes.title"); //$NON-NLS-1$

		// just return success as no work is performed here
		return SUCCESS;
	}

}
