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
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Ultrasonic;

//-----------------------------------------------------------------------------------------------------------------
// Public Classes & Variables; Always Running;

public class Robot extends IterativeRobot {
	Thread visionThread;
	RobotDrive myRobot = new RobotDrive(0,1);
	Spark myClimber = new Spark(2);
	//DigitalInput AutonomousButtonBoardB1 = new DigitalInput(0);
	//DigitalInput AutonomousButtonBoardB2 = new DigitalInput(1);
	Joystick stick = new Joystick(0);
	//Joystick ButtonBoard = new Joystick(1);
	Timer timer = new Timer();
	int AutoMode;
	int DriveMode;
	double myTimer;
/*	Ultrasonic UltraSensor1 = new Ultrasonic(3,2); // creates the ultra object andassigns ultra to be an ultrasonic sensor which uses DigitalOutput 1 for the echo pulse and DigitalInput 1 for the trigger pulse
	Ultrasonic UltraSensor2 = new Ultrasonic(5,4); // creates the ultra object andassigns ultra to be an ultrasonic sensor which uses DigitalOutput 2 for the echo pulse and DigitalInput 2 for the trigger pulse
	Ultrasonic UltraSensor3 = new Ultrasonic(7,6); // creates the ultra object andassigns ultra to be an ultrasonic sensor which uses DigitalOutput 3 for the echo pulse and DigitalInput 3 for the trigger pulse
	Ultrasonic UltraSensor4 = new Ultrasonic(9,8); // creates the ultra object andassigns ultra to be an ultrasonic sensor which uses DigitalOutput 4 for the echo pulse and DigitalInput 4 for the trigger pulse*/
	
// ------------------------------------------------------------------------------------------------------------------
// Camera Code; Runs Once When Robot Boots Up;
	
@Override
public void robotInit() {
/*	UltraSensor1.setAutomaticMode(true);
	UltraSensor2.setAutomaticMode(true);
	UltraSensor3.setAutomaticMode(true);
	UltraSensor4.setAutomaticMode(true);*/
	
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
			//Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
					//new Scalar(255, 255, 255), 5);
			// Give the output stream a new image to display
			outputStream.putFrame(mat);
		}
	});
	visionThread.setDaemon(true);
	visionThread.start();
}

// ----------------------------------------------------------------------------------------------------------------
//Put Code In Here If You Want It To Run Once On Autonomous Startup;

@Override
public void autonomousInit() {
	timer.reset();
	timer.start();
	AutoMode = 1;
	
	
	/*if (AutonomousButtonBoardB1.get() == false && AutonomousButtonBoardB2.get() == false) {
		AutoMode = 2;
	}
	if (AutonomousButtonBoardB1.get() == true && AutonomousButtonBoardB2.get() == true) {
		AutoMode = 1;
	}
	if (AutonomousButtonBoardB1.get() == true && AutonomousButtonBoardB2.get() == false) {
		AutoMode = 3;
	}
	if (AutonomousButtonBoardB1.get() == false && AutonomousButtonBoardB2.get() == true) {
		AutoMode = 4;
	}*/
}

// ----------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Periodically <Or Check For A Change> During Autonomous;

@Override
public void autonomousPeriodic() {
	myTimer = timer.get();
	/*double range1 = UltraSensor1.getRangeInches();
	double range2 = UltraSensor2.getRangeInches();
	double range3 =	UltraSensor3.getRangeInches();
	double range4 = UltraSensor4.getRangeInches();
	double RANGE1 = range1 + 1;
	double RANGE2 = range2 + 1;
	double RANGE3 = range1 - 1;
	double RANGE4 = range2 - 1;*/
	/*if (AutoMode == 1) { //A Mode //Cross The Baseline
		System.out.println("I am Mode Baseline");
		//System.out.println(range3);
		//System.out.println(range4);
		if (myTimer < 6.0) {
			myRobot.arcadeDrive(0.5, 0.0);
		}else{
			myRobot.arcadeDrive(0.0, 0.0);
		}
	}
		
		if (AutoMode == 2) { //B Mode //Center Davit Gear Placement
			System.out.println("I am Mode Center");
			//System.out.println(range3);
			//System.out.println(range4);
			//if (range1 > 2.5 && range2 > 2.5) {
			if (myTimer < 7.5) {	
			myRobot.drive(0.5, 0.0);
			//}else{
			}else{
				myRobot.arcadeDrive(0.0, 0.0);
			}
			//}
			
		}
			
	/*	if (AutoMode == 3) { //C Mode //Left Davit Gear Placement
			System.out.println("I am Mode Left");
			System.out.println(range1);
			System.out.println(range2);
			if (myTimer < 2.0) {
				myRobot.drive(1.0, 0.0);
			}
			if (myTimer < 3.0 && myTimer > 2.0) {
				myRobot.drive(0.0, 0.3);
			}
			if (myTimer < 4.5 && myTimer > 3.0) {
				myRobot.drive(1.0, 0.0);
			}
			if (myTimer < 5.0 && myTimer > 4.5) {
				myRobot.drive(0.2, 0.0);
			}
			/*if (range3 < 97.5 && range4 < 97.5) {
					myRobot.drive(0.5, 0.0);
				}else{
					range3 = 0.0;
					range4 = 0.0;
					myRobot.drive(0.0, 0.0);
					if (!(range1 > RANGE4 && range1 < RANGE2 && range2 > RANGE3 && range2 < RANGE1)) {
						myRobot.drive(0.0, 0.1);
					}else{
						myRobot.drive(0.2, 0.0);
						if (range1 < 2.5 && range2 < 2.5) {
							myRobot.drive(0.01, 0.0);*/
						//}
				//	}
				//}
		//}
				
			/*	if (AutoMode == 4) { //D Mode //Right Davit Gear Placement
					System.out.println("I am Mode Right");
					System.out.println(range3);
					System.out.println(range4);
					if (range3 < 97.5 && range4 < 97.5) {
						myRobot.drive(0.5, 0.0);
					}else{
						range3 = 0.0;
						range4 = 0.0;
						myRobot.drive(0.0, 0.0);
						if (!(range1 > RANGE4 && range1 < RANGE2 && range2 > RANGE3 && range2 < RANGE1)) {
							myRobot.drive(0.0, -0.1);
						}else{
							myRobot.drive(0.2, 0.0);
							if (range1 < 2.5 && range2 < 2.5) {
								myRobot.drive(0.01, 0.0);
							}
						}
					}
				}*/
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
	myRobot.arcadeDrive(-stick.getY(), -stick.getX());
	if (stick.getRawButton(1) == true) {
		myClimber.set(0.1);
	}

	if (stick.getRawButton(3) == true) {
		myClimber.set(1.0);
		myRobot.arcadeDrive(stick.getY(), -stick.getX());
	}else{
		myClimber.set(0.0);
		myRobot.arcadeDrive(-stick.getY(), -stick.getX());
		}
}				
				
// ---------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Periodically <Or Check For A Change> During Test Mode;

@Override
public void testPeriodic() {
	//Live.Window.run;
	myRobot.arcadeDrive(-stick.getY(), -stick.getX());
	}
}