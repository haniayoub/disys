package diSys.SystemManager;
import java.awt.BorderLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


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
public class NewJDialog extends javax.swing.JDialog {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel jPanel1;
	private JButton jButtonCommet;
	private JCheckBox jCheckBoxForce;
	private JComboBox jComboBoxClassName;
	private JLabel jLabel1;
	private JLabel jLabel1FileName;
	private JButton jButtonCancel;

	
	public NewJDialog(JFrame frame) {
		super(frame);
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				this.setTitle("Update Dialog");
			}
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(null);
				{
					jButtonCommet = new JButton();
					jPanel1.add(jButtonCommet);
					jButtonCommet.setText("Commet Updates");
					jButtonCommet.setBounds(10, 262, 115, 23);
				}
				{
					jButtonCancel = new JButton();
					jPanel1.add(jButtonCancel);
					jButtonCancel.setText("Cancel");
					jButtonCancel.setBounds(135, 262, 65, 23);
				}
				{
					jLabel1FileName = new JLabel();
					jPanel1.add(jLabel1FileName);
					jLabel1FileName.setText("jLabel1");
					jLabel1FileName.setBounds(20, 11, 407, 14);
				}
				{
					jLabel1 = new JLabel();
					jPanel1.add(jLabel1);
					jLabel1.setText("ExecuterClass:");
					jLabel1.setBounds(20, 36, 72, 14);
				}
				{
					ComboBoxModel jComboBoxClassNameModel = 
						new DefaultComboBoxModel(
								new String[] { "Item One", "Item Two" });
					jComboBoxClassName = new JComboBox();
					jPanel1.add(jComboBoxClassName);
					jComboBoxClassName.setModel(jComboBoxClassNameModel);
					jComboBoxClassName.setBounds(20, 56, 137, 20);
				}
				{
					jCheckBoxForce = new JCheckBox();
					jPanel1.add(jCheckBoxForce);
					jCheckBoxForce.setText("Force Update");
					jCheckBoxForce.setBounds(346, 32, 91, 23);
				}
			}
			this.setSize(481, 334);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
