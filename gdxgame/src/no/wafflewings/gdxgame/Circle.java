package no.wafflewings.gdxgame;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Circle extends Component{
	public float r;
	public float m;
	public Circle(){
		this(1.0f, 1.0f);
	}
	public Circle(float r, float m) {
		this.r = r;
		this.m = m;
	}
	public void setr(float r) {
		this.r = r;
	}
	public void setm(float m) {
		this.m = m;
	}
	public float getr() {
		return r;
	}
	public float getm() {
		return m;
	}
	
	public float getArea(){
		return (float) (r*r*Math.PI);
	}
}
