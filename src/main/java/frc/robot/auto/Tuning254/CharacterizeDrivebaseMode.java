package frc.robot.auto.Tuning254;

import frc.robot.auto.Tuning254.AutoModeEndedException;
import frc.robot.auto.Tuning254.CollectAccelerationDataAction;
import frc.robot.auto.Tuning254.CollectVelocityDataAction;
import frc.robot.auto.Tuning254.WaitAction;
import com.team254.lib.physics.DriveCharacterization;

import java.util.ArrayList;
import java.util.List;

public class CharacterizeDrivebaseMode extends AutoModeBase {
    private final boolean reverse;
    private final boolean turn;

    public CharacterizeDrivebaseMode(boolean reverse, boolean turn) {
        this.reverse = reverse;
        this.turn = turn;
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        List<DriveCharacterization.DataPoint> velocityData = new ArrayList<>();
        List<DriveCharacterization.DataPoint> accelerationData = new ArrayList<>();

        runAction(new CollectVelocityDataAction(velocityData, reverse, turn));
        runAction(new WaitAction(10));
        runAction(new CollectAccelerationDataAction(accelerationData, reverse, turn));

        DriveCharacterization.CharacterizationConstants constants = DriveCharacterization.characterizeDrive(velocityData, accelerationData);

        System.out.println("ks: " + constants.ks);
        System.out.println("kv: " + constants.kv);
        System.out.println("ka: " + constants.ka);
    }
}
