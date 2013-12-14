package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * This is the main activity of the HelloIOIO example application.
 * 
 * It displays a toggle button on the screen, which enables control of the
 */
public class MainActivity extends IOIOActivity
{
	private ToggleButton button_;
	private TextView mText;
	private ScrollView scroller;
	private int TotalCount = 5000;// 516 was 95

	/**
	 * Called when the activity is first created. Here we normally initialize
	 * our GUI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		button_ = (ToggleButton) findViewById(R.id.button);
		// mText = (TextView) findViewById(R.id.text);
	}

	/**
	 * This is the thread on which all the IOIO activity happens. It will be run
	 * every time the application is resumed and aborted when it is paused. The
	 * method setup() will be called right after a connection with the IOIO has
	 * been established (which might happen several times!). Then, loop() will
	 * be called repetitively until the IOIO gets disconnected.
	 */
	class Looper extends BaseIOIOLooper
	{
		private DigitalOutput led_;// on-board LED
		private DigitalOutput rightMotorClock;
		private DigitalOutput leftMotorClock;
		private DigitalOutput motorEn;
		private PwmOutput rightMotorPWM;
		private PwmOutput leftMotorPWM;
		private DigitalOutput rightMotorDirection;
		private DigitalOutput leftMotorDirection;

		/**
		 * Called every time a connection with IOIO has been established.
		 * Typically used to open pins.
		 * 
		 * @throws ConnectionLostException
		 *             When IOIO connection is lost.
		 * 
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#setup()
		 */
		@Override
		protected void setup() throws ConnectionLostException
		{
			led_ = ioio_.openDigitalOutput(0, true); // LED pin
//			rightMotorClock = ioio_.openDigitalOutput(28);
//			leftMotorClock = ioio_.openDigitalOutput(27);
			motorEn = ioio_.openDigitalOutput(3); // motor enable pin both
			motorEn.write(true);
//			rightMotorClock.write(true);
//			leftMotorClock.write(true);
			rightMotorPWM = ioio_.openPwmOutput(28, 1000);// pin #, frequency right motor
			leftMotorPWM = ioio_.openPwmOutput(27, 1000); // pin #, frequency left motor
			rightMotorPWM.setPulseWidth(10);
			leftMotorPWM.setPulseWidth(10);
			rightMotorDirection = ioio_.openDigitalOutput(20); // Right Direction Pin
			rightMotorDirection.write(false);
			leftMotorDirection = ioio_.openDigitalOutput(21); // Left Direction Pin
			leftMotorDirection.write(true);
			// log("hello");
		}

		/**
		 * Called repetitively while the IOIO is connected.
		 * 
		 * @throws ConnectionLostException
		 *             When IOIO connection is lost.
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#loop()
		 */
		@Override
		public void loop() throws ConnectionLostException
		{
			led_.write(!button_.isChecked());
			try
			{
				// rightMotorClock.write(true);
				// rightMotorClock.write(false);
				// leftMotorClock.write(true);
				// leftMotorClock.write(false);
				Thread.sleep(10);
				// // if (TotalCount-- < 0) {
				// motorEn.write(false);
				// }

			} catch (InterruptedException e)
			{
			}
		}
	}

	/**
	 * A method to create our IOIO thread.
	 * 
	 * @see ioio.lib.util.AbstractIOIOActivity#createIOIOThread()
	 */
	@Override
	protected IOIOLooper createIOIOLooper()
	{
		return new Looper();
	}

	public void log(final String msg)
	{
		runOnUiThread(new Runnable()
		{
			public void run()
			{
				mText.append(msg);
				mText.append("\n");
				scroller.smoothScrollTo(0, mText.getBottom());
			}
		});
	}
}