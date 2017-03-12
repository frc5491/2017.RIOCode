// ----------------------------------------------------------------------------------------------------------------
// Importations Of Classes From The Wipilib, If You Want To Use A Class Import It First;
package org.usfirst.frc.team5491.robot;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DigitalInput;
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

//-----------------------------------------------------------------------------------------------------------------
// Public Classes & Variables; Always Running;

public class Robot extends IterativeRobot {
	Thread visionThread;
	RobotDrive myRobot = new RobotDrive(0,1);
	Spark myClimber = new Spark(2);
	DigitalInput AutonomousButtonBoardB1 = new DigitalInput(0);
	DigitalInput AutonomousButtonBoardB2 = new DigitalInput(1);
	Joystick stick = new Joystick(0);
	Joystick ButtonBoard = new Joystick(1);
	Timer timer = new Timer();
	int AutoMode;
	int DriveMode;
	double myTimer;
	
// ------------------------------------------------------------------------------------------------------------------
// Camera Code; Runs Once When Robot Boots Up;

@Override
public void robotInit() {
	visionThread = new Thread(() -> {
		// Get the UsbCamera from CameraServer
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		// Set the resolution
		camera.setResolution(160, 120);
		camera.setFPS(30);
		// Get a CvSink. This will capture Mats from the camera
		CvSink cvSink = CameraServer.getInstance().getVideo();
		// Setup a CvSource. This will send images back to the Dashboard
		CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 160, 120);

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
	
	/*Thread t = new Thread(() -> {
		boolean allowCam1 = false;
		UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
		camera1.setResolution(200, 150);
		camera1.setFPS(30);
		UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
		camera2.setResolution(200, 150);
		camera2.setFPS(30);
		CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
		CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);
		CvSource outputStream = CameraServer.getInstance().putVideo("Switcher", 200, 150);
		Mat image = new Mat();
		while(!Thread.interrupted()) {
			if(stick.getRawButton(2) == true) {
				allowCam1 = !allowCam1;
			}
			if(allowCam1){
				cvSink2.setEnabled(false);
				cvSink1.setEnabled(true);
				cvSink1.grabFrame(image);
			}else{
				cvSink1.setEnabled(false);
				cvSink2.setEnabled(true);
				cvSink2.grabFrame(image);
			}
			outputStream.putFrame(image);
			}
			});
			t.start();;*/


// ----------------------------------------------------------------------------------------------------------------
//Put Code In Here If You Want It To Run Once On Autonomous Startup;

@Override
public void autonomousInit() {
	timer.reset();
	timer.start();;
	if (AutonomousButtonBoardB1.get() == false | AutonomousButtonBoardB2.get() == false) {
		AutoMode = 1;
	}
	if (AutonomousButtonBoardB1.get() == false | AutonomousButtonBoardB2.get() == true) {
		AutoMode = 2;
	}
	if (AutonomousButtonBoardB1.get() == true | AutonomousButtonBoardB2.get() == false) {
		AutoMode = 3;
	}
	if (AutonomousButtonBoardB1.get() == true | AutonomousButtonBoardB2.get() == true) {
		AutoMode = 4;
	}
}

// ----------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Periodically <Or Check For A Change> During Autonomous;

@Override
public void autonomousPeriodic() {
	myTimer = timer.get();
	
	/*if (Num == 1) { //A Mode
		if (myTimer < 0.25) {
			myRobot.drive(0.1, -1.0);
		}
		if (myTimer < 3.0 | myTimer > 2.0) {
			myRobot.drive(-0.2, 0.0);
			
		}
		if (myTimer < 4.0 | myTimer > 3.0) {
			myRobot.drive(0.1, -1.0);
			
		}
		if (myTimer < 5.0 | myTimer > 4.0) {
			myRobot.drive(0.1, -1.0);
			
		}
		if (myTimer < 6.0 | myTimer > 5.0) {
			myShooter.set(1.0);
			
		}
		if (myTimer < 7.0 | myTimer > 6.0) {
			myRobot.drive(0.1, -1.0);
			
		}
		if (myTimer < 8.0 | myTimer > 7.0) {
			myRobot.drive(0.1, -1.0);
			
		}
	}
		
		if (Num == 2) { //B Mode
			if (myTimer < 2.0) {
				myRobot.drive(0.1, -1.0);
			}
			if (myTimer < 3.0 | myTimer > 2.0) {
				myRobot.drive(0.1, -1.0);
				
			}
			if (myTimer < 4.0 | myTimer > 3.0) {
				myRobot.drive(0.1, -1.0);
				
			}
			if (myTimer < 6.0 | myTimer > 5.0) {
				myShooter.set(1.0);
				
			}
			if (myTimer < 5.0 | myTimer > 4.0) {
				myRobot.drive(0.1, -1.0);
				
			}
			if (myTimer < 7.0 | myTimer > 6.0) {
				myRobot.drive(0.1, -1.0);
				
			}
			if (myTimer < 8.0 | myTimer > 7.0) {
				myRobot.drive(0.1, -1.0);
				
			}
		}
			*/if (AutoMode == 3) { //C Mode
				if (myTimer < 9.5) {
					myRobot.drive(0.5, 0.0);
				}
				/*if (myTimer < 3.0 | myTimer > 2.0) {
					myRobot.drive(0.1, -1.0);
					
				}
				if (myTimer < 4.0 | myTimer > 3.0) {
					myRobot.drive(0.1, -1.0);
					
				}
				if (myTimer < 6.0 | myTimer > 5.0) {
					myShooter.set(1.0);
					
				}*/
			}
				
				if (AutoMode == 4) { //D Mode
					if (myTimer < 2.5) {
						myRobot.arcadeDrive(2.0, 0.0);
					}
				}
			}

// ----------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Once On Manual Control <Teleop> Startup;

@Override
public void teleopInit() {
}

// -----------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Periodically <Or Check For A Change> During Manual Control <Teleop>;

/*ButtonBoard Button Assignments
Button 1 = Manual Toggle Switch
Button 2 = Camera Change Switch
Button 3 = Ball Collector Switch
Button 4 = Shooter Button
Autonomous Mode A = Start In Position 1 <Shooting> Button 5 = false Button 6 = false
Autonomous Mode B = Start In Position 2 <Gear Placement In Front> Button 5 = false Button 6 = true
Autonomous Mode C = Start In Position 3 <Gear Placement in Back Right> Button 5 = true Button 6 = false
Autonomous Mode D = Start In Position 4 <Start In All Positions Cross Line> Button 5 = true Button 6 = true
Button 7 =
Button 8 =
Button 9 =
Button 10 =
Button 11 =
Button 12 = Co-Pilot Speed Dial
Button 13 =
Button 14 =
Button 15 =
Button 16 =
Button 17 =
Button 18 =
Button 19 =
*/

@Override
public void teleopPeriodic() {
	
	if (stick.getRawButton(3) == true) {
		DriveMode = 1;
		myClimber.set(5.0);
	}else{ if (stick.getRawButton(4) == true) {
		DriveMode = 1;
		myClimber.set(-5.0);
	}else{ myClimber.set(0.0);
		   DriveMode = 2;
		   }
		   
	if (DriveMode == 2) {
	myRobot.arcadeDrive(-stick.getY(), -stick.getX());
	}
	if (DriveMode == 1) {
		myRobot.arcadeDrive(stick.getY(), -stick.getX());
	}
}
}
	/*//Button 1
	if (ButtonBoard.getRawButton(1) == true) {
		System.out.println(ButtonBoard.getRawButton(1));
	}
	
	//Button 2
		if (ButtonBoard.getRawButton(1) == true) {
		if (ButtonBoard.getRawButton(2) == true) {
			System.out.println(ButtonBoard.getRawButton(2));
		}
		}
		
		//Button 3
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawButton(3) == true) {
					myBallColl.set(5.0);
					System.out.println(ButtonBoard.getRawButton(3));
				}else{ myBallColl.set(0.0);
				}
				}
				
				//Button 4
				if (ButtonBoard.getRawButton(1) == false) {
				if (ButtonBoard.getRawButton(4) == true) {
					myShooter.set(ButtonBoard.getRawAxis(12));
					System.out.println(ButtonBoard.getRawButton(4));
				}else{ myShooter.set(0.0);
				}
				}*/
				
				/*//Button 5
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawButton(5) == true) {
					
					System.out.println(ButtonBoard.getRawButton(5));
				}else{
				}
				}
				
				//Button 6
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawButton(5) == true) {

					System.out.println(ButtonBoard.getRawButton(5));
				}else{
				}
				}
				
				//Button 7
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawButton(5) == true) {

					System.out.println(ButtonBoard.getRawButton(5));
				}else{
				}
				}
				
				//Button 8
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawButton(5) == true) {

					System.out.println(ButtonBoard.getRawButton(5));
				}else{
				}
				}
				
				//Button 9
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawButton(5) == true) {

					System.out.println(ButtonBoard.getRawButton(5));
				}else{
				}
				}
				
				//Button 10
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawButton(5) == true) {

					System.out.println(ButtonBoard.getRawButton(5));
				}else{
				}
				}
				
				//Button 11
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawButton(5) == true) {

					System.out.println(ButtonBoard.getRawButton(5));
				}else{
				}
				}
				
				//Throttle 12
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawAxis(12) > 0) {
					System.out.println(ButtonBoard.getRawAxis(12));
				}
				}
				
				//Throttle 13
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawAxis(13) > 0) {
					System.out.println(ButtonBoard.getRawAxis(13));
				}
				}
				
				//Throttle 14
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawAxis(14) > 0) {
					System.out.println(ButtonBoard.getRawAxis(14));
				}
				}
				
				//Throttle 15
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawAxis(15) > 0) {
					System.out.println(ButtonBoard.getRawAxis(15));
				}
				}
				
				//Throttle 16
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawAxis(16) > 0) {
					System.out.println(ButtonBoard.getRawAxis(16));
				}
				}
				
				//Throttle 17
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawAxis(17) > 0) {
					System.out.println(ButtonBoard.getRawAxis(17));
				}
				}
				
				//Throttle 18
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawAxis(18) > 0) {
					System.out.println(ButtonBoard.getRawAxis(18));
				}
				}
				
				//Throttle 19
				if (ButtonBoard.getRawButton(1) == true) {
				if (ButtonBoard.getRawAxis(19) > 0) {
					System.out.println(ButtonBoard.getRawAxis(19));
				}
				}*/
				
				
// ---------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Periodically <Or Check For A Change> During Test Mode;

@Override
public void testPeriodic() {
	//Live.Window.run;
	
	if (ButtonBoard.getRawButton(1) == true) {
		System.out.println(ButtonBoard.getRawButton(1));
	}
	if (ButtonBoard.getRawButton(2) == true) {
		System.out.println(ButtonBoard.getRawButton(2));
	}
	if (ButtonBoard.getRawButton(3) == true) {
		System.out.println(ButtonBoard.getRawButton(3));
	}
	if (ButtonBoard.getRawButton(4) == true) {
		System.out.println(ButtonBoard.getRawButton(4));
	}
	if (ButtonBoard.getRawButton(5) == true) {
		System.out.println(ButtonBoard.getRawButton(5));
	}
	if (ButtonBoard.getRawButton(6) == true) {
		System.out.println(ButtonBoard.getRawButton(6));
	}
	if (ButtonBoard.getRawButton(7) == true) {
		System.out.println(ButtonBoard.getRawButton(7));
	}
	if (ButtonBoard.getRawButton(8) == true) {
		System.out.println(ButtonBoard.getRawButton(8));
	}
	if (ButtonBoard.getRawButton(9) == true) {
		System.out.println(ButtonBoard.getRawButton(9));
	}
	if (ButtonBoard.getRawAxis(1) > 0) {
		System.out.println(ButtonBoard.getRawAxis(1));
	}
	if (ButtonBoard.getRawAxis(2) > 0) {
		System.out.println(ButtonBoard.getRawAxis(2));
	}
		
	myRobot.arcadeDrive(-stick.getY(), -stick.getX());
	}
}










