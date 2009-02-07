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
public class ExecutersPanel extends javax.swing.JPanel {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JList executersList;
	private JLabel jLabel1;
	private JButton resumeButton;
	private JButton Button;
	private JButton RemoveButton;
	private JButton AddButton;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new ExecutersPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	*/
	
	public ExecutersPanel() {
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
							new String[] { "ExecuterA     144.241.23.21    running",
										    "ExecuterB     132.68.23.211    suspended" ,
											"ExecuterC     125.53.24.52    running" });
				executersList = new JList();
				this.add(executersList);
				executersList.setModel(executersListModel);
				executersList.setBounds(21, 33, 214, 247);
				executersList.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				AddButton = new JButton();
				this.add(AddButton);
				AddButton.setText("Add");
				AddButton.setBounds(317, 33, 73, 20);
			}
			{
				RemoveButton = new JButton();
				this.add(RemoveButton);
				RemoveButton.setText("Remove");
				RemoveButton.setBounds(317, 64, 73, 20);
			}
			{
				Button = new JButton();
				this.add(Button);
				Button.setText("Suspend");
				Button.setBounds(317, 95, 73, 20);
			}
			{
				resumeButton = new JButton();
				this.add(resumeButton);
				resumeButton.setText("Resume");
				resumeButton.setBounds(317, 126, 73, 20);
			}
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("Connected Executers :");
				jLabel1.setBounds(21, 11, 110, 14);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
