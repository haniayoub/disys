package diSys.UI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import diSys.Common.ExecuterRemoteInfo;
import diSys.Networking.IRemoteItemReceiver;


@SuppressWarnings("serial")
public class AddExecuterDlg extends javax.swing.JDialog {
	private JLabel jLabel1;
	private JLabel jLabelRCport;
	private JTextField jTextFieldRCPort;
	private JButton jButtonAdd;
	private JButton jButtonCancel;
	private JTextField jTextFieldIRPort;
	private JLabel jLabelIRport;
	private JTextField jTextFieldAddress;

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
				AddExecuterDlg inst = new AddExecuterDlg(frame);
				inst.setVisible(true);
			}
		});
	}
	
	public AddExecuterDlg(JFrame frame) {
		super(frame);
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				getContentPane().setLayout(null);
				{
					jLabel1 = new JLabel();
					getContentPane().add(jLabel1);
					jLabel1.setText("Address :");
					jLabel1.setBounds(21, 23, 46, 14);
				}
				{
					jTextFieldAddress = new JTextField();
					getContentPane().add(jTextFieldAddress);
					jTextFieldAddress.setText("LocalHost");
					jTextFieldAddress.setBounds(71, 20, 303, 20);
				}
				{
					jLabelIRport = new JLabel();
					getContentPane().add(jLabelIRport);
					jLabelIRport.setText("IR Port :");
					jLabelIRport.setBounds(21, 51, 46, 14);
				}
				{
					jLabelRCport = new JLabel();
					getContentPane().add(jLabelRCport);
					jLabelRCport.setText("RC Port :");
					jLabelRCport.setBounds(21, 77, 46, 14);
				}
				{
					jTextFieldIRPort = new JTextField();
					getContentPane().add(jTextFieldIRPort);
					jTextFieldIRPort.setText("3000");
					jTextFieldIRPort.setBounds(71, 48, 40, 20);
				
				}
				{
					jTextFieldRCPort = new JTextField();
					getContentPane().add(jTextFieldRCPort);
					jTextFieldRCPort.setText("3001");
					jTextFieldRCPort.setBounds(71, 74, 40, 20);
				}
				{
					jButtonAdd = new JButton();
					getContentPane().add(jButtonAdd);
					jButtonAdd.setText("Add");
					jButtonAdd.setBounds(10, 158, 60, 23);
					jButtonAdd.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent evt) {
							jButtonAddMousePressed(evt);
						}
					});
				}
				{
					jButtonCancel = new JButton();
					getContentPane().add(jButtonCancel);
					jButtonCancel.setText("Cancel");
					jButtonCancel.setBounds(76, 158, 69, 23);
					jButtonCancel.setPreferredSize(new java.awt.Dimension(69, 23));
					jButtonCancel.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent evt) {
							jButtonCancelMousePressed(evt);
						}
					});
				}
			}
			this.setSize(400, 230);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void jButtonAddMousePressed(MouseEvent evt) {
		try {
			ExecuterRemoteInfo ri=new ExecuterRemoteInfo(this.jTextFieldAddress.getText(),Integer.parseInt(this.jTextFieldIRPort.getText()),Integer.parseInt(this.jTextFieldRCPort.getText()));
			IRemoteItemReceiver rir=diSys.Networking.NetworkCommon.loadRMIRemoteObject(ri.getItemRecieverInfo());
			String message=null;
			if(rir!=null){
				try {	
				rir.Alive();
				}catch(Exception e){
					message="Executer is not alive\r\n"+e.getMessage();
				}
			}else{
				message="Can't Connect to executer ";
			}
			int Userreply=JOptionPane.YES_OPTION;
			if(message!=null){
		     Userreply=JOptionPane.showConfirmDialog(this, message+"\n\radd any way ?", "Add any Way ?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			}
			if(Userreply==JOptionPane.NO_OPTION) return;
		//	JOptionPane.showConfirmDialog(this,"", title, optionType, messageType)
			String result=SystemManagerUI.sysManager.addExecuter(ri);
			JOptionPane.showMessageDialog(this,result, "Done!", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		
		}
	}
	
	private void jButtonCancelMousePressed(MouseEvent evt) {
		this.setVisible(false);
	}
	

}
