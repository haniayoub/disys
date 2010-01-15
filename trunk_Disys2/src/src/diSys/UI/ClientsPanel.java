package diSys.UI;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;


@SuppressWarnings("serial")
public class ClientsPanel extends javax.swing.JPanel {
	private JList executersList;
	private JScrollPane jScrollPane1;
	private JLabel jLabel1;

	
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
