package no.wafflewings.gdxgame;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Circle>   ci;
	private SpriteBatch batch;
	Texture tex;
	Sprite sp;
	
	@SuppressWarnings("unchecked")
	public RenderSystem() {
		super(Aspect.getAspectForAll(Position.class, Velocity.class));
		batch = new SpriteBatch();
		sp = Gdxgame.atlas.createSprite("ball");
	}

	@Override
	protected void begin() {
		batch.setProjectionMatrix(Gdxgame.camera.combined);
		batch.begin();
	}
	
	@Override
	protected void process(Entity e) {
		// Get the components from the entity using component mappers.
		Position p = pm.get(e);
		Circle c = ci.get(e);
		sp.setSize(c.getr()*2, c.getr()*2);
		sp.setOrigin(c.getr(), c.getr());
		sp.setPosition(p.getPos().x-c.r, p.getPos().y-c.r);
		sp.draw(batch);
	}
	@Override
	protected void end() {
		batch.end();
	}
}
