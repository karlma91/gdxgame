package no.wafflewings.gdxgame;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;

public class CollisionSystem extends EntitySystem {
	@Mapper
	ComponentMapper<Position> pm;
	@Mapper
	ComponentMapper<Velocity> vm;
	@Mapper
	ComponentMapper<Circle> ci;

	@SuppressWarnings("unchecked")
	public CollisionSystem() {
		super(Aspect.getAspectForAll(Position.class, Velocity.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			Position p = pm.get(e);
			Velocity v = vm.get(e);
			Circle c = ci.get(e);
			if (p.p.y < Gdxgame.bot + c.getr()) {
				p.p.y = Gdxgame.bot + c.getr();
				v.getVel().y *= -1;
			}
			if (p.p.y > Gdxgame.top - c.getr()) {
				p.p.y = Gdxgame.top - c.getr();
				v.getVel().y *= -1;
			}
			if (p.p.x < Gdxgame.left + c.getr()) {
				p.p.x = Gdxgame.left + c.getr();
				v.getVel().x *= -1;
			}
			if (p.p.x > Gdxgame.right - c.getr()) {
				p.p.x = Gdxgame.right - c.getr();
				v.getVel().x *= -1;
			}
		}
		for (int i = 0; i < entities.size(); i++) {
			Entity a = entities.get(i);
			Position p1 = pm.get(a);
			Velocity v1 = vm.get(a);
			Circle c1 = ci.get(a);
			for (int j = i + 1; j < entities.size(); j++) {
				Entity b = entities.get(j);
				Position p2 = pm.get(b);
				Velocity v2 = vm.get(b);
				Circle c2 = ci.get(b);
				collision(p1, p2, v1, v2, c1, c2);
			}
		}
	}

	private void collision(Position p1, Position p2, Velocity v1, Velocity v2, Circle c1, Circle c2) {
		Vector2 un = new Vector2(p2.p);
		un.sub(p1.p);
		float l = un.len();
		if (l < c2.r + c1.r) {
			un.nor();
			Vector2 ut = new Vector2(un);
			ut.rotate90(1);
			
			float ol = (c2.r + c1.r) - l; 
			
			float v1n = un.dot(v1.v);
			float v2n = un.dot(v2.v);
			float nv1n = (v1n*(c1.m - c2.m) + 2*c2.m*v2n)/(c1.m+c2.m);
			float nv2n = (v2n*(c2.m - c1.m) + 2*c1.m*v1n)/(c1.m+c2.m);
			
			float utdv = ut.dot(v1.v);
			float utdbv = ut.dot(v2.v);
			v1.v.set(un.x*nv1n + ut.x*utdv, un.y*nv1n + ut.y*utdv);
			v2.v.set(un.x*nv2n + ut.x*utdbv, un.y*nv2n + ut.y*utdbv);
			p1.p.add(un.x*-ol, un.y*ol);
			p2.p.add(un.scl(ol));
		}
	}
	@Override
	protected boolean checkProcessing() {
		return true;
	}
}
