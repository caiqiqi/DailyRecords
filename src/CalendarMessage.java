import java.util.Calendar;

public class CalendarMessage {
	
	int year = -1;
	int month = -1;
	int day = -1;

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public void setMonth(int month) {
		if (month <= 12 && month >= 1)
			this.month = month;
		else
			this.month = 1;
	}

	public int getMonth() {
		return month;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}

	public String[] getMonthCalendar() {
		String[] day = new String[42];
		Calendar calendar = Calendar.getInstance();
		
		//public final void set(int year, int month, int date, int hourOfDay, int minute)
		// ����������year��month��1��,ע��0��ʾһ��...11��ʾʮ����,���һ������minuteû��д
		calendar.set(year, month - 1, 1);

		int dayInTheWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		
		int dayAmountInTheMonth = getDayAmountInTheMonth();
		
		//��������㷨û�㶮������
		for (int i = 0; i < dayInTheWeek; i++)
			day[i] = "";
		for (int i = dayInTheWeek, n = 1; i < dayInTheWeek + dayAmountInTheMonth; i++) {
			day[i] = String.valueOf(n);
			n++;
		}
		for (int i = dayInTheWeek + dayAmountInTheMonth; i < 42; i++)
			day[i] = "";
		return day;
	}

	/**
	 * ��ȡ�ض��·ݵ�����
	 * @return
	 */
	private int getDayAmountInTheMonth() {
		
		int dayAmount = 0;
		
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12)
			dayAmount = 31;
		if (month == 4 || month == 6 || month == 9 || month == 11)
			dayAmount = 30;
		if (month == 2)
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
				dayAmount = 29;
			else
				dayAmount = 28;
		return dayAmount;
	}
}
