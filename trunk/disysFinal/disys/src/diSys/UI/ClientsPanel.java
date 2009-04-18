package diSys.UI;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
@SuppressWarnings("serial")
public class ClientsPanel extends javax.swing.JPanel {
	private JList executersList;
	private JScrollPane jScrollPane1;
	private JLabel jLabel1;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new ClientsPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	*/
	
	public ClientsPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(627, 336));
			this.setLayout(null);
			this.setBackground(new java.awt.Color(255,255,255));
			{
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1);
				jScrollPane1.setBounds(21, 33, 584, 269);
				{
					executersList = new JList();
					jScrollPane1.setViewportView(executersList);
					executersList.setBounds(21, 33, 574, 247);
					executersList.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
				}
			}
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("Connected Clients :");
				jLabel1.setBounds(21, 12, 117, 14);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void UpdateSysUI() {
		Object[] items=SystemManagerUI.sysData.Clients.toArray();
		ListModel executersListModel = 
			new DefaultComboBoxModel(items);
		executersList.setModel(executersListModel);
		
	}

}
