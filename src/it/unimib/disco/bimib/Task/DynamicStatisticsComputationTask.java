package it.unimib.disco.bimib.Task;
//GESTODifferent imports
import it.unimib.disco.bimib.Sampling.SamplingManager;
import it.unimib.disco.bimib.Atms.AtmManager;
import it.unimib.disco.bimib.Exceptions.NotExistingNodeException;
import it.unimib.disco.bimib.Exceptions.ParamDefinitionException;
import it.unimib.disco.bimib.GUI.AtmView;
import it.unimib.disco.bimib.GUI.DynamicPerturbationsStatsView;
import it.unimib.disco.bimib.Middleware.VizMapperManager;
import it.unimib.disco.bimib.Mutations.MutationManager;
import it.unimib.disco.bimib.Networks.GraphManager;


//System imports
import java.util.ArrayList;
import java.util.Properties;

//Cytoscape imports
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class DynamicStatisticsComputationTask extends AbstractTask{

	private Properties perturbationFeatures;
	//private NetworkManagment cytoscapeBridge;
	private CySwingAppAdapter adapter;
	private CyApplicationManager appManager;
	private String networkId;
	private GraphManager mutatedNetworkManager;
	private SamplingManager samplingManager;
	private CyNetwork currentNetwork;
	private VizMapperManager vizMapperManager;

	

	public DynamicStatisticsComputationTask(GraphManager graphManager, SamplingManager samplingManager, 
			ArrayList<String> permanentKnockIn, ArrayList<String> permanentKnockOut,
			Properties perturbationFeatures, String networkId,
			CySwingAppAdapter adapter, VizMapperManager vizMapperManager, 
			CyNetwork currentNetwork) throws ParamDefinitionException, NotExistingNodeException{
		

		this.perturbationFeatures = perturbationFeatures;
		this.adapter = adapter;
		this.appManager = this.adapter.getCyApplicationManager();
		this.networkId = networkId;
		
		this.mutatedNetworkManager = graphManager.copy();
		this.samplingManager = samplingManager;
		
		this.currentNetwork = currentNetwork;
		this.vizMapperManager = vizMapperManager;
		
		//Fixes the knock-in nodes if specified
		if(permanentKnockIn != null){
			for(String geneName : permanentKnockIn)
				this.mutatedNetworkManager.perpetuallyChangeFunctionValue(geneName, true);
		}
		
		//Fixes the knock-out nodes if specified
		if(permanentKnockOut != null){
			for(String geneName : permanentKnockOut)
				this.mutatedNetworkManager.perpetuallyChangeFunctionValue(geneName, false);
		}
		
	}


	public void run(final TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("GESTODifferent - Dynamic perturbations analysis");
		
		//Defines the mutation manager
		MutationManager mutationManager = new MutationManager(this.mutatedNetworkManager, this.samplingManager, this.perturbationFeatures);
		//Defines the mutation manager and computes the dynamic perturbation statistics
		AtmManager atmManager = new AtmManager(this.perturbationFeatures, this.samplingManager, mutationManager, this.mutatedNetworkManager.getNodesNumber());	
		taskMonitor.setProgress(1.0);
		//Shows the results
		AtmView atmView = new AtmView(atmManager, this.samplingManager, this.adapter, 
				this.appManager, this.networkId, vizMapperManager, currentNetwork);
		atmView.setVisible(true);
		
		DynamicPerturbationsStatsView dynPerturbView = new DynamicPerturbationsStatsView(atmManager.getAtm().getDynamicPerturbationsStatistics(), 
				this.mutatedNetworkManager.getGraph().getNodesNames());
		dynPerturbView.setVisible(true);
		
		
	}
}