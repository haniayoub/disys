package diSys.UI;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import diSys.Common.ExecuterRemoteInfo;

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
public class ExecutersPanel extends javax.swing.JPanel {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JList executersList;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane1;
	private JLabel jLabel2;
	private JList jList1;
	private JLabel jLabel1;
	private JButton RemoveButton;
	private JButton AddButton;
	final static public AddExecuterDlg addExecuterDlg=new AddExecuterDlg(null);

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new ExecutersPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	*/
	
	public ExecutersPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(658, 355));
			this.setLayout(null);
			this.setBackground(new java.awt.Color(255,255,255));
			{
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1);
				jScrollPane1.setBounds(10, 31, 316, 287);
				{
					executersList = new JList();
					jScrollPane1.setViewportView(executersList);
					executersList.setBounds(10, 31, 316, 250);
					executersList.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					executersList.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent evt) {
							executersListValueChanged(evt);
						}
					});
				}
			}
			{
				AddButton = new JButton();
				this.add(AddButton);
				AddButton.setText("Add");
				AddButton.setBounds(10, 329, 73, 20);
				AddButton.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent evt) {
						AddButtonMousePressed(evt);
					}
				});
			}
			{
				RemoveButton = new JButton();
				this.add(RemoveButton);
				RemoveButton.setText("Remove");
				RemoveButton.setBounds(89, 329, 73, 20);
				RemoveButton.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent evt) {
						RemoveButtonMousePressed(evt);
					}
				});
			}
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("Active Executers :");
				jLabel1.setBounds(21, 11, 110, 14);
			}
			{
				jScrollPane2 = new JScrollPane();
				this.add(jScrollPane2);
				jScrollPane2.setBounds(332, 31, 316, 287);
				{
					jList1 = new JList();
					jScrollPane2.setViewportView(jList1);
					jList1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					jList1.setBounds(332, 31, 316, 250);
					jList1.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent evt) {
							jList1ValueChanged(evt);
						}
					});
				}
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setText("Enactive Executers :");
				jLabel2.setBounds(347, 11, 110, 14);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void UpdateSysUI() {
		ExecuterRemoteInfo ri1=!this.jList1.isSelectionEmpty()?(ExecuterRemoteInfo)this.jList1.getSelectedValue():null;
		ExecuterRemoteInfo ri2=!this.executersList.isSelectionEmpty()?(ExecuterRemoteInfo)this.executersList.getSelectedValue():null;
		
		Object[] items=SystemManagerUI.sysData.activeExecuters.toArray();
		ListModel executersListModel = 
			new DefaultComboBoxModel(items);
		executersList.setModel(executersListModel);
		Object[] items2=SystemManagerUI.sysData.enActiveExecuters.toArray();
		ListModel jList1Model = 
			new DefaultComboBoxModel(items2);
		jList1.setModel(jList1Model);
		if(ri1!=null)this.jList1.setSelectedValue(ri1, true);
		if(ri2!=null)this.executersList.setSelectedValue(ri2, true);
	}
	
	private void AddButtonMousePressed(MouseEvent evt) {
		addExecuterDlg.setVisible(true);
	}
	
	private void RemoveButtonMousePressed(MouseEvent evt) {
    ExecuterRemoteInfo ri;
    if(this.jList1.getSelectedValue()==null){
    	if(this.executersList.getSelectedValue()==null)return;
    	ri=(ExecuterRemoteInfo)this.executersList.getSelectedValue();
    }
    else{
    	ri=(ExecuterRemoteInfo)this.jList1.getSelectedValue();
    }
   int Userreply=JOptionPane.showConfirmDialog(this, "Executer :"+ri.toString()+"\n\rWill be deleted permenatly !, are you sure ?","Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	
	if(Userreply==JOptionPane.NO_OPTION) return;
    try {
		SystemManagerUI.sysManager.removeExecuter(ri);
	} catch (Exception e) {
		JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
	}
	}
	
	private void executersListValueChanged(ListSelectionEvent evt) {
		this.jList1.clearSelection();
	}
	
	private void jList1ValueChanged(ListSelectionEvent evt) {
	   this.executersList.clearSelection();
	}

}
