package hu.bme.mit.train.system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.bme.mit.train.controller.TrainControllerImpl;
import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;
import hu.bme.mit.train.system.TrainTachograf;
import hu.bme.mit.train.system.TrainSystem;
import java.time.LocalDateTime;

public class TrainSystemTest {

	TrainController controller;
	TrainSensor sensor;
	TrainUser user;
	
	@Before
	public void before() {
		TrainSystem system = new TrainSystem();
		controller = system.getController();
		sensor = system.getSensor();
		user = system.getUser();

		sensor.overrideSpeedLimit(50);
	}
	
	@Test
	public void OverridingJoystickPosition_IncreasesReferenceSpeed() {
		sensor.overrideSpeedLimit(10);

		Assert.assertEquals(0, controller.getReferenceSpeed());
		
		user.overrideJoystickPosition(5);

		controller.followSpeed();
		Assert.assertEquals(5, controller.getReferenceSpeed());
		controller.followSpeed();
		Assert.assertEquals(10, controller.getReferenceSpeed());
		controller.followSpeed();
		Assert.assertEquals(10, controller.getReferenceSpeed());
	}

	@Test
	public void OverridingJoystickPositionToNegative_SetsReferenceSpeedToZero() {
		user.overrideJoystickPosition(4);
		controller.followSpeed();
		user.overrideJoystickPosition(-5);
		controller.followSpeed();
		Assert.assertEquals(0, controller.getReferenceSpeed());
	}

	@Test
	public void OverrideSpeedLimit(){
		controller.setSpeedLimit(110);
		Assert.assertEquals(110, controller.getSpeedLimit());
		controller.setSpeedLimit(120);
		Assert.assertEquals(120, controller.getSpeedLimit());
		controller.setSpeedLimit(130);
		Assert.assertEquals(120, controller.getSpeedLimit());
	}

	@Test
	public void StaticVersion(){
		Assert.assertEquals(1, TrainControllerImpl.version);
	}

	@Test
	public void Log(){
		TrainTachograf tachograf = new TrainTachograf(controller, user);
		LocalDateTime t1 = LocalDateTime.now();
		LocalDateTime t2 = t1.plusDays(1);
		System.out.println(t1);
		System.out.println(t2);
		tachograf.log(t1);
		Assert.assertEquals(true, tachograf.containsLogByTime(t1));
		tachograf.log(t2);
		Assert.assertEquals(true, tachograf.containsLogByTime(t2));
	}
}
