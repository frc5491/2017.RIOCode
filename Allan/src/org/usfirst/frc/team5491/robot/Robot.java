package org.usfirst.frc.team5491.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
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

import edu.wpi.first.wpilibj.buttons.Button;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	RobotDrive myRobot = new RobotDrive(0, 1);
	Victor myShooter = new Victor(2);
	Victor myLeft = new Victor(4);
	Victor myRight = new Victor(5);
	Joystick stick = new Joystick(0);
	Timer timer = new Timer();
	Thread visionThread;
	Victor myBallColl = new Victor(3);
	double myTimer;
	float x;
	float y;
	double m;
	Float xvar;
	Float yvar;
	Float yvarneg;
	
	@Override
	public void robotInit() {	
		
		visionThread = new Thread(() -> {
			// Get the UsbCamera from CameraServer
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			// Set the resolution
			camera.setResolution(720, 1080);

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				// Put a rectangle on the image
				Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
						new Scalar(255, 255, 255), 5);
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();
	}
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	/**
	 * This function is run once each time the robot enters autonomous mode
	 */
	@Override
	public void autonomousInit() {
		timer.reset();
		timer.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
	 myTimer = timer.get();
		
		// Drive for 2 seconds
		if (myTimer < 2.0) {
			myLeft.setSpeed(0.5);
			myRight.setSpeed(0.5);
			
		} else {
		myLeft.setSpeed(0.0);
		myRight.setSpeed(0.0);
	}
}

	/**
	 * This function is called once each time the robot enters tele-operated
	 * mode
	 */
	@Override
	public void teleopInit() {
	myBallColl.set(5.0); //Speed Variable Here for Ball Collector
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		//Drive Code
		float x = (float) stick.getX(); //Variables
		float y = (float) stick.getY();
		float m = 2;
		float n = -1;
		xvar = Float.valueOf(Float.valueOf(x) * Float.valueOf(m));
		yvar = Float.valueOf(Float.valueOf(y) * Float.valueOf(m));
		yvarneg = Float.valueOf(Float.valueOf(y) * Float.valueOf(n));
		int quadrant = 0;
		
		if (xvar == 0 | yvar < 0) {
			quadrant = 1;
		}
		
		if (xvar == 0 | yvar > 0) {
			quadrant = 2;
		}
		
		if (xvar < 0 | yvar == 0) {
			quadrant = 3;
		}
		
		if (xvar > 0 | yvar ==0) {
			quadrant = 4;
		}
		
		if (xvar < 0 | yvar < 0) {
			quadrant = 5;
		}
		
		if (xvar > 0 | yvar < 0) {
			quadrant = 6;
		}
		
		if (xvar < 0 | yvar > 0) {
			quadrant = 7;
		}
		
		if (xvar < 0 | yvar > 0) {
			quadrant = 8;
		}
		
		switch (quadrant) {
		case 1: //Forward
			myLeft.setSpeed(yvar);
			myRight.setSpeed(yvar);
			break;
		case 2: //Backward
			myLeft.setSpeed(yvar);
			myRight.setSpeed(yvar);
			break;
		case 3: //Left Turn
			myLeft.setSpeed(0);
			myRight.setSpeed(xvar);
			break;
		case 4: //Right Turn
			myLeft.setSpeed(xvar);
			myRight.setSpeed(0);
			break;
		case 5: //Quadrant 1 Hard Left Turn
			myLeft.setSpeed(yvarneg);
			myRight.setSpeed(yvar);
			break;
		case 6: //Quadrant 2 Hard Right Turn
			myLeft.setSpeed(yvar);
			myRight.setSpeed(yvarneg);
			break;
		case 7: // Quadrant 3 Hard Backwards Right Turn
			myLeft.setSpeed(yvar);
			myRight.setSpeed(yvarneg);
			break;
		case 8: //Quadrant 4 Hard Backwards Left Turn
			myLeft.setSpeed(yvarneg);
			myRight.setSpeed(yvar);
			break;
		}
		
		//myRobot.arcadeDrive(stick.getY(), -stick.getX()); //Reverse Motor Code Norm=(stick);
		// Shoot Code
		if (stick.getRawButton(1)){ 
			myShooter.setSpeed(stick.getThrottle());  //Shooter Gets Speed 
		} else {
			myShooter.setSpeed(0);
		}
		System.out.println();
	}

		
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
