package no.wafflewings.gdxgame;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;

import aurelienribon.tweenengine.TweenAccessor;


	public class LightAccessor implements TweenAccessor<PointLight> {
        public static final int DIST = 1;
        public static final int COLOR = 2;

        @Override
        public int getValues(PointLight target, int tweenType, float[] returnValues) {
                switch (tweenType) {
                        case DIST:
                        	returnValues[0] = target.getDistance();
                        	return 1;
                        case COLOR: 
                        	returnValues[0] = target.getColor().r;
                        	returnValues[1] = target.getColor().g;
                        	returnValues[2] = target.getColor().b;
                        	returnValues[3] = target.getColor().a;
                        	return 4;
                        default: assert false; return -1;
                }
        }

        @Override
        public void setValues(PointLight target, int tweenType, float[] newValues) {
                switch (tweenType) {
                case DIST:
                	target.setDistance(newValues[0]);break;
                case COLOR: 
                	target.setColor(newValues[0], newValues[1], newValues[2], newValues[3]);
                	break;
                default: assert false;
                }
        }
}
