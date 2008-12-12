package diSys.UI;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
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
@SuppressWarnings("serial")
public class SystemManagerUI extends javax.swing.JDialog {
	private JTabbedPane TabbedPanel;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				SystemManagerUI inst = new SystemManagerUI(frame);
				inst.setVisible(true);
			}
		});
	}
	
	public SystemManagerUI(JFrame frame) {
		super(frame);
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				this.setName("System manager");
				this.setResizable(false);
				this.setTitle("System Manager");
			}
			{
				TabbedPanel = new JTabbedPane();
				getContentPane().add(TabbedPanel, BorderLayout.CENTER);
				TabbedPanel.setPreferredSize(new java.awt.Dimension(520, 302));
				TabbedPanel.addTab("System",new SystemPanel());
				TabbedPanel.addTab("Executers",new ExecutersPanel());
				TabbedPanel.addTab("Clients",new ClientsPanel());
				TabbedPanel.addTab("Update",new UpdatePnael());
			}
			this.setSize(431, 360);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
