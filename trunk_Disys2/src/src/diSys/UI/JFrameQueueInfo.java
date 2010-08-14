package diSys.UI;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.rmi.RemoteException;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.SwingUtilities;

import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.ItemInfo;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
@SuppressWarnings("serial")
public class JFrameQueueInfo extends javax.swing.JFrame {

	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager
					.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel jPanel1;
	private JScrollPane jScrollPane1;
	private JButton jButtonRemoveTask;
	private JButton jButtonDown;
	private JButton jButtonUp;
	private JButton jButtonRefresh;
	private JList jListQueueInfo;
	private ExecuterRemoteInfo exri;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrameQueueInfo inst = new JFrameQueueInfo();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public JFrameQueueInfo() {
		super();
		initGUI();
	}

	public JFrameQueueInfo(LinkedList<String> queueInfo, ExecuterRemoteInfo exri) {
		super();
		initGUI();
		this.exri = exri;
		ListModel jListQueueInfoModel = new DefaultComboBoxModel(queueInfo
				.toArray());
		jListQueueInfo.setModel(jListQueueInfoModel);
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
			jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(null);
				jPanel1.setPreferredSize(new java.awt.Dimension(747, 296));
				{
					jButtonRefresh = new JButton();
					jPanel1.add(jButtonRefresh);
					jButtonRefresh.setText("Refresh");
					jButtonRefresh.setBounds(21, 155, 90, 23);
					jButtonRefresh.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent evt) {
							jButtonRefreshMouseClicked(evt);
						}
					});
				}
				{
					jButtonUp = new JButton();
					jPanel1.add(jButtonUp);
					jButtonUp.setText("Move Up");
					jButtonUp.setBounds(21, 53, 90, 23);
					jButtonUp.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent evt) {
							jButtonUpMousePressed(evt);
						}
					});
				}
				{
					jButtonDown = new JButton();
					jPanel1.add(jButtonDown);
					jButtonDown.setText("Move Down");
					jButtonDown.setBounds(21, 87, 90, 23);
					jButtonDown.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent evt) {
							jButtonDownMousePressed(evt);
						}
					});
				}
				{
					jButtonRemoveTask = new JButton();
					jPanel1.add(jButtonRemoveTask);
					jButtonRemoveTask.setText("Remove");
					jButtonRemoveTask.setBounds(21, 121, 90, 23);
					jButtonRemoveTask.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent evt) {
							jButtonRemoveTaskMouseClicked(evt);
						}
					});
				}
				{
					jScrollPane1 = new JScrollPane();
					jPanel1.add(jScrollPane1);
					jScrollPane1.setBounds(130, 11, 599, 247);
					{						
						jListQueueInfo = new JList();
						jScrollPane1.setViewportView(jListQueueInfo);
						jListQueueInfo.setBounds(130, 62, 450, 245);
						jListQueueInfo.setToolTipText("Select a Task to view it's info...");
						jListQueueInfo.setPreferredSize(new java.awt.Dimension(2000, 245));
						jListQueueInfo.addListSelectionListener(new ListSelectionListener() {
							public void valueChanged(ListSelectionEvent evt) {
								jListQueueInfoValueChanged(evt);
							}
						});
					}
				}
				jButtonRefreshMouseClicked(null);
			}
			pack();
			setSize(750, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void jButtonRefreshMouseClicked(MouseEvent evt) {
		LinkedList<String> queueInfo;
		this.setSize(750, 300);
		try {
			queueInfo = SystemManagerUI.sysManager.getQueueInfo(exri);
			queueInfo.addFirst("-> " + SystemManagerUI.sysManager.getCurrentTask(exri).toString());
			ListModel jListQueueInfoModel = new DefaultComboBoxModel(queueInfo
					.toArray());

			jListQueueInfo.setModel(jListQueueInfoModel);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void jButtonUpMousePressed(MouseEvent evt) {
		int index = jListQueueInfo.getSelectedIndex();
		if(index == 0)
		{
			JOptionPane.showMessageDialog(this, "Cannot control the running task..."+"\n\r please select another task to manage!", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ItemInfo i = (ItemInfo) jListQueueInfo.getSelectedValue();
		ItemInfo iBefore;
		ItemInfo curr;
		if (index == 1)
			return;
		iBefore = (ItemInfo) jListQueueInfo.getModel().getElementAt(index - 1);
		try {
			int tmpPrio = i.priority;
			SystemManagerUI.sysManager.changePriority(exri, i.itemHashCode,
					iBefore.priority);
			SystemManagerUI.sysManager.changePriority(exri,
					iBefore.itemHashCode, tmpPrio);

			for (int j = 1; j <= index - 2; j++) {
				curr = (ItemInfo) jListQueueInfo.getModel().getElementAt(j);
				SystemManagerUI.sysManager.changePriority(exri,
						curr.itemHashCode, curr.priority - 1);
			}
			for (int j = jListQueueInfo.getModel().getSize() - 1; j >= index + 1; j--) {
				curr = (ItemInfo) jListQueueInfo.getModel().getElementAt(j);
				SystemManagerUI.sysManager.changePriority(exri,
						curr.itemHashCode, curr.priority + 1);
			}

			LinkedList<String> queueInfo = SystemManagerUI.sysManager.getQueueInfo(exri);
			queueInfo.addFirst("-> " + SystemManagerUI.sysManager.getCurrentTask(exri).toString());
			ListModel jListQueueInfoModel = new DefaultComboBoxModel(queueInfo
					.toArray());

			jListQueueInfo.setModel(jListQueueInfoModel);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@SuppressWarnings("unchecked")
	private void jButtonDownMousePressed(MouseEvent evt) {
		int index = jListQueueInfo.getSelectedIndex();
		if(index == 0)
		{
			JOptionPane.showMessageDialog(this, "Cannot control the running task..."+"\n\r please select another task to manage!", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ItemInfo i = (ItemInfo) jListQueueInfo.getSelectedValue();
		ItemInfo iAfter;
		ItemInfo curr;
		if (index == jListQueueInfo.getModel().getSize() - 1)
			return;
		iAfter = (ItemInfo) jListQueueInfo.getModel().getElementAt(index + 1);
		try {
			int tmpPrio = i.priority;
			SystemManagerUI.sysManager.changePriority(exri, i.itemHashCode,
					iAfter.priority);
			SystemManagerUI.sysManager.changePriority(exri,
					iAfter.itemHashCode, tmpPrio);

			for (int j = 1; j <= index - 1; j++) {
				curr = (ItemInfo) jListQueueInfo.getModel().getElementAt(j);
				SystemManagerUI.sysManager.changePriority(exri,
						curr.itemHashCode, curr.priority - 1);
			}
			for (int j = jListQueueInfo.getModel().getSize() - 1; j >= index + 2; j--) {
				curr = (ItemInfo) jListQueueInfo.getModel().getElementAt(j);
				SystemManagerUI.sysManager.changePriority(exri,
						curr.itemHashCode, curr.priority + 1);
			}

			LinkedList<String> queueInfo = SystemManagerUI.sysManager.getQueueInfo(exri);
			queueInfo.addFirst("-> " + SystemManagerUI.sysManager.getCurrentTask(exri).toString());
			ListModel jListQueueInfoModel = new DefaultComboBoxModel(queueInfo
					.toArray());

			jListQueueInfo.setModel(jListQueueInfoModel);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*
	private void jButtonUpMousePressed(MouseEvent evt) {
		int index = jListQueueInfo.getSelectedIndex();
		if(index == 0)
		{
			JOptionPane.showMessageDialog(this, "Cannot control the running task..."+"\n\r please select another task to manage!", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ItemInfo i = (ItemInfo) jListQueueInfo.getSelectedValue();
		if (index == 1)
			return;
		try 
		{
			SystemManagerUI.sysManager.moveUpTask(exri, i.itemHashCode);
			jButtonRefreshMouseClicked(null);

		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void jButtonDownMousePressed(MouseEvent evt) {
		int index = jListQueueInfo.getSelectedIndex();
		if(index == 0)
		{
			JOptionPane.showMessageDialog(this, "Cannot control the running task..."+"\n\r please select another task to manage!", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ItemInfo i = (ItemInfo) jListQueueInfo.getSelectedValue();
		if (index == jListQueueInfo.getModel().getSize() - 1)
			return;
		try 
		{
			SystemManagerUI.sysManager.moveDownTask(exri, i.itemHashCode);			
			jButtonRefreshMouseClicked(null);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/	
	private void jListQueueInfoValueChanged(ListSelectionEvent evt) {
		String taskInfo = ((ItemInfo)jListQueueInfo.getSelectedValue()).getToolTipText();
		jListQueueInfo.setToolTipText(taskInfo);
	}
	
	private void jButtonRemoveTaskMouseClicked(MouseEvent evt) {
		if(jListQueueInfo.getSelectedIndex() == 0)
		{
			JOptionPane.showMessageDialog(this, "Cannot control the running task..."+"\n\r please select another task to manage!", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ItemInfo i = (ItemInfo) jListQueueInfo.getSelectedValue();

		try {
			SystemManagerUI.sysManager.removeTask(exri, i.itemHashCode);
			jButtonRefreshMouseClicked(null);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}