import javax.swing.*;

import java.awt.*;

/**
 * 左上角显示日期的那个框框
 * 
 */
@SuppressWarnings("serial")
public class CalendarPad extends JPanel {

	int year;
	int month; 
	int day;

	CalendarMessage calendarMessage;
	private JTextField[] jTF_showDay;
	/* 某一月的每一天用JLabel表示 */
	private JLabel jL_title[];
	/*将下面这个字符串数组信息装进jL_title[]里面，然后又装进jP_north里面*/
	private String[] dayOfWeek = { "SUN日", "MON一", "TUE二", "WED三", "THU四", "FRI五",
			"SAT六" };

	private JPanel jP_north = new JPanel();
	private JPanel jP_center = new JPanel();

	public CalendarPad() {

		setLayout(new BorderLayout());
		
		jP_north.setLayout(new GridLayout(1, 7));
		
		jP_center.setLayout(new GridLayout(6, 7));
		add(jP_center, BorderLayout.CENTER);
		add(jP_north, BorderLayout.NORTH);
		jL_title = new JLabel[7];

		for (int j = 0; j < 7; j++) {
			jL_title[j] = new JLabel();
			jL_title[j].setFont(new Font("TimesRoman", Font.BOLD, 12));
			jL_title[j].setText(dayOfWeek[j]);
			jL_title[j].setHorizontalAlignment(JLabel.CENTER);
			jL_title[j].setBorder(BorderFactory.createRaisedBevelBorder());
			jP_north.add(jL_title[j]);
		}
		jL_title[0].setForeground(Color.red);
		jL_title[6].setForeground(Color.blue);
	}

	public void setShowDayTextField(JTextField[] text) {

		jTF_showDay = text;
		for (int i = 0; i < jTF_showDay.length; i++) {
			jTF_showDay[i].setFont(new Font("TimesRoman", Font.BOLD, 15));
			jTF_showDay[i].setHorizontalAlignment(JTextField.CENTER);
			jTF_showDay[i].setEditable(false);
			jP_center.add(jTF_showDay[i]);
		}

	}

	public void setCalendarMessage(CalendarMessage calendarMessage) {
		this.calendarMessage = calendarMessage;
	}

	/* 将每个月都有那些天按正确位置摆放进这个CalendarPad里面 */
	public void showMonthCalendar() {
		String[] a = calendarMessage.getMonthCalendar();
		for (int i = 0; i < 42; i++)
			jTF_showDay[i].setText(a[i]);
		validate();
	}
}
