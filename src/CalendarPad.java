import javax.swing.*;

import java.awt.*;

/**
 * ���Ͻ���ʾ���ڵ��Ǹ����
 * 
 */
@SuppressWarnings("serial")
public class CalendarPad extends JPanel {

	int year;
	int month; 
	int day;

	CalendarMessage calendarMessage;
	private JTextField[] jTF_showDay;
	/* ĳһ�µ�ÿһ����JLabel��ʾ */
	private JLabel jL_title[];
	/*����������ַ���������Ϣװ��jL_title[]���棬Ȼ����װ��jP_north����*/
	private String[] dayOfWeek = { "SUN��", "MONһ", "TUE��", "WED��", "THU��", "FRI��",
			"SAT��" };

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

	/* ��ÿ���¶�����Щ�찴��ȷλ�ðڷŽ����CalendarPad���� */
	public void showMonthCalendar() {
		String[] a = calendarMessage.getMonthCalendar();
		for (int i = 0; i < 42; i++)
			jTF_showDay[i].setText(a[i]);
		validate();
	}
}
