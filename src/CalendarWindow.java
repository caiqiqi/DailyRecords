import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/**
 * ��������ϵͳ �������
 * 
 */
@SuppressWarnings("serial")
public class CalendarWindow extends JFrame implements ActionListener, FocusListener {
	
	private static final String APPLICATION_TITLE = "��־����" ;
	
	int mYear;
	int mMonth; 
	int mDay;

	/*Ϊ��ȷ��һ���µ�ÿһ�춼��ʾ�ڡ��ܼ����У�����42������*/
	public static final int CELL_LENGTH = 6 *7;
	
	private static final String SAVE_DAILYRECORD = "������־" ;
	private static final String DELETE_DAILYRECORD = "ɾ����־" ;
	private static final String READ_DAILYRECORD = "��ȡ��־" ;
	private static final String NEXT_YEAR = "����";
	private static final String PREVIOUS_YEAR = "����";
	private static final String NEXT_MONTH = "����";
	private static final String PREVIOUS_MONTH = "����";
	
	private static final String DAILYRECORD_DIR = "./dailyRecord" ;
	private static final String PIC_FLOWER_DIR = "./flower.jpg" ;
	
	/*װ�š������������족������Ϣ*/
	CalendarMessage calendarMessage = new CalendarMessage(); 
	/*װ����Щ*/
	CalendarPad calendarPad = new CalendarPad();
	NotePad notePad= new NotePad() ;
	
	CalendarImage calendarImage = new CalendarImage() ;
	/*�Ǹ���*/
	Clock clock= new Clock() ;
	/*��ʾ���ꡱ�͡��¡��ı༭�ı���(����������������ó��˲��ɱ༭)*/
	private JTextField jTF_showYear, jTF_showMonth;
	/*������ÿ���µľ��������*/
	private JTextField[] jTF_showDay = new JTextField[CELL_LENGTH] ;
	
	private JTextField cacheFocusJTextField = null ;
	
	/*���ϵ�*/
	private JPanel jP_north = new JPanel() ;
	/*�ײ���*/
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

		// ������־�ĺ��������,�������doMark()����
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
		//���ø�JFrameΪ�ɼ�
		setVisible(true);
		setTitle(APPLICATION_TITLE);
		setBounds(60, 60, 660, 480);
		validate();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * ��Ӽ�����
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
	 * ��ʼ�������͵ײ��Ľ���
	 */
	private void initJPanelNorthAndSouth() {
		
		jTF_showYear = new JTextField("" + mYear, 6);
		jTF_showYear.setFont(new Font("TimesRoman", Font.BOLD, 12));
		jTF_showYear.setHorizontalAlignment(JTextField.CENTER);
		// �����ı��򲻿�����
		jTF_showYear.setEditable(false);

		jTF_showMonth = new JTextField(" " + mMonth, 4);
		jTF_showMonth.setFont(new Font("TimesRoman", Font.BOLD, 12));
		jTF_showMonth.setHorizontalAlignment(JTextField.CENTER);
		// �����ı��򲻿�����
		jTF_showMonth.setEditable(false);

		jP_north.add(previousYear);
		jP_north.add(jTF_showYear);
		jP_north.add(nextYear);
		jP_north.add(previousMonth);
		jP_north.add(jTF_showMonth);
		jP_north.add(nextMonth);
		add(jP_north, BorderLayout.NORTH);
		
		//���õײ��Ŀ��
		jP_south.add(saveDailyRecord);
		jP_south.add(deleteDailyRecord);
		jP_south.add(readDailyRecord);
		add(jP_south, BorderLayout.SOUTH);
	}

	/**
	 * ��ʼ�����Ͻǵ�  д��־�Ŀ��
	 */
	private void initNotePad() {
		
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		//�õ���ǰ��������
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH) + 1;
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println( mYear+ "��" +mMonth + "��" + mDay +"��");
		
		calendarMessage.setYear(mYear);
		calendarMessage.setMonth(mMonth);
		calendarMessage.setDay(mDay);
		
		calendarPad.setCalendarMessage(calendarMessage);
		
		notePad.setShowMessage(mYear, mMonth, mDay);
		calendarPad.setShowDayTextField(jTF_showDay);
		calendarPad.showMonthCalendar();
	}

	/**
	 * ��������־��Ŀ¼
	 */
	private void initDirectory() {
		
		//��������Ŀ¼�Ŵ�����Ŀ¼
		if (! file_directory.exists()) {
			file_directory.mkdir();
		}
	}

	/**
	 * ��ʼ���Ǹ���ʾĳ��������ÿһ��Ŀ��
	 */
	private void initShowDay() {

		for (int i = 0; i < jTF_showDay.length; i++) {
			jTF_showDay[i] = new JTextField();
			jTF_showDay[i].setBackground(backgroundColor);
			jTF_showDay[i].setLayout(new GridLayout(3, 3));
			//�������̫ռCPU�˰ɣ�Ȼ�󴴽������Ķ���̫ռ�ڴ���
			jTF_showDay[i].addFocusListener(this);
		}
	}

	/*���ﲻ����switch����Ϊswitchֻ����String����int������ֻ����if-then-else*/
	@Override
	public void actionPerformed(ActionEvent e) {

		 //���Ƕ������ĸ���ť���������꣬�������£�
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
		//�������ײ��İ�ť�����棬ɾ������ȡ��
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
	 * �ı�year��ֵ��Ȼ����ʾ����
	 * @param initYear
	 */
	private void setYearAndShow(int initYear) {
		jTF_showYear.setText("" + initYear);
		calendarMessage.setYear(initYear);
	}

	/*��ȡ������֮��*/
	@Override
	public void focusGained(FocusEvent e) {
		
		// �Ȼ�õ�ǰ�� JTextField
		JTextField currentFocusJTextField = (JTextField) e.getSource();
		if (cacheFocusJTextField == null){
			cacheFocusJTextField = (JTextField) e.getSource() ;
		}
		
		if ( cacheFocusJTextField != currentFocusJTextField ){
			//����ı�ʱ���ı���ɫ
			cacheFocusJTextField.setBackground(backgroundColor);
			cacheFocusJTextField = currentFocusJTextField ;
		}
		currentFocusJTextField.setBackground(foregroundColor);
		
		String str = currentFocusJTextField.getText();
		//.trim()
		//����־��ʾ���е����ݲ�Ϊ��
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


	/*ʧȥ����֮��*/
	@Override
	public void focusLost(FocusEvent e) {
		
		
	}

	/**
	 * ��ĳһ������־����Ը�����һ�����
	 */
	public void doMark() {
		for (int i = 0; i < jTF_showDay.length; i++) {
			jTF_showDay[i].removeAll();
			String str = jTF_showDay[i].getText().trim();
			try {
				
				//��str��Ϊ ""
				if ( !str.equals( "")) {
					int intDay = Integer.parseInt(str);
					if (isExistedDailyRecord(intDay) == true) {
						JLabel mess = new JLabel("��");
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
	 * �ж���־Ŀ¼�Ƿ����
	 */
	public boolean isExistedDailyRecord(int intDay) {
		
		String key = "" + mYear + "" + mMonth + "" + intDay;
		String[] dayFile = file_directory.list();
		boolean boo = false;
		
		for (int k = 0; k < dayFile.length; k++) {
			//�ж��Ƿ�ƥ�䵽�ԡ������ա�������txt�ļ�
			if (dayFile[k].equals(key + ".txt")) {
				boo = true;
				break;
			}
		}
		return boo;
	}
	
}
