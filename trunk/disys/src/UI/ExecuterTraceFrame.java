package UI;
import java.awt.BorderLayout;
import java.rmi.RemoteException;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

import sun.net.NetworkServer;

import Common.RMIRemoteInfo;
import Networking.IItemCollector;
import Networking.NetworkCommon;


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
	private IItemCollector ic;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ExecuterTraceFrame inst = new ExecuterTraceFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public ExecuterTraceFrame() {
		super();
		initGUI();
		ic=NetworkCommon.loadRMIRemoteObject(new RMIRemoteInfo("localhost",3001,Networking.RMIItemCollector.GlobalId));

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
					LogTraceArea = new JTextArea();
					MainPane.add(LogTraceArea);
					LogTraceArea.setText("Ececec");
					LogTraceArea.setBounds(10, 33, 364, 258);
				}
			}
			pack();
			this.setSize(400, 338);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void update(){
		while(true){
		try {
			LogTraceArea.append(ic.CollectLog());
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
