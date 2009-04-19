package diSys.UI;
import java.awt.BorderLayout;
import java.rmi.RemoteException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import diSys.Common.RMIRemoteInfo;
import diSys.Networking.IItemCollector;
import diSys.Networking.NetworkCommon;



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
public class ExecuterTraceFrame extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel MainPane;
	private JTextArea LogTraceArea;
	private Thread updater;
	private JScrollPane jScrollPane1;
	@SuppressWarnings("unchecked")
	private IItemCollector ic;

	/**
	* Auto-generated main method to display this JFrame
	*/
	/*public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ExecuterTraceFrame inst = new ExecuterTraceFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}*/
	
	public ExecuterTraceFrame(RMIRemoteInfo ri) {
		super();
		initGUI();
		ic=NetworkCommon.loadRMIRemoteObject(ri);

		updater=new Thread(new Runnable(){
			@Override
			public void run() {
				update();
			}});
	try {
		System.out.println(ic.CollectLog());
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		updater.start();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				MainPane = new JPanel();
				getContentPane().add(MainPane, BorderLayout.CENTER);
				MainPane.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					MainPane.add(jScrollPane1);
					jScrollPane1.setBounds(11, 17, 578, 304);
					jScrollPane1.setAutoscrolls(true);
					{
						LogTraceArea = new JTextArea();
						jScrollPane1.setViewportView(LogTraceArea);
						LogTraceArea.setText("");
						LogTraceArea.setBounds(11, 17, 578, 304);
						LogTraceArea.setAutoscrolls(true);
						LogTraceArea.setEditable(false);
						//LogTraceArea.setAutoscrolls(true);
						//jScrollPane1.setAutoscrolls(true);
					}
				}
			}
			pack();
			this.setSize(616, 371);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void update(){
		while(true){
		try {
			//LogTraceArea.append(ic.CollectLog());
			LogTraceArea.setText(LogTraceArea.getText()+ic.CollectLog());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		}
	}

}
