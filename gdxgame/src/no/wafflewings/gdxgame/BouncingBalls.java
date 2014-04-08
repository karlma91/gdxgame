package no.wafflewings.gdxgame;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BouncingBalls implements Screen {

	World world;
	AVal aval;
	TweenManager manager;
	ShapeRenderer sr;
	public BouncingBalls() {
		world = new World();
		world.setSystem(new MovementSystem());
		world.setSystem(new RenderSystem());
		world.setSystem(new CollisionSystem());
		world.initialize();
		for(int i= 0; i<10; i++) {
			addEntityBall();
		}
		aval = new AVal();
		aval.a = 0;
		Tween.registerAccessor(AVal.class, new AValAccessor());
		manager = new TweenManager();
		sr = new ShapeRenderer();
	}
	
	private void addEntityBall() {
		Entity e = world.createEntity();
		Vector2 p = new Vector2(MathUtils.random(-0.4f, 0.4f),
								MathUtils.random(-0.4f, 0.4f));
		e.addComponent(new Position(p));
		float speed = MathUtils.random(0, 0.3f);
		float ang = MathUtils.random(0, MathUtils.PI*2);
		Vector2 v = new Vector2(speed,0);
		v.rotateRad(ang);
		e.addComponent(new Velocity(v));
		float m = MathUtils.random(0.001f, 0.01f);
		e.addComponent(new Circle((float)(Math.sqrt(m / Math.PI)), m));
		e.addToWorld();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(aval.a, aval.a, aval.a, 1);
		//Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdxgame.camera.update();
		world.setDelta(Gdx.graphics.getDeltaTime());
		world.process();
		manager.update(Gdx.graphics.getDeltaTime());
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdxgame.mygame.setScreen(Gdxgame.mainmenu);
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		/*aval.a = 0;
		Timeline.createSequence()
		.push(
			Tween.to(aval, 1, 1.0f)
			.target(0.5f)
			.repeatYoyo(2, 0.0f))
		.push(
			Tween.to(aval, 1, 1.0f)
			.target(1.0f))
		.repeatYoyo(Tween.INFINITY, 0.5f)
		.start(manager);*/
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}

class AVal {
	float a;
}
class AValAccessor implements TweenAccessor<AVal> {

	@Override
	public int getValues(AVal target, int tweenType, float[] returnValues) {
		returnValues[0] = target.a;
		return 1;
	}

	@Override
	public void setValues(AVal target, int tweenType, float[] newValues) {
		target.a = newValues[0];
	}
	
}
