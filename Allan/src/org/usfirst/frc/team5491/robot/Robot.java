// ----------------------------------------------------------------------------------------------------------------
// Importations Of Classes From The Wipilib, If You Want To Use A Class Import It First;
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

//-----------------------------------------------------------------------------------------------------------------
// Public Classes & Variables; Always Running;

public class Robot extends IterativeRobot {
	RobotDrive myRobot = new RobotDrive(0,1);
	//Spark myShooter = new Spark(2);
	Spark myBallColl = new Spark(3);
	Victor myShooter = new Victor(2);
	Joystick stick = new Joystick(0);
	Joystick ButtonBoard = new Joystick(1);
	Timer timer = new Timer();
	Thread visionThread;
	double myTimer;
	
// ------------------------------------------------------------------------------------------------------------------
// Camera Code; Runs Once When Robot Boots Up;

@Override
public void robotInit() {
	Thread t = new Thread(() -> {
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
			t.start();;
}

// ----------------------------------------------------------------------------------------------------------------
//Put Code In Here If You Want It To Run Once On Autonomous Startup;

@Override
public void autonomousInit() {
	timer.reset();
	timer.start();;
}

// ----------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Periodically <Or Check For A Change> During Autonomous;

@Override
public void autonomousPeriodic() {
	myTimer = timer.get();
	
	if (ButtonBoard.getRawButton(5) == false | ButtonBoard.getRawButton(6) == false) { //A Mode
		if (myTimer < 2.0) {
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
		
		if (ButtonBoard.getRawButton(5) == false | ButtonBoard.getRawButton(6) == true) { //B Mode
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
			if (ButtonBoard.getRawButton(5) == true | ButtonBoard.getRawButton(6) == false) { //C Mode
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
			}
				
				if (ButtonBoard.getRawButton(5) == true | ButtonBoard.getRawButton(6) == true) { //D Mode
					if (myTimer < 2.0) {
						myRobot.drive(0.1, -1.0);
					}
					if (myTimer < 3.0 | myTimer > 2.0) {
						myRobot.drive(0.1, -1.0);
					}
				}
			}

// ----------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Once On Manual Control <Teleop> Startup;

@Override
public void teleopInit() {
	if (ButtonBoard.getRawButton(1) == false) {
		//myBallColl.set(5.0);;
	}
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
	
	//Drive Code
	myRobot.arcadeDrive(stick.getY(), -stick.getX());
	
	//Button 1
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
				}
				
				//Button 5
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
				}
				}
				
// ---------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Periodically <Or Check For A Change> During Test Mode;

@Override
public void testPeriodic() {
	//Live.Window.run;
	
	myRobot.arcadeDrive(stick.getY(), -stick.getX());
	
	if (stick.getRawButton(1)) {
		myShooter.set(stick.getThrottle());
		}else{ myShooter.set(0.0);}
	}
}










