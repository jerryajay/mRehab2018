package com.example.matt2929.strokeappdec2017.Workouts;

import android.util.Log;

import com.example.matt2929.strokeappdec2017.ListenersAndTriggers.OutputWorkoutData;
import com.example.matt2929.strokeappdec2017.ListenersAndTriggers.OutputWorkoutStrings;
import com.example.matt2929.strokeappdec2017.ListenersAndTriggers.SpeechTrigger;
import com.example.matt2929.strokeappdec2017.R;
import com.example.matt2929.strokeappdec2017.Utilities.SFXPlayer;

/**
 * Created by matt2929 on 1/16/18.
 */

public class WO_Sip extends SensorWorkoutAbstract {

	double thresholdPickup = 1.5;
	boolean pickedUp = false;
	double threseholdDrink = 2;
	long timeToDrink = 10000, timeDrank = 0, checkPoint = 0;
	int repCount = 0;
	boolean inCoolDown = false;
	long coolDownLength = 5000, cooldownStart = 0;

	public WO_Sip(String Name, Integer reps, SpeechTrigger speechTrigger, SFXPlayer sfxPlayer, OutputWorkoutData outputWorkoutData, OutputWorkoutStrings outputWorkoutStrings) {
		super.SensorWorkout(Name, reps, speechTrigger, sfxPlayer, outputWorkoutData, outputWorkoutStrings);
		sfxPlayer.loadSFX(R.raw.pour_water);
		sfxPlayer.loopSFX();
	}


	@Override
	public void SensorDataIn(float[] data) {
		super.SensorDataIn(data);
		if (WorkoutInProgress && !inCoolDown) {
			if (data[1] > thresholdPickup && !pickedUp) {
				pickedUp = true;
				checkPoint = System.currentTimeMillis();
			}
			if (pickedUp) {
				if (Math.abs(data[2]) > threseholdDrink || Math.abs(data[0]) > threseholdDrink) {
					timeDrank += (Math.abs(checkPoint - System.currentTimeMillis()));
					if (!sfxPlayer.isPlaying()) {
						sfxPlayer.playSFX();
					}
				} else {
					if (sfxPlayer.isPlaying()) {
						sfxPlayer.pauseSFX();
					}
				}
				checkPoint = System.currentTimeMillis();
			}
			if (timeDrank > timeToDrink) {
				repCount++;
				inCoolDown = true;
				cooldownStart = System.currentTimeMillis();
				if (sfxPlayer.isPlaying()) {
					sfxPlayer.pauseSFX();
				}
				if (repCount == reps) {
					workoutComplete = true;
				} else {
					speechTrigger.speak("Sip complete, Place the cup back down on the table and wait for the next sip.");
				}
			}
			Log.e("Sip", "picked up:" + pickedUp + " drink progress:" + (timeDrank) + "%" + data[2]);
		}
		if (inCoolDown) {
			if (Math.abs(cooldownStart - System.currentTimeMillis()) > coolDownLength) {
				inCoolDown = false;
				timeDrank = 0;
				pickedUp = false;
				speechTrigger.speak("Pick up the cup and drink");
				checkPoint = System.currentTimeMillis();
			}
		}
	}

	@Override
	public void StartWorkout() {
		super.StartWorkout();
	}

	@Override
	public boolean isWorkoutComplete() {
		return super.isWorkoutComplete();
	}

	@Override
	public WorkoutScore getScore() {
		return super.getScore();
	}

	@Override
	public void outputData(float[] f) {
		super.outputData(f);
	}

	@Override
	public void outputStrings(String[] s) {
		super.outputStrings(s);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public Integer getReps() {
		return super.getReps();
	}
}