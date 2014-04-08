package no.wafflewings.gdxgame;

import aurelienribon.tweenengine.Tween;
import box2dLight.PointLight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Gdxgame extends Game {
	static OrthographicCamera camera;
	
	static final float NATIV_W = 800;
	static final float NATIV_H = 450;
	
	static float bot,top,left,right;
	static float w,h;
	static Game mygame;
	static TextureAtlas atlas;
	public static Screen mainmenu;
	public static Screen bouncingBalls;
	public static Screen randomCurves;
	public static BitmapFont font;
	public static BitmapFont font2;
	public static TextureRegion ground;
	public static TextureRegion rocket;
	public static TextureRegion pickup;
	public static TextureRegion pickupBad;
	public static Sound pling;

	public void create () {
		Tween.setCombinedAttributesLimit(4);
		Tween.registerAccessor(Color.class, new ColorAccessor());
		Tween.registerAccessor(PointLight.class, new LightAccessor());
		atlas = new TextureAtlas(Gdx.files.internal("game.atlas"));
		pling = Gdx.audio.newSound(Gdx.files.internal("sounds/Pling.mp3"));
		ground = atlas.findRegion("ground");
		rocket = atlas.findRegion("player_liten");
		pickup = atlas.findRegion("light_liten");
		pickupBad = atlas.findRegion("light_liten");
		font = new BitmapFont(Gdx.files.internal("fonts/SIL.fnt"));
		font2 = new BitmapFont(Gdx.files.internal("fonts/font2.fnt"));
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		bot = - ((h/w)/2);
		top = ((h/w)/2);
		left = - (1.0f) / 2;
		right = (1.0f) / 2;
		camera = new OrthographicCamera(w/h, 1);
		mygame = this;
		mainmenu = new MainMenu();
		bouncingBalls = new BouncingBalls();
		randomCurves = new RandomCurves();
		setScreen(mainmenu);
	}
}
