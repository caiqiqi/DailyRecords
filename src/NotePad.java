import java.awt.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*;

/**
 * 日志文本框
 *
 */
public class NotePad extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1646118489169395817L;
	
	private static final String WARNING = "警告" ;
	private static final String REQUEST = "请求" ;
	private static final String NOTICE = "提示" ;
	
	/*右上角灰色的那部分*/
	private TextField jTF_showMessage = new TextField() ;
	/*日志内容输入栏*/
	private JTextArea jTA_textContent = new JTextArea(10, 10);
	/*点击右键之后显示的四个选项：复制，剪切，粘贴，清空*/
	private JPopupMenu menu = new JPopupMenu();
	/*对应的那四个选项*/
	private JMenuItem 	
	itemCopy = new JMenuItem("复制"),
	itemCut = new JMenuItem("剪切"),
	itemPaste = new JMenuItem("粘贴"),
	itemClear = new JMenuItem("清空");
	

	public NotePad() {
		
		initMenu();
		
//		jTF_showMessage.setHorizontalAlignment(TextField.CENTER);
		jTF_showMessage.setFont(new Font("TimesRoman", Font.BOLD, 16));
		jTF_showMessage.setBackground(Color.gray);
//		jTF_showMessage.setBorder(BorderFactory.createRaisedBevelBorder());
		//设置为不可编辑
		jTF_showMessage.setEditable(false);
		jTF_showMessage.addTextListener(new TextListener(){
			@Override
			public void textValueChanged(TextEvent e) {
				//当框中的日期改变时，若日志显示区有文字，则清空之
				if ( ! isContentClear() ){
					clearContent();
				}
			}
			
		}) ;
		
		//设置鼠标监听器
		jTA_textContent.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//鼠标点击(右键)之后
				if (e.getModifiers() == InputEvent.BUTTON3_MASK)
					menu.show(jTA_textContent, e.getX(), e.getY());
			}
		});
		
		setLayout(new BorderLayout());
		add(jTF_showMessage, BorderLayout.NORTH);
		add(new JScrollPane(jTA_textContent), BorderLayout.CENTER);

	}

	/**
	 * 判断日志显示区是否为空
	 * @return
	 */
	public boolean isContentClear(){
		return jTA_textContent.getText().trim().equals("");
	}
	/**
	 * 准备右键菜单选项
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
	 * 在那个灰色的标题栏上（日志内容显示区上面）设置文本
	 */
	public void setShowMessage(int year, int month, int day) {
		jTF_showMessage.setText("" + year + "年" + month + "月" + day + "日");
	}

	
	public void clearContent(){
		if (jTA_textContent != null) {
			jTA_textContent.setText("");
		}
	}
	/**
	 * 保存日志
	 * 
	 */
	public void save(File dir, int year, int month, int day) {
		
		//这里不把这个dailyContent作为全局变量，而是在调用save()方法的时候才获取jTA_textContent的值
		String dailyContent = jTA_textContent.getText();
		String fileName = "" + year + "" + month + "" + day + ".txt";
		
		//若输入内容为空则不保存，返回
		if (dailyContent.trim().equals("")){
			JOptionPane.showConfirmDialog(this, "你还没输入内容呢，无法保存！" , WARNING,
					JOptionPane.DEFAULT_OPTION);
			return;
		}
		
		
		boolean isDiaryExisted = checkIfDiaryExists(dir, year, month, day);
		
		//若日志文件存在
		if (isDiaryExisted) {
			String m = "" + year + "年" + month + "月" + day + "已有日志,将新的内容添加到日志吗?";
			showBeforeWriteToFile(m, dir, dailyContent, fileName, true);
			
		} else {
			String m = "" + year + "年" + month + "月" + day + "还没有日志,保存日志吗?";
			showBeforeWriteToFile(m, dir, dailyContent, fileName, false);
		}
	}

	/**
	 * 查看给定日志文件是否存在
	 */
	private boolean checkIfDiaryExists(File dir, int year, int month, int day) {
		
		String key = "" + year + "" + month + "" + day;
		String[] dayFile = dir.list();
		boolean boo = false;
		//遍历目标目录下的文件名，看是否能找到那个日志文件
		for (int k = 0; k < dayFile.length; k++) {
			if (dayFile[k].startsWith(key)) {
				boo = true;
				break;
			}
		}
		return boo;
	}

	/**
	 * 删除日志
	 */
	public void delete(File dir, int year, int month, int day) {
		
		boolean isDiaryExisted = checkIfDiaryExists(dir, year, month, day);
		if (isDiaryExisted) {
			String m = "删除" + year + "年" + month + "月" + day + "日的日志吗?";
			int ok = JOptionPane.showConfirmDialog(this, m, REQUEST,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ok == JOptionPane.YES_OPTION) {
				String fileName = "" + year + "" + month + "" + day + ".txt";
				deleteFile(dir, fileName);
			}
			
		} else {
			String m = "" + year + "年" + month + "月" + day + "无日志记录";
			JOptionPane.showMessageDialog(this, m, NOTICE,
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * 删除那个文件
	 * @param dir :文件目录
	 * @param fileName : 文件名
	 */
	private void deleteFile(File dir, String fileName) {
		File deleteFile = new File(dir, fileName);
		deleteFile.delete();
	}

	/**
	 * 读取日志
	 */
	public void read(File dir, int year, int month, int day) {
		
		String fileName = "" + year + "" + month + "" + day + ".txt";
		boolean isDiaryExisted = checkIfDiaryExists(dir, year, month, day);
		if (isDiaryExisted) {
			String m = "" + year + "年" + month + "月" + day + "有日志,显示日志内容吗?";
			int ok = JOptionPane.showConfirmDialog(this, m, REQUEST,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ok == JOptionPane.YES_OPTION) {
				//点击确定按钮先清空文本区
				clearContent();
				
				try {
					
					readFromFile(dir, fileName);
				} catch (IOException exp) {
				}
			}
		} else {
			String m = "" + year + "年" + month + "月" + day + "无日志记录";
			JOptionPane.showMessageDialog(this, m, NOTICE,
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * 先弹出对话框是否将日志写入到文件（判断是否追加），再写入到文件，然后清空日志显示区
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
				//最后一个参数为 true，表示追加到文件末尾
				writeToFile(dir, dailyContent, fileName, true);
				
				//将文本框清空
				clearContent();
			} catch (IOException exp) {
				exp.printStackTrace();
			}
		}
	}
	
	/**
	 * 将“指定内容”以“制定文件名”写入到“指定目录”
	 * 
	 * @param append 是否追加
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeToFile(File dir, String dailyContent, String fileName, boolean append)
			throws FileNotFoundException, IOException {
		
		File f = new File(dir, fileName);
		RandomAccessFile out = new RandomAccessFile(f, "rw");
		byte[] bb = dailyContent.getBytes();
		//若为true，则追加到文件末尾
		if (append) {
			long fileEnd = out.length();
			out.seek(fileEnd);
		}
		out.write(bb);
		out.close();
	}
	
	/**
	 * 从文件中读到内容显示到日志显示区
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
			jTA_textContent.setText("");
	}
}
