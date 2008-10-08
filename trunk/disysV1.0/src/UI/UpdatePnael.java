package UI;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
public class UpdatePnael extends javax.swing.JPanel {
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JButton UpdateButton;
	private JButton BrowseButton;
	private JTextField jTextField1;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new UpdatePnael());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	*/
	public UpdatePnael() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 300));
			this.setLayout(null);
			this.setBackground(new java.awt.Color(255,255,255));
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("Current Version :");
				jLabel1.setBounds(12, 12, 89, 14);
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setText("V2.0");
				jLabel2.setBounds(101, 12, 26, 14);
			}
			{
				jLabel3 = new JLabel();
				this.add(jLabel3);
				jLabel3.setText("Updates File:");
				jLabel3.setBounds(12, 47, 67, 14);
			}
			{
				jTextField1 = new JTextField();
				this.add(jTextField1);
				jTextField1.setText("c:\\java\\updates.jar");
				jTextField1.setBounds(12, 67, 243, 20);
			}
			{
				BrowseButton = new JButton();
				this.add(BrowseButton);
				BrowseButton.setText("Browse");
				BrowseButton.setBounds(302, 68, 83, 20);
			}
			{
				UpdateButton = new JButton();
				this.add(UpdateButton);
				UpdateButton.setText("Update");
				UpdateButton.setBounds(12, 269, 77, 20);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
