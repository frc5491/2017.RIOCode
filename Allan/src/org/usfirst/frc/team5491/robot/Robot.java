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


public class Robot extends IterativeRobot {
	RobotDrive myRobot = new RobotDrive(0, 1);
	Spark myShooter = new Spark(2);
	Joystick stick = new Joystick(1);
	Joystick ButtonBoard = new Joystick(1);
	Timer timer = new Timer();
	Thread visionThread;
	Victor myBallColl = new Victor(3);
	double myTimer;
	@Override
	public void robotInit() {	
		
		
// -----------------------------------------------------------------------------------------------------------------
		
		
		visionThread = new Thread(() -> {
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			camera.setResolution(720, 1080);
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
	
	
	@Override
	public void autonomousInit() {
		timer.reset();
		timer.start();
	}

	
// -----------------------------------------------------------------------------------------------------------------
	
	
	@Override
	public void autonomousPeriodic() {
		
	 myTimer = timer.get();
		
		if (myTimer < 2.0) {
		myRobot.drive(0.1, -1.0);
			
		} else {
		myRobot.drive(0.0, 0.0);
	}
}

	
// -----------------------------------------------------------------------------------------------------------------
	
	
	@Override
	public void teleopInit() {
	//if (stick.getRawButton(2))
	//	if (stick.getRawButton(2) == false) {
	//		myBallColl.set(0.0);
	//	}
		if (stick.getRawButton(2) == true) {
		myBallColl.set(5.0);
		} 
	
}

	
// ----------------------------------------------------------------------------------------------------------------
	
	
	@Override
	public void teleopPeriodic() {
		//Drive Code
		myRobot.arcadeDrive(stick.getY(), -stick.getX());
		
		// Shoot Code
		
		/*if (stick.getRawButton(2) == false) { //Automatic Mode
			if (stick.getRawButton(3)) { 
				myShooter.set(0);
				
			}
		}*/
		
		//if (ButtonBoard.getRawButton(0) == true) {		//Manual Mode
		if (stick.getRawButton(1)){ 
			myShooter.set(stick.getThrottle());  //Shooter Gets Speed 
		} else {
			myShooter.set(0);
		}
		System.out.println();
	}
//}
	

// ---------------------------------------------------------------------------------------------------------------
	
	
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
