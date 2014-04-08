package no.wafflewings.gdxgame;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TunnelGenerator {
	private final int turnStepsMax;
	private final int wallStepMax = 6;
	private final float wMaxEasy, wMaxHard;
	private final float edgeMinEasy, edgeMinHard;
	private final float edgeMaxEasy, edgeMaxHard;
	private final float vel;
	
	private int steps;
	private float angVel;
	private float topVel, botVel;
	private int topSteps, botSteps;
	
	private TunnelStep lastStep;
	
	/**
	 * @param stepSize distance between each step
	 * @param turnStepsMax maximum number of steps before changing angular velocity
	 * @param wMax maximum angular velocity per step
	 * @param edgeMin minimum distance between mid and edge
	 * @param edgeMax maximum distance between mid and edge
	 */
	public TunnelGenerator(float stepSize, int turnStepsMax, float wMax, float edgeMin, float edgeMax) {
		this(stepSize, turnStepsMax, wMax, wMax, edgeMin, edgeMin, edgeMax, edgeMax);
	}
	
	public TunnelGenerator(float stepSize, int turnStepsMax, float wMaxEasy, float wMaxHard, float edgeMinEasy, float edgeMinHard, float edgeMaxEasy, float edgeMaxHard) {
		this.vel = stepSize;
		this.turnStepsMax = turnStepsMax;
		this.wMaxEasy = wMaxEasy;
		this.wMaxHard = wMaxHard;
		this.edgeMinEasy = edgeMinEasy; 
		this.edgeMinHard = edgeMinHard;
		this.edgeMaxEasy = edgeMaxEasy;
		this.edgeMaxHard = edgeMaxHard;
		init();
	}
	
	public void init() {
		lastStep = new TunnelStep(new Vector2(-5,0), edgeMaxEasy, edgeMaxEasy);
		angVel = 0;
		topVel = 0; botVel = 0;
		steps = 25; // number of steps before turning
		topSteps = 14; botSteps = 16; // number of steps before changing wall size
	}
	
	public TunnelStep step() {
		float fTop = lastStep.fTop;
		float fBot = lastStep.fBot;
		float edgeMin = RandomCurves.difficulty(edgeMinEasy, edgeMinHard);
		float edgeMax = RandomCurves.difficulty(edgeMaxEasy, edgeMaxHard);
		
		if (steps-- <= 0) {
			steps = MathUtils.random(turnStepsMax);
			angVel= randAngVel();
		}
		if (topSteps-- <= 0) {
			topSteps = MathUtils.random(wallStepMax) + 1;
			topVel = (randEdgeOffset() - fTop) / topSteps;
		}
		if (botSteps-- <= 0) {
			botSteps = MathUtils.random(wallStepMax) + 1;
			botVel = (randEdgeOffset() - fBot) / botSteps;
		}
		
		fTop = MathUtils.clamp(fTop + topVel, edgeMin, edgeMax);
		fBot = MathUtils.clamp(fBot + botVel, edgeMin, edgeMax);
		
		Vector2 angle = lastStep.normal.cpy().rotate90(-1);
		angle.rotateRad(angVel);
		Vector2 nor = angle.cpy().rotate90(1);
		Vector2 mid = angle.scl(vel).add(lastStep.mid);
		
		lastStep = new TunnelStep(mid, fTop, fBot, nor);
		return lastStep;
	}
	
	float randEdgeOffset() {
		return MathUtils.random(RandomCurves.difficulty(edgeMinEasy, edgeMinHard), 
				RandomCurves.difficulty(edgeMaxEasy, edgeMaxHard));
		
	}
	
	float randAngVel() {
		return (MathUtils.random(-1.0f,1.0f)) * RandomCurves.difficulty(wMaxEasy, wMaxHard);
		
	}
}
