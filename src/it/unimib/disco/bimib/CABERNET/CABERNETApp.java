/**
 * The app entry point.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 */

package it.unimib.disco.bimib.CABERNET;

//CABERNET imports
import it.unimib.disco.bimib.GUI.Actions.ATMExplorationMenuAction;
import it.unimib.disco.bimib.GUI.Actions.ComputeDynamicPerturbationsAction;
import it.unimib.disco.bimib.GUI.Actions.ComputeDynamicPerturbationsMenuAction;
import it.unimib.disco.bimib.GUI.Actions.DynamicalStatsAction;
import it.unimib.disco.bimib.GUI.Actions.DynamicalStatsMenuAction;
import it.unimib.disco.bimib.GUI.Actions.ExploreFunctionMenuAction;
import it.unimib.disco.bimib.GUI.Actions.ExportAction;
import it.unimib.disco.bimib.GUI.Actions.ExportMenuAction;
import it.unimib.disco.bimib.GUI.Actions.ATMExplorationAction;
import it.unimib.disco.bimib.GUI.Actions.ExploreFunctionAction;
import it.unimib.disco.bimib.GUI.Actions.MostFrequentlyTreeViewAction;
import it.unimib.disco.bimib.GUI.Actions.MostFrequentlyTreeViewMenuAction;
import it.unimib.disco.bimib.GUI.Actions.TesNumberComputingAction;
import it.unimib.disco.bimib.GUI.Actions.TesNumberComputingMenuAction;
import it.unimib.disco.bimib.GUI.Actions.WizardAction;
import it.unimib.disco.bimib.Middleware.VizMapperManager;



//System imports
import java.util.Properties;



//Cytoscape imports
import org.cytoscape.app.swing.AbstractCySwingApp;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.CyNetworkViewContextMenuFactory;
import org.cytoscape.application.swing.CyNodeViewContextMenuFactory;
import org.cytoscape.service.util.CyServiceRegistrar;

public class CABERNETApp extends AbstractCySwingApp{

	private SimulationsContainer simulationsContainer;

	public CABERNETApp(CySwingAppAdapter swingAdapter) {
		super(swingAdapter);

		//Creates a new simulations container
		simulationsContainer = new SimulationsContainer();

		//Defines the wizard
		swingAdapter.getCySwingApplication().addAction(new WizardAction(swingAdapter, this.simulationsContainer));
		VizMapperManager viz = new VizMapperManager(this.swingAdapter);

		//Registers the function exploration service
		final CyServiceRegistrar registrar = adapter.getCyServiceRegistrar();
		registrar.registerService(new ExploreFunctionAction(swingAdapter.getCyApplicationManager(), this.simulationsContainer),
				CyNodeViewContextMenuFactory.class,
				new Properties());
		swingAdapter.getCySwingApplication().addAction(new ExploreFunctionMenuAction(swingAdapter, this.simulationsContainer));


		//Registers the ATM exploration service
		registrar.registerService(new ATMExplorationAction(swingAdapter.getCyApplicationManager(), swingAdapter, this.simulationsContainer, viz),
				CyNetworkViewContextMenuFactory.class,
				new Properties());
		swingAdapter.getCySwingApplication().addAction(new ATMExplorationMenuAction(swingAdapter.getCyApplicationManager(), swingAdapter, this.simulationsContainer, viz));

		//Registers the dynamic perturbations statistics computation service
		registrar.registerService(new ComputeDynamicPerturbationsAction(swingAdapter, this.simulationsContainer, viz), CyNetworkViewContextMenuFactory.class,
				new Properties());
		swingAdapter.getCySwingApplication().addAction(new ComputeDynamicPerturbationsMenuAction(swingAdapter, this.simulationsContainer, viz));

		//Registers the dynamical statistics view service
		registrar.registerService(new DynamicalStatsAction(swingAdapter, this.simulationsContainer), CyNetworkViewContextMenuFactory.class,
				new Properties());
		swingAdapter.getCySwingApplication().addAction(new DynamicalStatsMenuAction(swingAdapter, this.simulationsContainer));

		//Registers the export service
		registrar.registerService(new ExportAction(swingAdapter, this.simulationsContainer), CyNetworkViewContextMenuFactory.class,
				new Properties());
		swingAdapter.getCySwingApplication().addAction(new ExportMenuAction(swingAdapter, this.simulationsContainer));

		//Registers the Tes view service
		registrar.registerService(new TesNumberComputingAction(swingAdapter, this.simulationsContainer), CyNetworkViewContextMenuFactory.class,
				new Properties());
		swingAdapter.getCySwingApplication().addAction(new TesNumberComputingMenuAction(swingAdapter, this.simulationsContainer));

		//Registers the Frequently tree discovering service
		registrar.registerService(new MostFrequentlyTreeViewAction(swingAdapter, 
				this.simulationsContainer), CyNetworkViewContextMenuFactory.class, new Properties());
		swingAdapter.getCySwingApplication().addAction(new MostFrequentlyTreeViewMenuAction(swingAdapter, this.simulationsContainer));
	}


}
