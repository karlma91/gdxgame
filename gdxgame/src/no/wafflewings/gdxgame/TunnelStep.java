package no.wafflewings.gdxgame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class TunnelStep {
	public final Vector2 top;
	public final Vector2 mid;
	public final Vector2 bot;
	public final Vector2 normal;
	final float fTop, fBot;
	Body bTop, bBot;
	
	public TunnelStep(Vector2 mid, float topFactor, float botFactor) {
		this(mid, topFactor, botFactor, new Vector2(0,1));
	}
	
	public TunnelStep(Vector2 mid, float topFactor, float botFactor, Vector2 normal) {
		this.mid = mid;
		top = normal.cpy().scl(+topFactor).add(mid);
		bot = normal.cpy().scl(-botFactor).add(mid);
		this.normal = normal.cpy();
		fTop = topFactor;
		fBot = botFactor;
	}
}
