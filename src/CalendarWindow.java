import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/**
 * 日历管理系统 程序入口
 * 
 */
@SuppressWarnings("serial")
public class CalendarWindow extends JFrame implements ActionListener, FocusListener {
	
	private static final String APPLICATION_TITLE = "日志管理" ;
	
	int mYear;
	int mMonth; 
	int mDay;

	/*为了确保一个月的每一天都显示在“周几”中，用了42个格子*/
	public static final int CELL_LENGTH = 6 *7;
	
	private static final String SAVE_DAILYRECORD = "保存日志" ;
	private static final String DELETE_DAILYRECORD = "删除日志" ;
	private static final String READ_DAILYRECORD = "读取日志" ;
	private static final String NEXT_YEAR = "下年";
	private static final String PREVIOUS_YEAR = "上年";
	private static final String NEXT_MONTH = "下月";
	private static final String PREVIOUS_MONTH = "上月";
	
	private static final String DAILYRECORD_DIR = "./dailyRecord" ;
	private static final String PIC_FLOWER_DIR = "./flower.jpg" ;
	
	/*装着“哪年哪月哪天”这种信息*/
	CalendarMessage calendarMessage = new CalendarMessage(); 
	/*装着那些*/
	CalendarPad calendarPad = new CalendarPad();
	NotePad notePad= new NotePad() ;
	
	CalendarImage calendarImage = new CalendarImage() ;
	/*那个钟*/
	Clock clock= new Clock() ;
	/*显示“年”和“月”的编辑文本框(不过这里给他们设置成了不可编辑)*/
	private JTextField jTF_showYear, jTF_showMonth;
	/*日历中每个月的具体的日子*/
	private JTextField[] jTF_showDay = new JTextField[CELL_LENGTH] ;
	
	private JTextField cacheFocusJTextField = null ;
	
	/*顶上的*/
	private JPanel jP_north = new JPanel() ;
	/*底部的*/
	private JPanel jP_south = new JPanel() ;
	
	private JButton nextYear = new JButton(NEXT_YEAR);
	private JButton previousYear = new JButton(PREVIOUS_YEAR);
	private JButton nextMonth = new JButton(NEXT_MONTH);
	private JButton previousMonth = new JButton(PREVIOUS_MONTH);
	
	private JButton saveDailyRecord = new JButton(SAVE_DAILYRECORD);
	private JButton deleteDailyRecord = new JButton(DELETE_DAILYRECORD);
	private JButton readDailyRecord = new JButton(READ_DAILYRECORD);
	
	private File file_directory = new File( DAILYRECORD_DIR ) ;
	
	private Color backgroundColor = Color.white ;
	private Color foregroundColor = Color.gray ;

	public CalendarWindow() {

		initDirectory();
		
		initShowDay();

		initNotePad();

		// 给有日志的号码做标记,见后面的doMark()方法
		doMark();
		
		setCalendarImage();
		
		addJPane();
		
		initJPanelNorthAndSouth();
		
		addListener();
		
		initWindow();
	}

	private void setCalendarImage() {
		calendarImage.setImageFile(new File(PIC_FLOWER_DIR ));
	}

	private void addJPane() {
		JSplitPane splitV1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, calendarPad, calendarImage);
		JSplitPane splitV2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, notePad, clock);
		JSplitPane splitH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitV1, splitV2);
		add(splitH, BorderLayout.CENTER);
	}

	private void initWindow() {
		//设置该JFrame为可见
		setVisible(true);
		setTitle(APPLICATION_TITLE);
		setBounds(60, 60, 660, 480);
		validate();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * 添加监听器
	 * */
	private void addListener() {
		
		nextYear.addActionListener(this);
		previousYear.addActionListener(this);
		nextMonth.addActionListener(this);
		previousMonth.addActionListener(this);
		if (jTF_showYear != null) {
			jTF_showYear.addActionListener(this);
		}
		
		saveDailyRecord.addActionListener(this);
		deleteDailyRecord.addActionListener(this);
		readDailyRecord.addActionListener(this);
	}

	/**
	 * 初始化顶部和底部的界面
	 */
	private void initJPanelNorthAndSouth() {
		
		jTF_showYear = new JTextField("" + mYear, 6);
		jTF_showYear.setFont(new Font("TimesRoman", Font.BOLD, 12));
		jTF_showYear.setHorizontalAlignment(JTextField.CENTER);
		// 设置文本框不可输入
		jTF_showYear.setEditable(false);

		jTF_showMonth = new JTextField(" " + mMonth, 4);
		jTF_showMonth.setFont(new Font("TimesRoman", Font.BOLD, 12));
		jTF_showMonth.setHorizontalAlignment(JTextField.CENTER);
		// 设置文本框不可输入
		jTF_showMonth.setEditable(false);

		jP_north.add(previousYear);
		jP_north.add(jTF_showYear);
		jP_north.add(nextYear);
		jP_north.add(previousMonth);
		jP_north.add(jTF_showMonth);
		jP_north.add(nextMonth);
		add(jP_north, BorderLayout.NORTH);
		
		//设置底部的框框
		jP_south.add(saveDailyRecord);
		jP_south.add(deleteDailyRecord);
		jP_south.add(readDailyRecord);
		add(jP_south, BorderLayout.SOUTH);
	}

	/**
	 * 初始化右上角的  写日志的框框
	 */
	private void initNotePad() {
		
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		//得到当前的年月日
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH) + 1;
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println( mYear+ "年" +mMonth + "月" + mDay +"日");
		
		calendarMessage.setYear(mYear);
		calendarMessage.setMonth(mMonth);
		calendarMessage.setDay(mDay);
		
		calendarPad.setCalendarMessage(calendarMessage);
		
		notePad.setShowMessage(mYear, mMonth, mDay);
		calendarPad.setShowDayTextField(jTF_showDay);
		calendarPad.showMonthCalendar();
	}

	/**
	 * 创建存日志的目录
	 */
	private void initDirectory() {
		
		//若不存在目录才创建该目录
		if (! file_directory.exists()) {
			file_directory.mkdir();
		}
	}

	/**
	 * 初始化那个显示某个月里面每一天的框框
	 */
	private void initShowDay() {

		for (int i = 0; i < jTF_showDay.length; i++) {
			jTF_showDay[i] = new JTextField();
			jTF_showDay[i].setBackground(backgroundColor);
			jTF_showDay[i].setLayout(new GridLayout(3, 3));
			//可能这句太占CPU了吧，然后创建出来的东西太占内存了
			jTF_showDay[i].addFocusListener(this);
		}
	}

	/*这里不能用switch，因为switch只能是String或者int，所以只能用if-then-else*/
	@Override
	public void actionPerformed(ActionEvent e) {

		 //先是顶部的四个按钮（下年上年，下月上月）
		if (e.getSource() == nextYear) {
			mYear++;
			setYearAndShow(mYear);
			
			calendarPad.setCalendarMessage(calendarMessage);
			calendarPad.showMonthCalendar();
			notePad.setShowMessage(mYear, mMonth, mDay);
			
			doMark();
			
		} else if (e.getSource() == previousYear) {
			mYear--;
			setYearAndShow(mYear);
			
			calendarPad.setCalendarMessage(calendarMessage);
			calendarPad.showMonthCalendar();
			notePad.setShowMessage(mYear, mMonth, mDay);
			
			doMark();
			
		} else if (e.getSource() == nextMonth) {
			mMonth++;
			if (mMonth > 12){
				mMonth = 1;
				mYear++;
			}
			jTF_showYear.setText(" " + mYear);
			jTF_showMonth.setText(" " + mMonth);
			calendarMessage.setMonth(mMonth);
			calendarMessage.setYear(mYear);
			calendarPad.setCalendarMessage(calendarMessage);
			calendarPad.showMonthCalendar();
			notePad.setShowMessage(mYear, mMonth, mDay);
			
			doMark();
			
		} else if (e.getSource() == previousMonth) {
			mMonth--;
			if (mMonth < 1){
				mMonth = 12;
				mYear--;
			}
			jTF_showYear.setText(" " + mYear);
			jTF_showMonth.setText(" " + mMonth);
			calendarMessage.setMonth(mMonth);
			calendarMessage.setYear(mYear);
			calendarPad.setCalendarMessage(calendarMessage);
			calendarPad.showMonthCalendar();
			notePad.setShowMessage(mYear, mMonth, mDay);
			
			doMark();
			
		} 
		//那三个底部的按钮（保存，删除，读取）
		else if (e.getSource() == saveDailyRecord) {
			notePad.save(file_directory, mYear, mMonth, mDay);

			doMark();
		} else if (e.getSource() == deleteDailyRecord) {
			notePad.delete(file_directory, mYear, mMonth, mDay);
			doMark();
		} else if (e.getSource() == readDailyRecord)
			notePad.read(file_directory, mYear, mMonth, mDay);
	}

	/**
	 * 改变year的值，然后显示出来
	 * @param initYear
	 */
	private void setYearAndShow(int initYear) {
		jTF_showYear.setText("" + initYear);
		calendarMessage.setYear(initYear);
	}

	/*获取到焦点之后*/
	@Override
	public void focusGained(FocusEvent e) {
		
		// 先获得当前的 JTextField
		JTextField currentFocusJTextField = (JTextField) e.getSource();
		if (cacheFocusJTextField == null){
			cacheFocusJTextField = (JTextField) e.getSource() ;
		}
		
		if ( cacheFocusJTextField != currentFocusJTextField ){
			//焦点改变时，改变颜色
			cacheFocusJTextField.setBackground(backgroundColor);
			cacheFocusJTextField = currentFocusJTextField ;
		}
		currentFocusJTextField.setBackground(foregroundColor);
		
		String str = currentFocusJTextField.getText();
		//.trim()
		//若日志显示区中的内容不为空
		if ( ! str.equals("")) {
			try {
				mDay = Integer.parseInt(str);
			} catch (NumberFormatException exp) {
				exp.printStackTrace();
			}
		}
		calendarMessage.setDay(mDay);
		notePad.setShowMessage(mYear, mMonth, mDay);
	}


	/*失去焦点之后*/
	@Override
	public void focusLost(FocusEvent e) {
		
		
	}

	/**
	 * 若某一天有日志，则对该天作一个标记
	 */
	public void doMark() {
		for (int i = 0; i < jTF_showDay.length; i++) {
			jTF_showDay[i].removeAll();
			String str = jTF_showDay[i].getText().trim();
			try {
				
				//若str不为 ""
				if ( !str.equals( "")) {
					int intDay = Integer.parseInt(str);
					if (isExistedDailyRecord(intDay) == true) {
						JLabel mess = new JLabel("有");
						mess.setFont(new Font("TimesRoman", Font.PLAIN, 11));
						mess.setForeground(Color.blue);
						jTF_showDay[i].add(mess);
					}
				}
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		calendarPad.repaint();
		calendarPad.validate();
	}

	/**
	 * 判断日志目录是否存在
	 */
	public boolean isExistedDailyRecord(int intDay) {
		
		String key = "" + mYear + "" + mMonth + "" + intDay;
		String[] dayFile = file_directory.list();
		boolean boo = false;
		
		for (int k = 0; k < dayFile.length; k++) {
			//判断是否匹配到以“年月日”命名的txt文件
			if (dayFile[k].equals(key + ".txt")) {
				boo = true;
				break;
			}
		}
		return boo;
	}
	
}
