package diSys.UI;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
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
	private JLabel jLabel1;
	private JButton resumeButton;
	private JButton Button;
	private JButton RemoveButton;

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
			setPreferredSize(new Dimension(400, 300));
			this.setLayout(null);
			this.setBackground(new java.awt.Color(255,255,255));
			{
				ListModel executersListModel = 
					new DefaultComboBoxModel(
							new String[] { "ClientA     132.69.211.13",
										    "ClientB     132.68.24.21" ,
											"ClientC     132.69.85.75" });
				executersList = new JList();
				this.add(executersList);
				executersList.setModel(executersListModel);
				executersList.setBounds(21, 33, 155, 247);
				executersList.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				RemoveButton = new JButton();
				this.add(RemoveButton);
				RemoveButton.setText("Remove");
				RemoveButton.setBounds(317, 33, 73, 20);
			}
			{
				Button = new JButton();
				this.add(Button);
				Button.setText("Suspend");
				Button.setBounds(317, 61, 73, 20);
			}
			{
				resumeButton = new JButton();
				this.add(resumeButton);
				resumeButton.setText("Resume");
				resumeButton.setBounds(317, 88, 73, 20);
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

}
