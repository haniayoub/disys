package diSys.UI;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
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
public class WaitDialog extends javax.swing.JDialog {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel jPanel1;
	private JLabel jLabelTime;
	private JLabel jLabel1;
	private JTextPane jTextPaneMessage;

	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				WaitDialog inst = new WaitDialog(frame);
				inst.setVisible(true);
			}
		});
	}
	
	public WaitDialog(JFrame frame) {
		super(frame);
		initGUI();
	}
	static int time=60;
	public WaitDialog(JFrame frame,String message,int Time) {
		super(frame);
		initGUI();
		this.jTextPaneMessage.setText(message);
		this.jLabelTime.setText(""+Time);
		time=Time;
		new Thread(new Runnable() {

			@Override
			public void run() {

				SystemManagerUI.me.setVisible(false);
				for(int i=time;i>0;i--){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					jLabelTime.setText(""+i);
				}
				setVisible(false);
				dispose();
				SystemManagerUI.me.setVisible(true);
				SystemManagerUI.UpdateUI();
				SystemManagerUI.updateThread.resume();
			}}).start();
	}
	
	private void initGUI() {
		try {
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(null);
				{
					jTextPaneMessage = new JTextPane();
					jPanel1.add(jTextPaneMessage);
					jTextPaneMessage.setText("jTextPane1");
					jTextPaneMessage.setBounds(10, 11, 495, 90);
					jTextPaneMessage.setEditable(false);
					jTextPaneMessage.setEnabled(false);
				}
				{
					jLabel1 = new JLabel();
					jPanel1.add(jLabel1);
					jLabel1.setText("Time:");
					jLabel1.setBounds(10, 112, 30, 14);
				}
				{
					jLabelTime = new JLabel();
					jPanel1.add(jLabelTime);
					jLabelTime.setText("60");
					jLabelTime.setBounds(44, 112, 14, 14);
				}
			}
			this.setSize(531, 173);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
