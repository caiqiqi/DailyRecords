import java.awt.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*;

/**
 * 日志文本框
 * @author caiqiqi
 *
 */
public class NotePad extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1646118489169395817L;
	
	/*右上角灰色的那部分*/
	JTextArea jTA_textContent;
	/*日志内容输入栏*/
	JTextField jTF_showMessage;
	/*点击右键之后显示的四个选项：复制，剪切，粘贴，清空*/
	JPopupMenu menu;
	/*对应的那四个选项*/
	JMenuItem itemCopy, itemCut, itemPaste, itemClear;

	public NotePad() {
		jTF_showMessage = new JTextField();
		jTF_showMessage.setHorizontalAlignment(JTextField.CENTER);
		jTF_showMessage.setFont(new Font("TimesRoman", Font.BOLD, 16));
		//这句注释掉之后就可以看到这个标题栏上的文字了（显示当前日期）
//		jTF_showMessage.setForeground(Color.gray);
		jTF_showMessage.setBackground(Color.gray);
		jTF_showMessage.setBorder(BorderFactory.createRaisedBevelBorder());
		//设置为不可编辑
//		jTF_showMessage.setEditable(false);
		
		initMenu();
		jTA_textContent = new JTextArea(10, 10);
		//设置鼠标监听器
		jTA_textContent.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//鼠标点击之后
				if (e.getModifiers() == InputEvent.BUTTON3_MASK)
					menu.show(jTA_textContent, e.getX(), e.getY());
			}
		});

		setLayout(new BorderLayout());
		add(jTF_showMessage, BorderLayout.NORTH);
		add(new JScrollPane(jTA_textContent), BorderLayout.CENTER);

	}

	/**
	 * 准备右键菜单选项
	 */
	private void initMenu() {
		menu = new JPopupMenu();
		itemCopy = new JMenuItem("复制");
		itemCut = new JMenuItem("剪切");
		itemPaste = new JMenuItem("粘贴");
		itemClear = new JMenuItem("清空");
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
		jTF_showMessage.setText("" + year + "年" + month + "月" + day + "日");
	}

	/**
	 * 保存日志
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
			String m = "" + year + "年" + month + "月" + day
					+ "已有日志,将新的内容添加到日志吗?";
			int ok = JOptionPane.showConfirmDialog(this, m, "询问",
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
					//将文本框清空
					jTA_textContent.setText("");
				} catch (IOException exp) {
				}
			}
		} else {
			String m = "" + year + "年" + month + "月" + day + "还没有日志,保存日志吗?";
			int ok = JOptionPane.showConfirmDialog(this, m, "询问",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ok == JOptionPane.YES_OPTION) {
				try {
					File f = new File(dir, fileName);
					RandomAccessFile out = new RandomAccessFile(f, "rw");
//					long fileEnd = out.length();
					byte[] bb = dailyContent.getBytes();
					out.write(bb);
					out.close();
					//将文本框清空
					jTA_textContent.setText("");
				} catch (IOException exp) {
				}
			}
		}
	}

	/**
	 * 删除日志
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
			String m = "删除" + year + "年" + month + "月" + day + "日的日志吗?";
			int ok = JOptionPane.showConfirmDialog(this, m, "询问",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ok == JOptionPane.YES_OPTION) {
				String fileName = "" + year + "" + month + "" + day + ".txt";
				File deleteFile = new File(dir, fileName);
				deleteFile.delete();
			}
		} else {
			String m = "" + year + "年" + month + "月" + day + "无日志记录";
			JOptionPane.showMessageDialog(this, m, "提示",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * 读取日志
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
			String m = "" + year + "年" + month + "月" + day + "有日志,显示日志内容吗?";
			int ok = JOptionPane.showConfirmDialog(this, m, "询问",
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
			String m = "" + year + "年" + month + "月" + day + "无日志记录";
			JOptionPane.showMessageDialog(this, m, "提示",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * 处理点击右键之后
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
