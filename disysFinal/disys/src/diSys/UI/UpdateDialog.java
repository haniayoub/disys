package diSys.UI;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import diSys.Common.SystemUpdates;


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
public class UpdateDialog extends javax.swing.JDialog {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel jPanel1;
	private JComboBox jComboBoxClassName;
	private JButton jButtonCommet;
	private JButton jButtonBrowse;
	private JLabel jLabel2;
	private JList jListJars;
	private JLabel jLabel1;
	private JButton jButtonCancel;
	private JCheckBox jCheckForce;
	private JLabel jLabelFile;

	/**
	* Auto-generated main method to display this JDialog
	*/
	/*public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				UpdateDialog inst = new UpdateDialog(frame);
				inst.setVisible(true);
			}
		});
	}*/
	public boolean update=false;
	private JScrollPane jScrollPane1;
	public UpdateDialog(JFrame frame) {
		super(frame);
		initGUI();
	}
	private SystemUpdates sysUp;
	public UpdateDialog(JFrame frame,SystemUpdates sysUp,String FileName) {
		super(frame);
		initGUI();
		ComboBoxModel jComboBoxClassNameModel = 
			new DefaultComboBoxModel(sysUp.getExecuterClassNames());
		jComboBoxClassName.setModel(jComboBoxClassNameModel);
		jLabelFile.setText(FileName);
		this.sysUp=sysUp;
	}
	
	private void initGUI() {
		try {
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setBorder(BorderFactory.createTitledBorder("Update Dialog"));
				jPanel1.setLayout(null);
				{
					jLabelFile = new JLabel();
					jPanel1.add(jLabelFile);
					jLabelFile.setText("jLabel1");
					jLabelFile.setBounds(16, 19, 371, 14);
				}
				{
					jComboBoxClassName = new JComboBox();
					jPanel1.add(jComboBoxClassName);
					jComboBoxClassName.setBounds(136, 39, 251, 20);
				}
				{
					jCheckForce = new JCheckBox();
					jPanel1.add(jCheckForce);
					jCheckForce.setText("Force Update");
					jCheckForce.setBounds(142, 259, 91, 23);
				}
				{
					jButtonCommet = new JButton();
					jPanel1.add(jButtonCommet);
					jButtonCommet.setText("Commet Updates");
					jButtonCommet.setBounds(21, 259, 115, 23);
					jButtonCommet.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButtonCommetMouseClicked(evt);
						}
					});
				}
				{
					jButtonCancel = new JButton();
					jPanel1.add(jButtonCancel);
					jButtonCancel.setText("Cancel");
					jButtonCancel.setBounds(314, 259, 65, 23);
					jButtonCancel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButtonCancelMouseClicked(evt);
						}
					});
				}
				{
					jLabel1 = new JLabel();
					jPanel1.add(jLabel1);
					jLabel1.setText("Executer ClassName :");
					jLabel1.setBounds(16, 42, 105, 14);
				}
				{
					jScrollPane1 = new JScrollPane();
					jPanel1.add(jScrollPane1);
					jScrollPane1.setBounds(16, 92, 371, 149);
					{
						
						jListJars = new JList();
						jScrollPane1.setViewportView(jListJars);
						//jListJars.setModel(jListJarsModel);
						jListJars.setBounds(16, 92, 371, 149);
						jListJars.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
					}
				}
				{
					jLabel2 = new JLabel();
					jPanel1.add(jLabel2);
					jLabel2.setText("Include Jars :");
					jLabel2.setBounds(16, 67, 65, 14);
				}
				{
					jButtonBrowse = new JButton();
					jPanel1.add(jButtonBrowse);
					jButtonBrowse.setText("Browse");
					jButtonBrowse.setBounds(85, 63, 67, 23);
					jButtonBrowse.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent evt) {
							jButtonBrowseMousePressed(evt);
						}
					});
				}
			}
			this.setSize(419, 338);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private File[] files=new File[]{};
	private void jButtonCommetMouseClicked(MouseEvent evt) {
		//System.out.println("jButtonCommet.mouseClicked, event="+evt);
		//TODO add your code for jButtonCommet.mouseClicked
		SystemManagerUI.updateThread.pause();
		this.sysUp.setExecuterClassName(this.jComboBoxClassName.getSelectedItem().toString());
		this.sysUp.setIncludeJars(files);
		boolean force=this.jCheckForce.isSelected();
		String message=null;
		try {
			message=SystemManagerUI.sysManager.Update(this.sysUp, force);
			SystemManagerUI.sysManager=null;
		} catch (Exception e) {
			  JOptionPane.showMessageDialog(this,"Failed to Update \n"+e.getMessage());
			  return;
		}

		new WaitDialog(null,message+"\r\nWait until auto update is stabilized ...",30).setVisible(true);
	    this.dispose();
	}
	
	private void jButtonCancelMouseClicked(MouseEvent evt) {
		this.dispose();
	}
	
	private void jButtonBrowseMousePressed(MouseEvent evt) {
		//System.out.println("jButtonBrowse.mousePressed, event="+evt);
		//TODO add your code for jButtonBrowse.mousePressed
		UpdatePnael.fc.setMultiSelectionEnabled(true);
		int returnVal = UpdatePnael.fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
        	//FileName=fc.getSelectedFile().getAbsolutePath();
        	//this.jTextField1.setText(FileName);
        
        	UpdatePnael.fc.getSelectedFiles();
        	files=UpdatePnael.fc.getSelectedFiles();
        	ListModel jListJarsModel = 
				new DefaultComboBoxModel(files);
			jListJars.setModel(jListJarsModel);
			UpdatePnael.fc.setMultiSelectionEnabled(false);
			
		}else{
			UpdatePnael.fc.setMultiSelectionEnabled(false);
			return;
        }
	
	}

}
