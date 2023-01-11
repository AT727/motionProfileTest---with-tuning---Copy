package frc.robot.auto.Tuning254;

import frc.robot.auto.Tuning254.AutoModeEndedException;

public class DoNothingMode extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        System.out.println("doing nothing");
    }
}
