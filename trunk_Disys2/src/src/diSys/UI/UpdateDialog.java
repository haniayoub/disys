package diSys.UI;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import diSys.Common.SystemUpdates;

@SuppressWarnings("serial")
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
			new DefaultComboBoxModel(sysUp.GetTaskTypes());
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
					jLabel1.setText("Task Calss types :");
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
					jButtonBrowse.setBounds(85, 63, 72, 23);
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
		this.sysUp.setIncludeJars(files);
		try {
			this.sysUp.VerfiyUpdates(jLabelFile.getText(),jComboBoxClassName.getSelectedItem().toString());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage()+"\n\r please check if there is missing jars !", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}catch(NoClassDefFoundError e){
			JOptionPane.showMessageDialog(this, e.getMessage()+" Class not found Error\n\r please check if there is missing jars !", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		SystemManagerUI.updateThread.pause();
		
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
		UpdatePnael.fc.setMultiSelectionEnabled(true);
		int returnVal = UpdatePnael.fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
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
