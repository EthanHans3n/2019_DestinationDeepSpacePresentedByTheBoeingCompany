/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;

// import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

// import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;   

import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.networktables.NetworkTableEntry;

import edu.wpi.first.wpilibj.SPI;
import com.kauailabs.navx.frc.AHRS;

import frc.robot.commands.SafeMode;
import frc.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  public static Drivetrain drivetrain;
  public static HatchLatch hatchLatch;
  public static CargoIntake cargoIntake;
  public static RumbleRumble rumble;
  public static DigitalInput cLimit;

  public static AHRS ahrs;

  public static NetworkTableEntry collisionDetection;

  public static double currAccelX;
  public static double lastAccelX;
  public static double currentJerkX;

  public static double currAccelY;
  public static double lastAccelY;
  public static double currentJerkY;

  public static boolean collisionDetected;
  public static double jerkThreshold;

  //ALWAYS INITIALIZE YOUR OI AFTER ALL THE OTHER SUBSYSTEMS
  public static OI m_oi;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    ahrs = new AHRS(SPI.Port.kMXP);

    rumble = new RumbleRumble();
    drivetrain = new Drivetrain();
    hatchLatch = new HatchLatch();
    rumble = new RumbleRumble();
    cargoIntake = new CargoIntake();

    cLimit = new DigitalInput(0);

    RobotMap.hatchGrab.set(false);
    RobotMap.hatchMove.set(false);

    ShuffleboardTab shuffTab = Shuffleboard.getTab("Drive");

    collisionDetection = shuffTab
      .add("Squared Inputs", true)
      .withWidget(BuiltInWidgets.kToggleButton)
      .getEntry();

    m_oi = new OI();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // if (collisionDetection.getBoolean(true)) {
    //   getJerk();
    //   if (collisionDetected) {
    //     new SafeMode();
    //   }
    // }
  }

  public void getJerk() {
    collisionDetected = false;

    currAccelX = ahrs.getWorldLinearAccelX();
    currentJerkX = currAccelX - lastAccelX;
    lastAccelX = currAccelX;

    currAccelY = ahrs.getWorldLinearAccelY();
    currentJerkY = currAccelY - lastAccelY;
    lastAccelY = currAccelY;
    
    if ( ( Math.abs(currentJerkX) > jerkThreshold ) ||
          ( Math.abs(currentJerkY) > jerkThreshold) ) {
        collisionDetected = true;
    }
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
