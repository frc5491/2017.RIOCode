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
Thread t = new Thread(() -> {
boolean allowCam1 = false;
UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
camera1.setResolution(320, 240);
camera1.setFPS(30);
UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
camera2.setResolution(320, 240);
camera2.setFPS(30);
CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);
CvSource outputStream = CameraServer.getInstance().putVideo("Switcher", 320, 240);
Mat image = new Mat();
while(!Thread.interrupted()) {
if(ButtonBoard.getRawButton(2)) {
allowCam1 = !allowCam1;
}
if(allowCam1){
cvSink2.setEnabled(false);
cvSink1.setEnabled(true);
cvSink1.grabFrame(image);
} else{
cvSink1.setEnabled(false);
cvSink2.setEnabled(true);
cvSink2.grabFrame(image);
}
outputStream.putFrame(image);
}
});
t.start();;

/*visionThread = new Thread(() -> {
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
        visionThread.start();*/
}


//-----------------------------------------------------------------------------------------------------------------
//Put Code In Here If You Want It To Run Once On Autonomous Startup;

@Override
public void autonomousInit() {
timer.reset();
timer.start();;
}

// -----------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Periodically <Or Check For A Changes> During Autonomous;

@Override
public void autonomousPeriodic() {
myTimer = timer.get();

if (ButtonBoard.getRawButton(5) == false | ButtonBoard.getRawButton(6) == false) { //A Mode
if (myTimer < 2.0) {
myRobot.drive(0.1, -1.0); // <-- (Speed, Rotation) Of Wheels When The If Then Statement Is True;
}
if (myTimer < 3.0 | myTimer > 2.0) {
myRobot.drive(0.1, -1.0);
}
if (myTimer < 4.0 | myTimer > 3.0) {
myRobot.drive(0.1, -1.0);
}
if (myTimer < 4.0 | myTimer > 3.0) {
myRobot.drive(0.1, -1.0);
}
if (myTimer < 5.0 | myTimer > 4.0) {
myShooter.setSpeed(0/*The Sonic Radar Sensor Speed*/);
}
if (myTimer < 6.0 | myTimer > 5.0) {
myRobot.drive(0.1, -1.0);
}
if (myTimer < 7.0 | myTimer > 6.0) {
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
if (myTimer < 5.0 | myTimer > 4.0) {
myShooter.setSpeed(0/*The Sonic Radar Sensor Speed*/);
}
if (myTimer < 6.0 | myTimer > 5.0) {
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
if (myTimer < 5.0 | myTimer > 4.0) {
myShooter.setSpeed(0/*The Sonic Radar Sensor Speed*/);
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

// -----------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Once On Manual Control <Teleop> Startup; 
@Override
public void teleopInit() {
if (ButtonBoard.getRawButton(1) == false) {
myBallColl.set(5.0);;
}
}

// ----------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Periodically <Or Check For A Changes> During Manual Control <Teleop>;

	/* ButtonBoard Button Assignments
    Button 1 = Manual Toggle Switch;
    Button 2 = Camera Change Switch;
    Button 3 = Ball Collector Toggle Switch;
    Button 4 = Shooter Button;
    Autonomous Mode 1 (Start In Position One Shooting Mode) = Port 5 = false & Port 6 = false;
    Autonomous Mode 2 (Start In Position Two Gear Placement In Front) = Port 5 = false & Port 6 = true;
    Autonomous Mode 3 (Start In Position Three Gear Placement In Back Right Side) = Port 5 = true & Port 6 = false;
    Autonomous Mode 4 (Start In All Positions Cross Line) = Port 5 = true & Port 6 = true;
    Button 7 =  
    Button 8 =
    Button 9 =
    Button 10 =
    Button 11 =
    Button 12 = Co-Pilot Shooter Speed Dial;
   `Button 13 =
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
if (ButtonBoard.getRawButton(2) == false) {
}
if (ButtonBoard.getRawButton(2) == true) {
System.out.println(ButtonBoard.getRawButton(2));
}
}
// Button 3
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getRawButton(3) == false) {
myBallColl.set(0.0);
}
if (ButtonBoard.getRawButton(3) == true) {
myBallColl.set(5.0);
System.out.println(ButtonBoard.getRawButton(3));
}
}
//Button 4
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getRawButton(4) == false) {
myShooter.setSpeed(0.0);
}
if (ButtonBoard.getRawButton(4) == true) {
myShooter.setSpeed(ButtonBoard.getThrottle());
System.out.println(ButtonBoard.getRawButton(4));
}
}
//Button 5
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getRawButton(5) == false) {
}
if (ButtonBoard.getRawButton(5) == true) {
System.out.println(ButtonBoard.getRawButton(5));
}
}
//Button 6
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getRawButton(6) == false) {
}
if (ButtonBoard.getRawButton(6) == true) {
System.out.println(ButtonBoard.getRawButton(6));
}
}
//Button 7
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getRawButton(7) == false) {
}
if (ButtonBoard.getRawButton(7) == true) {
System.out.println(ButtonBoard.getRawButton(7));
}
}
//Button 8
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getRawButton(8) == false) {
}
if (ButtonBoard.getRawButton(8) == true) {
System.out.println(ButtonBoard.getRawButton(8));
}
}
//Button 9
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getRawButton(9) == false) {
}
if (ButtonBoard.getRawButton(9) == true) {
System.out.println(ButtonBoard.getRawButton(9));
}
}
 //Button 10
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getRawButton(10) == false) {
}
if (ButtonBoard.getRawButton(10) == true) {
System.out.println(ButtonBoard.getRawButton(10));
}
}
//Button 11
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getRawButton(11) == false) {
}
if (ButtonBoard.getRawButton(11) == true) {
System.out.println(ButtonBoard.getRawButton(11));
}
}
//Throttle 12
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getThrottle() > 0) {
System.out.println(ButtonBoard.getThrottle());
}
}
//Throttle 12
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getThrottle() > 0) {
System.out.println(ButtonBoard.getThrottle());
}
}
//Throttle 12
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getThrottle() > 0) {
System.out.println(ButtonBoard.getThrottle()); 
}
}
//Throttle 12
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getThrottle() > 0) {
System.out.println(ButtonBoard.getThrottle()); 
}
}
//Throttle 12
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getThrottle() > 0) {
System.out.println(ButtonBoard.getThrottle());
}
}
//Throttle 12
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getThrottle() > 0) {
System.out.println(ButtonBoard.getThrottle());
}
}
//Throttle 12
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getThrottle() > 0) {
System.out.println(ButtonBoard.getThrottle()); 
}
}
//Throttle 12
if (ButtonBoard.getRawButton(1) == true) {
if (ButtonBoard.getThrottle() > 0) {
System.out.println(ButtonBoard.getThrottle());
}
}
}
// ---------------------------------------------------------------------------------------------------------------
// Put Code In Here If You Want It To Run Periodically <Or Check For Changes> During Test Mode;

@Override
public void testPeriodic() {
LiveWindow.run();

myRobot.arcadeDrive(stick.getY(), -stick.getX()); //Test Driving

if (stick.getRawButton(1)) { 
	myShooter.set(stick.getThrottle()); 
	if (stick.getRawButton(3)) { 
		myShooter.set(stick.getThrottle()/2); //Test Shooter
	}
}

if (stick.getRawButton(2)) { 
	myBallColl.set(5.0); 
	}else{ myBallColl.set(0.0); } //Test Ball Collector
	}
}



