package no.wafflewings.gdxgame;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Player {

    Body body;
    private final float INITMAXSPEED = 2.7f;
    float maxSpeed = INITMAXSPEED;
    Sprite spr;
    PointLight light;
    Color startc;
    
    public int score = 0;
    
    public Player(World world, RayHandler rayHandler) {
        body = addRocket(world);
        spr = new Sprite(Gdxgame.rocket);
        float ratio = spr.getWidth() / (float) (spr.getHeight());
        spr.setSize(0.5f * ratio, 0.5f);
        spr.setOrigin(spr.getWidth() / 2, spr.getHeight() / 2);
        startc = new Color(0.6f,0.7f,0.8f,1.0f);
        light = new PointLight(rayHandler, 500, startc, 5.0f, 0, 0);
        light.setSoft(true);
        light.setSoftnessLenght(0.1f);
    }

    private Body addRocket(World world) {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't
        // move we would set it to StaticBody
        bodyDef.type = BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(0, 0);

        // Create our body in the world using our body definition
        Body rocket = world.createBody(bodyDef);
        rocket.setLinearDamping(1f);
        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(0.08f);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.filter.categoryBits = 2;
		fixtureDef.filter.groupIndex = 2;
		fixtureDef.filter.maskBits = 2|1;
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        rocket.createFixture(fixtureDef);
        return rocket;
    }

    /*
     * Call begin before this...
     */
    public void render(SpriteBatch sprBatch) {
        spr.setPosition(body.getPosition().x - spr.getWidth() / 2, body.getPosition().y - spr.getHeight() / 2);
        Vector2 fdir = body.getLinearVelocity();
        spr.setRotation(fdir.angle() - 90);
        spr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        spr.draw(sprBatch);
        light.setPosition(getPos());
    }

    public Vector2 getPos() {
        return body.getPosition();
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public Vector2 getDir() {
        return body.getLinearVelocity().cpy();
    }
    public float getDirRad() {
        return body.getLinearVelocity().getAngleRad();
    }

    public float getAngle() {
        return body.getLinearVelocity().angle();
    }

    public void reset() {
        score = 0;
    	maxSpeed = INITMAXSPEED;
        body.setTransform(0, 0, 0);
        body.setLinearVelocity(Vector2.Zero);
        light.setColor(startc);
    }

    public void update(float delta, Vector2 forced) {
        float force = maxSpeed / 20;
        Vector2 d = forced.cpy();
        d.sub(getPos());
        Vector2 vdir = getDir().nor();
        if(d.dot(vdir) < 0) {
            float a = d.dot(vdir.rotate90(1));
            d.set(getDir().rotate((a > 0 ? 1 : -1) * 65 ));
        }
        d.nor().scl(force);
        body.applyForceToCenter(d, true);
        Vector2 vel = new Vector2(body.getLinearVelocity());
        float speed = vel.len();
        if (speed > maxSpeed) {
            vel.nor().scl(maxSpeed);
            body.setLinearVelocity(vel);
        }
    }

    public float getSpeed() {
        return body.getLinearVelocity().len();
    }
}
