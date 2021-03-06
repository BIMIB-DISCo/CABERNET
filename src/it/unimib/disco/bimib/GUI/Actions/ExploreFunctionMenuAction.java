/**
 * ExploreFunctionMenuAction class.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 */

package it.unimib.disco.bimib.GUI.Actions;

//CABERNET imports
import it.unimib.disco.bimib.CABERNET.SimulationsContainer;
import it.unimib.disco.bimib.GUI.ExploresFunctionView;
//GRNSim imports
import it.unimib.disco.bimib.Exceptions.NotExistingNodeException;
import it.unimib.disco.bimib.Functions.Function;

//System imports
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


//Cytoscape imports
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTableUtil;


public class ExploreFunctionMenuAction extends AbstractCyAction{

	private static final long serialVersionUID = 1L;

	private CyApplicationManager appManager;
	private SimulationsContainer simulationsContainer;


	public ExploreFunctionMenuAction(CySwingAppAdapter adapter, 
			SimulationsContainer simulationsContainer) {

		super("Explore node function", 
				adapter.getCyApplicationManager(),
				"network",
				adapter.getCyNetworkViewManager());

		this.appManager = adapter.getCyApplicationManager();
		this.simulationsContainer = simulationsContainer;
		setPreferredMenu("Apps.CABERNET.Functions");
		
	}

	//Menu action pressed
	public void actionPerformed(ActionEvent e) {
		Function function = null;
		int currentNode;
		String simulationId = "";
		ArrayList<String> genesNames;

		//Gets the simulation id connected with the selected network.
		CyNetwork currentNetwork = appManager.getCurrentNetwork();
		simulationId = currentNetwork.getRow(currentNetwork).get(CyNetwork.NAME, String.class);
		//Gets the selected nodes
		List<CyNode> selectedNodes = CyTableUtil.getNodesInState(currentNetwork,"selected",true);
		try {
			for(CyNode node : selectedNodes){
				//Gets the function of the selected node and show it in the correct view

				currentNode = currentNetwork.getRow(node).get("Gene number", Integer.class);
				function = simulationsContainer.getSimulation(simulationId).getGraphManager().getGraph().getFunction(currentNode);
				genesNames = simulationsContainer.getSimulation(simulationId).getGraphManager().getGraph().getNodesNames();
				ExploresFunctionView exploresFunctionFrame = new ExploresFunctionView(currentNode, genesNames, function);
				exploresFunctionFrame.setVisible(true);
			}
		} catch (NotExistingNodeException ex) {
			//A node must be selected: error message
			JOptionPane.showMessageDialog(null, "A node must be selected before its function showing.", "Error", JOptionPane.ERROR_MESSAGE, null);
		}

	}

}
