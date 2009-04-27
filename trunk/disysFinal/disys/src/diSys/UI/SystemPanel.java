package diSys.UI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import javax.swing.BorderFactory;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import diSys.Common.RMIRemoteInfo;
import diSys.Common.SystemManagerData;
import diSys.SystemManager.SystemManager;

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

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JLabel Label1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel5;
	private JButton jButtonShowTrace;
	private JButton jButtonCleanKill;
	private JLabel jLabel_version_text;
	private JPanel jPanel1;
	private JLabel jLabel_version;
	private JTextField TextBox_port;
	private JTextField TextBox_HostName;
	private JLabel jLabel9;
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
				Label2 = new JLabel();
				this.add(getLabel2());
				Label2.setText("status :");
				Label2.setBounds(20, 140, 114, 14);
			}
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("Active Executers :");
				jLabel1.setBounds(20, 163, 114, 14);
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setText("Inactive Executers :");
				jLabel2.setBounds(20, 185, 114, 14);
			}
			{
				jLabel3 = new JLabel();
				this.add(jLabel3);
				jLabel3.setText("Clients :");
				jLabel3.setBounds(20, 209, 105, 14);
			}
			{
				jLabel5 = new JLabel();
				this.add(jLabel5);
				jLabel5.setText("Disconnected");
				jLabel5.setBounds(173, 140, 81, 14);
			}
			{
				jLabel6 = new JLabel();
				this.add(jLabel6);
				jLabel6.setText("0");
				jLabel6.setBounds(173, 163, 12, 14);
			}
			{
				jLabel7 = new JLabel();
				this.add(jLabel7);
				jLabel7.setText("0");
				jLabel7.setBounds(173, 185, 10, 14);
			}
			{
				jLabel8 = new JLabel();
				this.add(jLabel8);
				jLabel8.setText("0");
				jLabel8.setBounds(173, 209, 10, 14);
			}
			{
				startButton = new JButton();
				this.add(startButton);
				startButton.setText("Connect");
				startButton.setBounds(10, 266, 83, 20);
				startButton.setPreferredSize(new java.awt.Dimension(83, 20));
				startButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						startButtonMouseClicked(evt);
					}
				});
			}
			{
				stopButton = new JButton();
				this.add(stopButton);
				stopButton.setText("Disconnect");
				stopButton.setBounds(103, 266, 102, 20);
				stopButton.setPreferredSize(new java.awt.Dimension(102, 20));
				stopButton.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent evt) {
						stopButtonMousePressed(evt);
					}
				});
			}
			{
				jPanel1 = new JPanel();
				this.add(jPanel1);
				jPanel1.setBounds(12, 12, 389, 101);
				jPanel1.setLayout(null);
				jPanel1.setBorder(BorderFactory.createTitledBorder("Connection Info"));
				jPanel1.setBackground(new java.awt.Color(255,255,255));
				{
					TextBox_port = new JTextField();
					jPanel1.add(TextBox_port);
					TextBox_port.setText("5000");
					TextBox_port.setBounds(178, 59, 58, 23);
				}
				{
					TextBox_HostName = new JTextField();
					jPanel1.add(TextBox_HostName);
					TextBox_HostName.setText("LocalHost");
					TextBox_HostName.setBounds(178, 21, 191, 23);
				}
				{
					Label1 = new JLabel();
					jPanel1.add(Label1);
					Label1.setText("SystemManager address :");
					Label1.setBounds(14, 24, 134, 16);
				}
				{
					jLabel9 = new JLabel();
					jPanel1.add(jLabel9);
					jLabel9.setText("System Manager  Port :");
					jLabel9.setBounds(12, 62, 122, 16);
				}
			}
			{
				jLabel_version = new JLabel();
				this.add(jLabel_version);
				jLabel_version.setText("CurrentVersion :");
				jLabel_version.setBounds(20, 119, 93, 16);
			}
			{
				jLabel_version_text = new JLabel();
				this.add(jLabel_version_text);
				jLabel_version_text.setText(" ");
				jLabel_version_text.setBounds(173, 119, 32, 16);
			}
			{
				jButtonCleanKill = new JButton();
				this.add(jButtonCleanKill);
				jButtonCleanKill.setText("Clean Kill");
				jButtonCleanKill.setBounds(276, 265, 129, 23);
				jButtonCleanKill.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent evt) {
						jButtonCleanKillMousePressed(evt);
					}
				});
			}
			{
				jButtonShowTrace = new JButton();
				this.add(jButtonShowTrace);
				jButtonShowTrace.setText("Show Trace");
				jButtonShowTrace.setBounds(276, 236, 127, 23);
				jButtonShowTrace.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent evt) {
						jButtonShowTraceMousePressed(evt);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JLabel getLabel2() {
		return Label2;
	}
	
	private void startButtonMouseClicked(MouseEvent evt) {
		SystemManagerUI.sysmRi=new RMIRemoteInfo(this.TextBox_HostName.getText(),Integer.parseInt(this.TextBox_port.getText()),SystemManager.GlobalID);
	
		SystemManagerUI.sysManager=diSys.Networking.NetworkCommon.loadRMIRemoteObject(SystemManagerUI.sysmRi);
	
		if(SystemManagerUI.sysManager==null){
			  JOptionPane.showMessageDialog(this,"Failed to connect");
		}else{
			SystemManagerUI.UpdateUI();
			if(SystemManagerUI.updateThread.IsRunning())
				SystemManagerUI.updateThread.resume();
				else SystemManagerUI.updateThread.start();
			
			
			//UpdateUI();
		}
	}

	public void UpdateSysUI() {
		if(SystemManagerUI.sysManager==null){
			this.jLabel5.setText("Disconnected");
			return;
		}
	
		this.jLabel5.setText("Running");
	    jLabel_version_text.setText(""+SystemManagerUI.sysData.Version);
		jLabel6.setText(""+SystemManagerUI.sysData.activeExecuters.size());
		jLabel7.setText(""+SystemManagerUI.sysData.enActiveExecuters.size());
		jLabel8.setText(""+SystemManagerUI.sysData.Clients.size());
	}
	
	private void stopButtonMousePressed(MouseEvent evt) {
		SystemManagerUI.updateThread.pause();
		SystemManagerUI.sysManager=null;
		SystemManagerUI.UpdateUI();
	}
	
	private void jButtonCleanKillMousePressed(MouseEvent evt) {
		int Userreply=JOptionPane.YES_OPTION;
	
	    Userreply=JOptionPane.showConfirmDialog(this,"This Operation will kill all the executers connected to the system !\n are you sure?","are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if(Userreply==JOptionPane.NO_OPTION) return;
	//	JOptionPane.showConfirmDialog(this,"", title, optionType, messageType)
		try{
		String result=SystemManagerUI.sysManager.CleanExit();
		JOptionPane.showMessageDialog(this,result, "Done!", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
	
		}
	
	}
	
	private void jButtonShowTraceMousePressed(MouseEvent evt) {
		SystemManagerUI.sysmLogDlg.setVisible(true);
	}
}
