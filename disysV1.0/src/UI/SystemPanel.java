package UI;

import javax.swing.JButton;
import javax.swing.JLabel;

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
public class SystemPanel extends javax.swing.JPanel {
	private JLabel Label1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JButton stopButton;
	private JButton startButton;
	private JLabel jLabel8;
	private JLabel jLabel7;
	private JLabel jLabel6;
	private JLabel jLabel1;
	private JLabel Label2;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new SystemPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	*/
	
	public SystemPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(413, 297));
			this.setLayout(null);
			this.setBackground(new java.awt.Color(255,255,255));
			{
				Label1 = new JLabel();
				this.add(Label1);
				Label1.setText("SystemManager address :");
				Label1.setBounds(20, 35, 127, 14);
			}
			{
				Label2 = new JLabel();
				this.add(getLabel2());
				Label2.setText("status :");
				Label2.setBounds(20, 57, 114, 14);
			}
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("Runnig Executers :");
				jLabel1.setBounds(20, 80, 114, 14);
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setText("Suspended Executers :");
				jLabel2.setBounds(20, 102, 114, 14);
			}
			{
				jLabel3 = new JLabel();
				this.add(jLabel3);
				jLabel3.setText("Clients :");
				jLabel3.setBounds(20, 126, 105, 14);
			}
			{
				jLabel4 = new JLabel();
				this.add(jLabel4);
				jLabel4.setText("132.68.224.21");
				jLabel4.setBounds(153, 35, 72, 14);
			}
			{
				jLabel5 = new JLabel();
				this.add(jLabel5);
				jLabel5.setText("Running");
				jLabel5.setBounds(153, 57, 39, 14);
			}
			{
				jLabel6 = new JLabel();
				this.add(jLabel6);
				jLabel6.setText("10");
				jLabel6.setBounds(153, 80, 12, 14);
			}
			{
				jLabel7 = new JLabel();
				this.add(jLabel7);
				jLabel7.setText("3");
				jLabel7.setBounds(153, 102, 10, 14);
			}
			{
				jLabel8 = new JLabel();
				this.add(jLabel8);
				jLabel8.setText("6");
				jLabel8.setBounds(153, 126, 10, 14);
			}
			{
				startButton = new JButton();
				this.add(startButton);
				startButton.setText("start");
				startButton.setBounds(20, 266, 72, 20);
			}
			{
				stopButton = new JButton();
				this.add(stopButton);
				stopButton.setText("Stop");
				stopButton.setBounds(103, 266, 65, 20);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JLabel getLabel2() {
		return Label2;
	}

}
