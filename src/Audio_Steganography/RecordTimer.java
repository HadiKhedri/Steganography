package Audio_Steganography;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JLabel;

/**
 * This class counts record time/play time in the form of HH:MM:SS
 * @author Hadi Khedri
 *
 */
public class RecordTimer extends Thread {
	private DateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");	
	private boolean isRunning = false;
	private boolean isReset = false;
	private long startTime;
	private JLabel labelRecordTime;
        
	RecordTimer(JLabel labelRecordTime) {
		this.labelRecordTime = labelRecordTime;
	}
        @Override
	public void run() {
		isRunning = true;
		startTime = System.currentTimeMillis();
	
		while (isRunning) {
			try {
                                Thread.sleep(1000);
                                labelRecordTime.setText("Timer: " + toTimeString());
                                        
			} catch (InterruptedException ex) {
				if (isReset) {
					labelRecordTime.setText("Time: 00:00:00");
					isRunning = false;		
					
					break;
				}
			}
		}
	}
	
	/**
	 * Cancel counting record/play time.
	 */
	void cancel() {
		isRunning = false;		
	}
	
	/**
	 * Reset counting to "00:00:00"
	 */
	void reset() {
		isReset = true;
		isRunning = false;
	}
	
	/**
	 * Generate a String for time counter in the format of "HH:mm:ss"
	 * @return the time counter
	 */
	private String toTimeString() {
		long now = System.currentTimeMillis();
		Date current = new Date(now - startTime);
		dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timeCounter = dateFormater.format(current);
		return timeCounter;
	}
}