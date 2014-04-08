package no.wafflewings.gdxgame;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;

public class MovementSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Velocity> vm;

	public MovementSystem() {
		super(Aspect.getAspectForAll(Position.class, Velocity.class));
	}

	@Override
	protected void process(Entity e) {
		// Get the components from the entity using component mappers.
		Position position = pm.get(e);
		Velocity velocity = vm.get(e);
		// Update the position.
		position.add(velocity.getVel().x * world.getDelta(),velocity.getVel().y * world.getDelta());
	}
}
