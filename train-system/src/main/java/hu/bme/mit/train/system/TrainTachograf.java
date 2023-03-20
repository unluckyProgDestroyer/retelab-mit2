package hu.bme.mit.train.system;

import java.time.LocalDateTime;
import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class TrainTachograf{

    private TrainController controller;
    private TrainUser user;
    private Table<LocalDateTime, Integer, Integer> logTable;


    public TrainTachograf(TrainController c, TrainUser u){
        controller = c;
        user = u;
        logTable = HashBasedTable.create();
    }

    public void log(LocalDateTime in){
        if (in == null) in = LocalDateTime.now();
        logTable.put(in, user.getJoystickPosition(), controller.getReferenceSpeed());
    }

    public boolean containsLogByTime(LocalDateTime time){
        return logTable.containsRow(time);
    }
}