// ----------------------------------------------------------------------------------------------------------------
		// Importations Of Classes From The WipiLib, If You Want To Use A Class Import It First;

package org.usfirst.frc.team5491.robot;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.*;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;


// -----------------------------------------------------------------------------------------------------------------
		// Public Classes & Variables; Always Running;

public class Robot extends IterativeRobot {
	RobotDrive myRobot = new RobotDrive(0, 1);
	Spark myShooter = new Spark(2); // <-- These Numbers Are PWM Ports Where The Speed Controllers Are Hooked Up To The RoboRio;
	Joystick stick = new Joystick(1); // <-- These Numbers Are The Port On The DriverStation Where The Controller Is Hooked Up;
	Joystick ButtonBoard = new Joystick(1);
	Timer timer = new Timer();
	Thread visionThread;
	Spark myBallColl = new Spark(3);
	double myTimer;
		
		
// -----------------------------------------------------------------------------------------------------------------
		// Camera Code; Runs Once When Robot Boots Up;
		
		@Override
		public void robotInit() {
		visionThread = new Thread(() -> {
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			camera.setResolution(720, 1080); // <-- Change Numbers For Resolution Change;
			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);
			Mat mat = new Mat();
			while (!Thread.interrupted()) {
				if (cvSink.grabFrame(mat) == 0) {
					outputStream.notifyError(cvSink.getError());
					continue;
				}
				Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
				new Scalar(255, 255, 255), 5);
				outputStream.putFrame(mat);
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();
	}


// -----------------------------------------------------------------------------------------------------------------
		// Put Code In Here If You Want It To Run Once On Autonomous Startup;
	
	@Override
	public void autonomousInit() {
		timer.reset();
		timer.start();
	}

	
// -----------------------------------------------------------------------------------------------------------------
		// Put Code In Here If You Want It To Run Periodically <Or Check For A Changes> During Autonomous;
	
	@Override
	public void autonomousPeriodic() {
		
	 myTimer = timer.get();
		
		if (myTimer < 2.0) {
		myRobot.drive(0.1, -1.0); // <-- (Speed, Rotation) Of Wheels When The If Then Statement Is True;
			
		} else {
		myRobot.drive(0.0, 0.0);
	}
}

	
// -----------------------------------------------------------------------------------------------------------------
		// Put Code In Here If You Want It To Run Once On Manual Control <Teleop> Startup; 
	
	@Override
	public void teleopInit() {	
		if (ButtonBoard.getRawButton(1) == false) {
			myBallColl.set(5.0);
		}
	}

	
// ----------------------------------------------------------------------------------------------------------------
		// Put Code In Here If You Want It To Run Periodically <Or Check For A Changes> During Manual Control <Teleop>;
	
	/* ButtonBoard Button Assignments
	 * Manual Toggle Switch = Port 1;
	 * Analog Speed Dial = Port 2;
	 * Ball Collector Toggle Switch = Port 3;
	 * Shooter Button = Port 4;
	 */
	
	@Override
	public void teleopPeriodic() { 
		
		//Drive Code
		myRobot.arcadeDrive(stick.getY(), -stick.getX());
		
		// Manual Ball Collector Code
		if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawButton(3) == false) {
						myBallColl.set(0.0);
						}
			
				if (ButtonBoard.getRawButton(3) == true) {
						myBallColl.set(5.0);
						}
				}
		
		
		// Shoot Code
		if (ButtonBoard.getRawButton(1) == false) { //Automatic Mode
			if (ButtonBoard.getRawButton(4)) { 
				myShooter.set(/* Distance Algorithm*/0);
			}
		}
		
		if (ButtonBoard.getRawButton(1) == true) { //Manual Mode
			if (ButtonBoard.getRawButton(4)){ 
				myShooter.set(ButtonBoard.getThrottle());  //Shooter Gets Speed 
			
			} else { myShooter.set(0);
		}
	}
}
	

// ---------------------------------------------------------------------------------------------------------------
		// Put Code In Here If You Want It To Run Periodically <Or Check For Changes> During Test Mode;
	
	@Override
	public void testPeriodic() {
		//LiveWindow.run();
		myRobot.arcadeDrive(stick.getY(), -stick.getX()); //Test Driving
		if (stick.getRawButton(1)) { myShooter.set(stick.getThrottle());} //Test Shooter
		if (stick.getRawButton(2)) { myBallColl.set(5.0); }else{ myBallColl.set(0.0);} //Test Ball Collector
			
	}
}
