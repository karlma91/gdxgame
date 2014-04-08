package no.wafflewings.gdxgame;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Position extends Component{
	Vector2 p;
	public Position(){
		this(0.0f, 0.0f);
	}
	public Position(float x, float y) {
		p = new Vector2(x, y);
	}
	public Position(Vector2 pos){
		this(pos.x, pos.y);
	}
	public void setPos(Vector2 pos) {
		p.set(pos);
	}
	public void add(Vector2 pos) {
		p.add(pos);
	}
	public void add(float x, float y) {
		p.add(x,y);
	}
	public Vector2 getPos() {
		return p;
	}
}
