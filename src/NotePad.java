import java.awt.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*;

/**
 * ��־�ı���
 * @author caiqiqi
 *
 */
public class NotePad extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1646118489169395817L;
	
	/*���Ͻǻ�ɫ���ǲ���*/
	JTextArea jTA_textContent;
	/*��־����������*/
	JTextField jTF_showMessage;
	/*����Ҽ�֮����ʾ���ĸ�ѡ����ƣ����У�ճ�������*/
	JPopupMenu menu;
	/*��Ӧ�����ĸ�ѡ��*/
	JMenuItem itemCopy, itemCut, itemPaste, itemClear;

	public NotePad() {
		jTF_showMessage = new JTextField();
		jTF_showMessage.setHorizontalAlignment(JTextField.CENTER);
		jTF_showMessage.setFont(new Font("TimesRoman", Font.BOLD, 16));
		//���ע�͵�֮��Ϳ��Կ�������������ϵ������ˣ���ʾ��ǰ���ڣ�
//		jTF_showMessage.setForeground(Color.gray);
		jTF_showMessage.setBackground(Color.gray);
		jTF_showMessage.setBorder(BorderFactory.createRaisedBevelBorder());
		//����Ϊ���ɱ༭
//		jTF_showMessage.setEditable(false);
		
		initMenu();
		jTA_textContent = new JTextArea(10, 10);
		//������������
		jTA_textContent.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//�����֮��
				if (e.getModifiers() == InputEvent.BUTTON3_MASK)
					menu.show(jTA_textContent, e.getX(), e.getY());
			}
		});

		setLayout(new BorderLayout());
		add(jTF_showMessage, BorderLayout.NORTH);
		add(new JScrollPane(jTA_textContent), BorderLayout.CENTER);

	}

	/**
	 * ׼���Ҽ��˵�ѡ��
	 */
	private void initMenu() {
		menu = new JPopupMenu();
		itemCopy = new JMenuItem("����");
		itemCut = new JMenuItem("����");
		itemPaste = new JMenuItem("ճ��");
		itemClear = new JMenuItem("���");
		itemCopy.addActionListener(this);
		itemCut.addActionListener(this);
		itemPaste.addActionListener(this);
		itemClear.addActionListener(this);
		menu.add(itemCopy);
		menu.add(itemCut);
		menu.add(itemPaste);
		menu.add(itemClear);
	}

	public void setShowMessage(int year, int month, int day) {
		jTF_showMessage.setText("" + year + "��" + month + "��" + day + "��");
	}

	/**
	 * ������־
	 * 
	 */
	public void save(File dir, int year, int month, int day) {
		String dailyContent = jTA_textContent.getText();
		String fileName = "" + year + "" + month + "" + day + ".txt";
		String key = "" + year + "" + month + "" + day;
		String[] dayFile = dir.list();
		boolean boo = false;
		for (int k = 0; k < dayFile.length; k++) {
			if (dayFile[k].startsWith(key)) {
				boo = true;
				break;
			}
		}
		if (boo) {
			String m = "" + year + "��" + month + "��" + day
					+ "������־,���µ�������ӵ���־��?";
			int ok = JOptionPane.showConfirmDialog(this, m, "ѯ��",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ok == JOptionPane.YES_OPTION) {
				try {
					File f = new File(dir, fileName);
					RandomAccessFile out = new RandomAccessFile(f, "rw");
					long fileEnd = out.length();
					byte[] bb = dailyContent.getBytes();
					out.seek(fileEnd);
					out.write(bb);
					out.close();
					//���ı������
					jTA_textContent.setText("");
				} catch (IOException exp) {
				}
			}
		} else {
			String m = "" + year + "��" + month + "��" + day + "��û����־,������־��?";
			int ok = JOptionPane.showConfirmDialog(this, m, "ѯ��",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ok == JOptionPane.YES_OPTION) {
				try {
					File f = new File(dir, fileName);
					RandomAccessFile out = new RandomAccessFile(f, "rw");
//					long fileEnd = out.length();
					byte[] bb = dailyContent.getBytes();
					out.write(bb);
					out.close();
					//���ı������
					jTA_textContent.setText("");
				} catch (IOException exp) {
				}
			}
		}
	}

	/**
	 * ɾ����־
	 */
	public void delete(File dir, int year, int month, int day) {
		String key = "" + year + "" + month + "" + day;
		String[] dayFile = dir.list();
		boolean boo = false;
		for (int k = 0; k < dayFile.length; k++) {
			if (dayFile[k].startsWith(key)) {
				boo = true;
				break;
			}
		}
		if (boo) {
			String m = "ɾ��" + year + "��" + month + "��" + day + "�յ���־��?";
			int ok = JOptionPane.showConfirmDialog(this, m, "ѯ��",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ok == JOptionPane.YES_OPTION) {
				String fileName = "" + year + "" + month + "" + day + ".txt";
				File deleteFile = new File(dir, fileName);
				deleteFile.delete();
			}
		} else {
			String m = "" + year + "��" + month + "��" + day + "����־��¼";
			JOptionPane.showMessageDialog(this, m, "��ʾ",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * ��ȡ��־
	 */
	public void read(File dir, int year, int month, int day) {
		
		String fileName = "" + year + "" + month + "" + day + ".txt";
		String key = "" + year + "" + month + "" + day;
		String[] dayFile = dir.list();
		boolean boo = false;
		for (int k = 0; k < dayFile.length; k++) {
			if (dayFile[k].startsWith(key)) {
				boo = true;
				break;
			}
		}
		if (boo) {
			String m = "" + year + "��" + month + "��" + day + "����־,��ʾ��־������?";
			int ok = JOptionPane.showConfirmDialog(this, m, "ѯ��",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ok == JOptionPane.YES_OPTION) {
				jTA_textContent.setText(null);
				try {
					File f = new File(dir, fileName);
					FileReader inOne = new FileReader(f);
					BufferedReader inTwo = new BufferedReader(inOne);
					String s = null;
					while ((s = inTwo.readLine()) != null)
						jTA_textContent.append(s + "\n");
					inOne.close();
					inTwo.close();
				} catch (IOException exp) {
				}
			}
		} else {
			String m = "" + year + "��" + month + "��" + day + "����־��¼";
			JOptionPane.showMessageDialog(this, m, "��ʾ",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * �������Ҽ�֮��
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == itemCopy)
			jTA_textContent.copy();
		else if (e.getSource() == itemCut)
			jTA_textContent.cut();
		else if (e.getSource() == itemPaste)
			jTA_textContent.paste();
		else if (e.getSource() == itemClear)
			jTA_textContent.setText(null);
	}
}
