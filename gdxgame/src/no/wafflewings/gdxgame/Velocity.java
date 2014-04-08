package no.wafflewings.gdxgame;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Velocity extends Component{
	public Vector2 v;
	public Velocity(){
		this(0.0f, 0.0f);
	}
	public Velocity(float x, float y) {
		v = new Vector2(x, y);
	}
	public Velocity(Vector2 pos){
		this(pos.x, pos.y);
	}
	public void setVel(Vector2 pos) {
		v.set(pos);
	}
	public void add(Vector2 pos) {
		v.add(pos);
	}
	public Vector2 getVel() {
		return v;
	}
}
