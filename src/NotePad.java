import java.awt.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*;

/**
 * ��־�ı���
 *
 */
public class NotePad extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1646118489169395817L;
	
	private static final String WARNING = "����" ;
	private static final String REQUEST = "����" ;
	private static final String NOTICE = "��ʾ" ;
	
	/*���Ͻǻ�ɫ���ǲ���*/
	private TextField jTF_showMessage = new TextField() ;
	/*��־����������*/
	private JTextArea jTA_textContent = new JTextArea(10, 10);
	/*����Ҽ�֮����ʾ���ĸ�ѡ����ƣ����У�ճ�������*/
	private JPopupMenu menu = new JPopupMenu();
	/*��Ӧ�����ĸ�ѡ��*/
	private JMenuItem 	
	itemCopy = new JMenuItem("����"),
	itemCut = new JMenuItem("����"),
	itemPaste = new JMenuItem("ճ��"),
	itemClear = new JMenuItem("���");
	

	public NotePad() {
		
		initMenu();
		
//		jTF_showMessage.setHorizontalAlignment(TextField.CENTER);
		jTF_showMessage.setFont(new Font("TimesRoman", Font.BOLD, 16));
		jTF_showMessage.setBackground(Color.gray);
//		jTF_showMessage.setBorder(BorderFactory.createRaisedBevelBorder());
		//����Ϊ���ɱ༭
		jTF_showMessage.setEditable(false);
		jTF_showMessage.addTextListener(new TextListener(){
			@Override
			public void textValueChanged(TextEvent e) {
				//�����е����ڸı�ʱ������־��ʾ�������֣������֮
				if ( ! isContentClear() ){
					clearContent();
				}
			}
			
		}) ;
		
		//������������
		jTA_textContent.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//�����(�Ҽ�)֮��
				if (e.getModifiers() == InputEvent.BUTTON3_MASK)
					menu.show(jTA_textContent, e.getX(), e.getY());
			}
		});
		
		setLayout(new BorderLayout());
		add(jTF_showMessage, BorderLayout.NORTH);
		add(new JScrollPane(jTA_textContent), BorderLayout.CENTER);

	}

	/**
	 * �ж���־��ʾ���Ƿ�Ϊ��
	 * @return
	 */
	public boolean isContentClear(){
		return jTA_textContent.getText().trim().equals("");
	}
	/**
	 * ׼���Ҽ��˵�ѡ��
	 */
	private void initMenu() {
		
		itemCopy.addActionListener(this);
		itemCut.addActionListener(this);
		itemPaste.addActionListener(this);
		itemClear.addActionListener(this);
		menu.add(itemCopy);
		menu.add(itemCut);
		menu.add(itemPaste);
		menu.add(itemClear);
	}

	
	/**
	 * ���Ǹ���ɫ�ı������ϣ���־������ʾ�����棩�����ı�
	 */
	public void setShowMessage(int year, int month, int day) {
		jTF_showMessage.setText("" + year + "��" + month + "��" + day + "��");
	}

	
	public void clearContent(){
		if (jTA_textContent != null) {
			jTA_textContent.setText("");
		}
	}
	/**
	 * ������־
	 * 
	 */
	public void save(File dir, int year, int month, int day) {
		
		//���ﲻ�����dailyContent��Ϊȫ�ֱ����������ڵ���save()������ʱ��Ż�ȡjTA_textContent��ֵ
		String dailyContent = jTA_textContent.getText();
		String fileName = "" + year + "" + month + "" + day + ".txt";
		
		//����������Ϊ���򲻱��棬����
		if (dailyContent.trim().equals("")){
			JOptionPane.showConfirmDialog(this, "�㻹û���������أ��޷����棡" , WARNING,
					JOptionPane.DEFAULT_OPTION);
			return;
		}
		
		
		boolean isDiaryExisted = checkIfDiaryExists(dir, year, month, day);
		
		//����־�ļ�����
		if (isDiaryExisted) {
			String m = "" + year + "��" + month + "��" + day + "������־,���µ�������ӵ���־��?";
			showBeforeWriteToFile(m, dir, dailyContent, fileName, true);
			
		} else {
			String m = "" + year + "��" + month + "��" + day + "��û����־,������־��?";
			showBeforeWriteToFile(m, dir, dailyContent, fileName, false);
		}
	}

	/**
	 * �鿴������־�ļ��Ƿ����
	 */
	private boolean checkIfDiaryExists(File dir, int year, int month, int day) {
		
		String key = "" + year + "" + month + "" + day;
		String[] dayFile = dir.list();
		boolean boo = false;
		//����Ŀ��Ŀ¼�µ��ļ��������Ƿ����ҵ��Ǹ���־�ļ�
		for (int k = 0; k < dayFile.length; k++) {
			if (dayFile[k].startsWith(key)) {
				boo = true;
				break;
			}
		}
		return boo;
	}

	/**
	 * ɾ����־
	 */
	public void delete(File dir, int year, int month, int day) {
		
		boolean isDiaryExisted = checkIfDiaryExists(dir, year, month, day);
		if (isDiaryExisted) {
			String m = "ɾ��" + year + "��" + month + "��" + day + "�յ���־��?";
			int ok = JOptionPane.showConfirmDialog(this, m, REQUEST,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ok == JOptionPane.YES_OPTION) {
				String fileName = "" + year + "" + month + "" + day + ".txt";
				deleteFile(dir, fileName);
			}
			
		} else {
			String m = "" + year + "��" + month + "��" + day + "����־��¼";
			JOptionPane.showMessageDialog(this, m, NOTICE,
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * ɾ���Ǹ��ļ�
	 * @param dir :�ļ�Ŀ¼
	 * @param fileName : �ļ���
	 */
	private void deleteFile(File dir, String fileName) {
		File deleteFile = new File(dir, fileName);
		deleteFile.delete();
	}

	/**
	 * ��ȡ��־
	 */
	public void read(File dir, int year, int month, int day) {
		
		String fileName = "" + year + "" + month + "" + day + ".txt";
		boolean isDiaryExisted = checkIfDiaryExists(dir, year, month, day);
		if (isDiaryExisted) {
			String m = "" + year + "��" + month + "��" + day + "����־,��ʾ��־������?";
			int ok = JOptionPane.showConfirmDialog(this, m, REQUEST,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ok == JOptionPane.YES_OPTION) {
				//���ȷ����ť������ı���
				clearContent();
				
				try {
					
					readFromFile(dir, fileName);
				} catch (IOException exp) {
				}
			}
		} else {
			String m = "" + year + "��" + month + "��" + day + "����־��¼";
			JOptionPane.showMessageDialog(this, m, NOTICE,
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * �ȵ����Ի����Ƿ���־д�뵽�ļ����ж��Ƿ�׷�ӣ�����д�뵽�ļ���Ȼ�������־��ʾ��
	 * @param append 
	 * @param fileName 
	 * @param dailyContent 
	 * @param dir 
	 */
	private void showBeforeWriteToFile(String message, File dir, String dailyContent, String fileName, boolean append){
		
		int ok = JOptionPane.showConfirmDialog(this, message, REQUEST,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (ok == JOptionPane.YES_OPTION) {
			try {
				//���һ������Ϊ true����ʾ׷�ӵ��ļ�ĩβ
				writeToFile(dir, dailyContent, fileName, true);
				
				//���ı������
				clearContent();
			} catch (IOException exp) {
				exp.printStackTrace();
			}
		}
	}
	
	/**
	 * ����ָ�����ݡ��ԡ��ƶ��ļ�����д�뵽��ָ��Ŀ¼��
	 * 
	 * @param append �Ƿ�׷��
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeToFile(File dir, String dailyContent, String fileName, boolean append)
			throws FileNotFoundException, IOException {
		
		File f = new File(dir, fileName);
		RandomAccessFile out = new RandomAccessFile(f, "rw");
		byte[] bb = dailyContent.getBytes();
		//��Ϊtrue����׷�ӵ��ļ�ĩβ
		if (append) {
			long fileEnd = out.length();
			out.seek(fileEnd);
		}
		out.write(bb);
		out.close();
	}
	
	/**
	 * ���ļ��ж���������ʾ����־��ʾ��
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void readFromFile(File dir, String fileName)
			throws FileNotFoundException, IOException {
		
		File f = new File(dir, fileName);
		FileReader inOne = new FileReader(f);
		BufferedReader inTwo = new BufferedReader(inOne);
		String s = null;
		while ((s = inTwo.readLine()) != null)
			jTA_textContent.append(s + "\n");
		inOne.close();
		inTwo.close();
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
			jTA_textContent.setText("");
	}
}
