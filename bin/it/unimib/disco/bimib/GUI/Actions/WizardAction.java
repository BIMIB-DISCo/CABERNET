package it.unimib.disco.bimib.GUI.Actions;

import it.unimib.disco.bimib.GESTODifferent.GESTODifferentConstants;
import it.unimib.disco.bimib.GESTODifferent.SimulationsContainer;
import it.unimib.disco.bimib.GUI.Wizard;
import it.unimib.disco.bimib.Task.NetworkCreation;

import java.awt.event.ActionEvent;
import java.util.Properties;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.swing.DialogTaskManager;


public class WizardAction extends AbstractCyAction{

	private static final long serialVersionUID = 1L;
	private SimulationsContainer simulationsContainer;
	private CyApplicationManager appManager;
	private final CySwingAppAdapter adapter;

	public WizardAction(CySwingAppAdapter adapter, SimulationsContainer simulationsContainer) {
		super("GESTODifferent");
		this.adapter = adapter;
		this.appManager = this.adapter.getCyApplicationManager();
		this.simulationsContainer = simulationsContainer;
		setPreferredMenu("Apps");
	}


	/**
	 * GESTODifferent entry point.
	 * This method opens the wizard view.
	 */
	public void actionPerformed(ActionEvent e) {
		// Get a Cytoscape service 'DialogTaskManager' in CyActivator class
		DialogTaskManager dialogTaskManager = adapter.getCyServiceRegistrar().getService(DialogTaskManager.class);

		Wizard wizard = new Wizard();
		Properties simulationFeatures;
		Properties tasks;
		Properties outputs;
		boolean atm_computation, tree_matching;
		String matching_type;
		int threshold;

		int response = wizard.showWizard();
		if(response == 1){
			simulationFeatures = wizard.getSimulationFeatures();
			tasks = wizard.getTaskToDo();
			outputs = wizard.getOutputs();
			atm_computation = tasks.getProperty(GESTODifferentConstants.ATM_COMPUTATION).equals(GESTODifferentConstants.YES);
			tree_matching = tasks.getProperty(GESTODifferentConstants.TREE_MATCHING).equals(GESTODifferentConstants.YES);
			//Network Creation from features
			try{
				if(tasks.getProperty(GESTODifferentConstants.NETWORK_CREATION).equals(GESTODifferentConstants.NEW)){
					if(!tree_matching){
						dialogTaskManager.execute(new TaskIterator(new NetworkCreation(simulationFeatures, outputs, this.adapter, 
								this.appManager, this.simulationsContainer, atm_computation)));
					}else{
						matching_type = tasks.getProperty(GESTODifferentConstants.MATCHING_TYPE);
						if(matching_type.equals(GESTODifferentConstants.PERFECT_MATCH)){
							dialogTaskManager.execute(new TaskIterator(new NetworkCreation(simulationFeatures, outputs, this.adapter, 
									this.simulationsContainer, atm_computation, tree_matching, wizard.getDifferentiationTree())));
						}else{
							threshold = Integer.parseInt(tasks.getProperty(GESTODifferentConstants.MATCHING_THRESHOLD));
							dialogTaskManager.execute(new TaskIterator(new NetworkCreation(simulationFeatures, outputs, this.adapter, 
									this.simulationsContainer, atm_computation, tree_matching, wizard.getDifferentiationTree(),
									matching_type, threshold)));
							
						}
					}
				}
			}catch(Exception ex){

			}



		}
	}
}
