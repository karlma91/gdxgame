package no.wafflewings.gdxgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightPickup {

	Sprite spr;
	Body collision;
	PointLight light;
	Color lcolor;
	World world;
	boolean on;
	public boolean friendly;

	public LightPickup(Vector2 p, RayHandler rayHandler, World world, boolean friendly ) {
	    this.friendly =  friendly;
		this.world = world;
		if(friendly) {
		    spr = new Sprite(Gdxgame.pickup);
		    lcolor = new Color(0.3f, 0.7f, 0.3f, 1);
		}else{
		    spr = new Sprite(Gdxgame.pickup);
		    lcolor = new Color(0.8f, 0.3f, 0.3f, 1);
		}
		float ratio = spr.getWidth() / (float) (spr.getHeight());
		spr.setSize(0.3f * ratio, 0.3f);
		spr.setOrigin(spr.getWidth() / 2, spr.getHeight() / 2);
		spr.setPosition(p.x - spr.getWidth() / 2, p.y - spr.getHeight() / 2);
		spr.setColor(0.3f, 0.5f, 0.3f, 1.0f);
		light = new PointLight(rayHandler, 30, lcolor, 0.3f, 0, 0);
		light.setPosition(p);
		collision = addCircle(world, p,friendly);
		on = false;
	}

	private Body addCircle(World world, Vector2 p, boolean friendly2) {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't
		// move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(p.x, p.y);

		// Create our body in the world using our body definition
		Body rocket = world.createBody(bodyDef);
		rocket.setUserData(this);
		rocket.setLinearDamping(1f);
		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(0.08f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		if(friendly2){
		fixtureDef.filter.categoryBits = 2;
		fixtureDef.filter.groupIndex = 2;
		fixtureDef.filter.maskBits = 2;
		}
		fixtureDef.isSensor = friendly2;
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		rocket.createFixture(fixtureDef);
		return rocket;
	}

	public void render(float delta, SpriteBatch sprBatch) {
	    spr.setPosition(collision.getPosition().x - spr.getWidth()/2, collision.getPosition().y - spr.getHeight()/2);
		spr.draw(sprBatch);
//		light.setColor(lcolor);
	}

	public void dispose() {
		light.remove();
		world.destroyBody(collision);
	}
}
