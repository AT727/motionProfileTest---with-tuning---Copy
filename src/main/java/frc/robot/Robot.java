// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.auto.Tuning254.AutoModeExecutor;
import frc.robot.auto.Tuning254.AutoModeBase;
import java.util.Optional;
import frc.robot.subsystems.Drive254;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.controlboard.*;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private final Drive254 mDrive = new Drive254();
  private RobotContainer m_robotContainer;
  private AutoModeExecutor mAutoModeExecutor = new AutoModeExecutor();
  private AutoModeSelector mAutoModeSelector = new AutoModeSelector();
  private final ControlBoard mControlBoard = ControlBoard.getInstance();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    mAutoModeSelector.updateModeCreator();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    try {
            mSubsystemManager.outputToSmartDashboard();
            mAutoModeSelector.outputToSmartDashboard();
//            mKinematicSelector.outputToSmartDashboard();
            SmartDashboard.putNumber("Timestamp", Timer.getFPGATimestamp());
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    if (mAutoModeExecutor != null) {
      mAutoModeExecutor.stop();
      mAutoModeSelector.reset();
            mAutoModeSelector.updateModeCreator();
            mAutoModeExecutor = new AutoModeExecutor();
  }
  }

  @Override
  public void disabledPeriodic() {
    mAutoModeSelector.updateModeCreator();

    Optional<AutoModeBase> autoMode = mAutoModeSelector.getAutoMode();
    if (autoMode.isPresent() && autoMode.get() != mAutoModeExecutor.getAutoMode()) {
        System.out.println("Set auto mode to: " + autoMode.get().getClass().toString());
        mAutoModeExecutor.setAutoMode(autoMode.get());
    }
  }

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
//     m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    mAutoModeExecutor.start();
    // schedule the autonomous command (example)
//     if (m_autonomousCommand != null) {
//       m_autonomousCommand.schedule();
//     }
    mAutoModeSelector.getAutoMode().get().setStartPose();
    mAutoModeExecutor.start();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (mAutoModeExecutor != null) {
      mAutoModeExecutor.stop();
  }
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    double throttle = mControlBoard.getThrottle();
    double turn = mControlBoard.getTurn();
    boolean quickTurn = mControlBoard.getQuickTurn();
    mDrive.setCheesyishDrive(throttle, turn, quickTurn);
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
