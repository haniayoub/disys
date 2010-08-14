package diSys.UI;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import diSys.Common.RMIRemoteInfo;
import diSys.Common.SystemManagerData;
import diSys.SystemManager.ISystemManager;


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
public class SystemManagerUI extends javax.swing.JFrame{
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
	public static JFrame father;
	public SystemManagerUI(JFrame frame) {
		super();
		father=frame;
		initGUI();
		 addWindowListener(new WindowAdapter() {
		      @Override
			public void windowClosing(WindowEvent e) {
		        System.exit(0);
		      }
		    });
	}
	@SuppressWarnings("unchecked")
	public static ISystemManager sysManager;
	public static RMIRemoteInfo sysmRi;
	public static SystemManagerData sysData;
	
	public static SystemPanel sysPanel=new SystemPanel();
	public static ExecutersPanel executersPanel=new ExecutersPanel();
	public static ClientsPanel clientsPanel=new ClientsPanel();
	public static UpdatePnael updatePanel=new UpdatePnael();
	private AnalysisPanel analysisPanel1;
	public static SystemManagerUI me;
	public static UpdaterThread updateThread=new UpdaterThread(1000);
	public static SystemManegerLogTrace sysmLogDlg=new SystemManegerLogTrace();
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
				TabbedPanel.addTab("System",sysPanel);
				TabbedPanel.addTab("Executers",executersPanel);
				TabbedPanel.addTab("Clients",clientsPanel);
				TabbedPanel.addTab("Update",updatePanel);
				{
					analysisPanel1 = new AnalysisPanel();
					TabbedPanel.addTab("Analysis", null, analysisPanel1, null);
				}
			}
			//sysManager=diSys.Networking.NetworkCommon.loadRMIRemoteObject(ri)
			this.setSize(687, 449);
		} catch (Exception e) {
			e.printStackTrace();
		}
		me=this;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent evt) {
				thisWindowClosed(evt);
			}
		});
	}

	public static void UpdateUI(){
		try {
			if(SystemManagerUI.sysManager==null)SystemManagerUI.sysManager=diSys.Networking.NetworkCommon.loadRMIRemoteObject(SystemManagerUI.sysmRi);
				//java.rmi.server.RemoteStub.toStub(SystemManagerUI.sysManager).
			SystemManagerUI.sysData=SystemManagerUI.sysManager.GetData();
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null,"Failed to Load system manager Data");
			SystemManagerUI.updateThread.pause();
			SystemManagerUI.sysManager=null;
			e.printStackTrace();
		}
		if(SystemManagerUI.sysmLogDlg.isVisible())
			try {
				SystemManagerUI.sysmLogDlg.setText(SystemManagerUI.sysManager.CollectLog());
			} catch (RemoteException e) {
			}
		sysPanel.UpdateSysUI();
		executersPanel.UpdateSysUI();
		clientsPanel.UpdateSysUI();
		updatePanel.UpdateSysUI();
	}
	
	private void thisWindowClosed(WindowEvent evt) {
		System.out.println("this.windowClosed, event="+evt);
		System.exit(0);
	}
}
