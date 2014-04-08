package no.wafflewings.gdxgame;

import java.util.LinkedList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;

public class RandomCurves implements Screen {

	public final int NUM_SEGMENTS = 50;
	
    public final int WAITING_TO_START = 0;
    public final int RUNNING =1;
    public final int GAME_OVER = 2;
    public final int MAINMENU = 3;
    public static float hard = 0.0f; // difficulty measure between [0,1]
    public final int LIGHTSHADOW = 1;
    public final int NOSHADOW = 2;
    public int state = 0;
    float time = 0;
    
    Player player;
    int totalSegments = 0;
    TunnelStep nextSegment = null;
    
    RayHandler rayHandler;
    TweenManager tweenManager;
    Stage stage;
    SpriteBatch sprBatch, fontBatch;
    PolygonSpriteBatch pb;
    ShapeRenderer sr;
    LinkedList<TunnelStep> positions;
    LinkedList<Vector2[]> toDraw;
    LinkedList<Vector2[]> toDrawTop;
    LinkedList<LightPickup> pickups;
    TunnelGenerator tg;
    World world;
    Box2DDebugRenderer debugRenderer;
    int restart = 0;
    Vector2 forced;
    Body endWall;
    public int myHighScore;
    
    Texture tex;

	String vertexShader = "attribute vec4 a_position;    \n"
			+ "attribute vec4 a_color;\n" + 
			"attribute vec2 a_texCoord0;\n"
			+ "uniform mat4 u_worldView;\n" + 
			"varying vec4 v_color;"
			+ "varying vec2 v_texCoords;" + 
			"void main()                  \n"
			+ "{                            \n"
			+ "   v_color = vec4(1, 1, 1, 1); \n"
			+ "   v_texCoords = a_texCoord0; \n"
			+ "   gl_Position =  u_worldView * a_position;  \n"
			+ "}                            \n";
	String fragmentShader = "#ifdef GL_ES\n" + 
			  "precision mediump float;\n"
			+ "#endif\n" 
			+ "varying vec4 v_color;\n"
			+ "varying vec2 v_texCoords;\n" 
			+ "uniform sampler2D u_texture;\n"
			+ "void main()                                  \n"
			+ "{                                            \n"
			+ "  gl_FragColor = texture2D(u_texture, v_texCoords);\n"
			+ "}";

	Wire wire;
	ShaderProgram shader;
	PointLight startlight;
    public RandomCurves() {
    	tweenManager = new TweenManager();
        wire = new Wire();
    	shader = new ShaderProgram(vertexShader, fragmentShader);
    	tex =  new Texture(Gdx.files.internal("fonts/ground.png"));
        stage = new Stage();
        sr = new ShapeRenderer();
        sprBatch = new SpriteBatch();
        fontBatch = new SpriteBatch();
        pickups = new LinkedList<LightPickup>();
        state = WAITING_TO_START;
        pb = new PolygonSpriteBatch();
        positions = new LinkedList<TunnelStep>();
        toDraw = new LinkedList<Vector2[]>();
        toDrawTop = new LinkedList<Vector2[]>();
        tg = new TunnelGenerator(0.4f, 8, 12*MathUtils.degreesToRadians, 0.7f, 1.1f);
        forced = new Vector2();
        world = new World(new Vector2(0, 0), true); 
        rayHandler = new RayHandler(world);
        Filter f = new Filter();
        f.categoryBits = 1;
        f.maskBits = 1;
        short bit = 1;
        PointLight.setContactFilter(bit,bit,bit);
        endWall = createEndWall();
        world.setContactListener(new ContactListener() {
            
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                
            }
            
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                
            }
            
            @Override
            public void endContact(Contact contact) {
                
            }
            
            @Override
            public void beginContact(Contact contact) {
                Fixture fa = contact.getFixtureA();
                Fixture fb = contact.getFixtureB();
                if(isFixturePickup(fa, fb)){
                	
                } else if(fa.getBody() == player.body){
                    Gdx.app.log("Player", "hitWall");
                }
                
                if(isFixturePickup(fb, fa)){
                	
                }else if(fb.getBody() == player.body){
                    Gdx.app.log("Player", "hitWall");
                    playSound(Gdxgame.pling);
                    Tween.to(player.light, LightAccessor.COLOR, 0.2f)
                    .target(0.8f, 0.3f,0.3f,1.0f).start(tweenManager);
                    restart = 1;
                }
            }
        });
        debugRenderer = new Box2DDebugRenderer();
        player = new Player(world, rayHandler);
        startlight = new PointLight(rayHandler, 500, new Color(0.5f,0.5f,0.8f,1.0f), 8.0f, -4, 0);
        
        TweenCallback flikker = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                if(type ==COMPLETE){
                    Tween.to(startlight, LightAccessor.DIST, (float) (0.2f * Math.random()))
                    .target(5 + MathUtils.random() * 2)
                    .ease(TweenEquations.easeInOutCubic)
                    .setCallback(this)
                    .start(tweenManager);
                }
            }
        };
        Tween.to(startlight, LightAccessor.DIST, 0.1f)
        .target(8.0f)
        .ease(TweenEquations.easeInOutCubic)
        .setCallback(flikker)
        .start(tweenManager);
       
        restart();
    }
    
    private boolean isFixturePickup(Fixture fb, Fixture fa) {
    	Object obj = fb.getBody().getUserData();
        if(obj != null && obj instanceof LightPickup && fa.getBody() == player.body) {
        	LightPickup l= (LightPickup) obj;
        	if(l.on) return true;
        	if(l.friendly){
        	    playSound(Gdxgame.pling);
        	    Tween.to(l.light, LightAccessor.DIST, 0.6f)
        	    .target(4f).ease(TweenEquations.easeOutBack).start(tweenManager);
        	    Tween.to(l.light, LightAccessor.COLOR, 1.1f)
        	    .target(0.8f, 0.8f, 0.8f, 1.0f).start(tweenManager);
        	    player.maxSpeed+=0.08f;
        	    player.score++;
        	} else {
        	    Gdxgame.pling.play(1.0f);
                Tween.to(l.light, LightAccessor.DIST, 0.6f)
                .target(15f).ease(TweenEquations.easeOutBack).start(tweenManager);
                Tween.to(l.light, LightAccessor.COLOR, 1.1f)
                .target(0.9f, 0.1f, 0.1f, 1.0f).start(tweenManager);
                //Vector2 lip = player.getDir().cpy().rotate90((int) Math.signum(MathUtils.random() - 0.5f)).nor().scl(8);
                //player.body.applyForceToCenter(lip,true);
                playSound(Gdxgame.pling);
                restart = 1;
        	}
        	return true;
        }else{
        	return false;
        }
    }
    
    private void renderBody(Body b, int index, Vector2[] pverts) {
    	Fixture f = b.getFixtureList().first();
    	PolygonShape ps = (PolygonShape) f.getShape();
    	float[] vert = new float[4*4];
		Vector2 v = new Vector2(); 
		Vector2 p1 = new Vector2(); 
		Vector2 p2 = new Vector2(); 
		
		for(int i = 0; i<ps.getVertexCount(); i++){
			ps.getVertex(i, v);
    		v.rotate(b.getAngle()* MathUtils.radiansToDegrees);
        	v.add(b.getPosition()); 
        	//vert[i*4] = v.x;
        	//vert[i*4+1] = v.y;
        	vert[i*4] = pverts[i].x + b.getPosition().x;
        	vert[i*4+1] = pverts[i].y + b.getPosition().y;
		}
		ps.getVertex(0, p1);
		ps.getVertex(1, p2);
		
		Vector2 ang = new Vector2(p2).sub(p1);
		float angle  = ang.angle();
		//Gdx.app.log("angle", String.format("%f", angle));
		vert[2] = 1;
		vert[3] = 0;
		vert[6] = 1;
		vert[7] = 1;
		vert[10] = 0;
		vert[11] = 1;
		vert[14] = 0;
		vert[15] = 0;
    	Mesh quad = new Mesh(true, 4, 0, 
                         new VertexAttribute(Usage.Position, 2, "a_position"), 
                         new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));
    	quad.setVertices(vert);
        GL20 gl = Gdx.graphics.getGL20();
        gl.glEnable(GL20.GL_TEXTURE_2D);
        gl.glActiveTexture(GL20.GL_TEXTURE0);
        gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
        gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
    	tex.bind();
    	shader.begin();
    	shader.setUniformMatrix("u_worldView", Gdxgame.camera.combined);
    	shader.setUniformi("u_texture", 0);
    	quad.render(shader, GL20.GL_TRIANGLE_STRIP);
    	shader.end();
    }
    

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tweenManager.update(delta);
        Gdxgame.camera.position.x = player.getX();
        Gdxgame.camera.position.y = player.getY();
        Gdxgame.camera.zoom = 4;
        Gdxgame.camera.update();
        sprBatch.setProjectionMatrix(Gdxgame.camera.combined);
        sr.setProjectionMatrix(Gdxgame.camera.combined);
        debugRenderer.render(world, Gdxgame.camera.combined);

        //sprBatch.setProjectionMatrix(Gdxgame.camera.combined);
//        for(int i = 0; i<positions.size()-1; i++){
//        	Body b = positions.get(i).bBot;
//        	if (b != null) renderBody(b, i,toDraw.get(i));
//        }
//        for(int i = 0; i<positions.size()-1; i++){
//        	Body b = positions.get(i).bTop;
//        	if (b != null) renderBody(b, i,toDrawTop.get(i));
//        }
        
        if(player.getSpeed() > 0.1f){
            wire.addNewPointFromDir(player.getPos(), player.getDir());
        }
        wire.render(tex);
        
        rayHandler.setCombinedMatrix(Gdxgame.camera.combined);
        sprBatch.begin();
        for(LightPickup lp : pickups)
        	lp.render(delta, sprBatch);
        sprBatch.end();
        rayHandler.updateAndRender();
        
        fontBatch.begin();
        Gdxgame.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdxgame.font.setScale(1);;
        
        switch (state) {
        case WAITING_TO_START:
            break;
        case GAME_OVER:
            break;
        case RUNNING:
            Gdxgame.font2.setScale(0.5f);
            Gdxgame.font2.draw(fontBatch, String.format("%d", player.score), Gdxgame.w/2, Gdxgame.h-20);
            Gdxgame.font2.setScale(1f);
            break;
        case MAINMENU:
            
            break;
        default:
            break;
        }
        fontBatch.end();
        
        sprBatch.begin();
        player.render(sprBatch);
        sprBatch.end();
        update(delta);
    }
    
    
    public boolean isRight(Vector2 bot, Vector2 top, Vector2 p) {
    	return (top.x - bot.x)*(p.y - bot.y) < (top.y - bot.y)*(p.x - bot.x);
    }
    
    boolean positionChanged() {
    	if (isRight(nextSegment.bot, nextSegment.top, player.getPos())) {
    		totalSegments++;
    		if(totalSegments %15 == 0) {
    			TunnelStep last = positions.getLast();
    			Vector2 rand = new Vector2(last.top).lerp(last.bot, 0.1f + MathUtils.random()* 0.8f);
    			pickups.add(new LightPickup(rand, rayHandler, world, true));
    			
    		}else if(totalSegments % (20 - player.score / 3) == 0) {
    		    TunnelStep last = positions.getLast();
                Vector2 rand = new Vector2(last.top).lerp(last.bot, 0.1f + MathUtils.random()* 0.8f);
                pickups.add(new LightPickup(rand, rayHandler, world, false));
    		}
    		if(pickups.size()>10) {
                LightPickup toremove = pickups.remove(0);
                toremove.dispose();
            }
    		nextSegment = positions.get(NUM_SEGMENTS / 2 + 1);
    		//System.out.printf("total: %d\n", totalSegments);
    		return true;
    	}
    	return false;
    }
    
    public void update(float delta){
        time+=delta;
        Vector3 dir = new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);
        Gdxgame.camera.unproject(dir);
        forced.set(dir.x,dir.y);
        
        switch (state) {
        case WAITING_TO_START:
            if(Gdx.input.justTouched() && Gdx.input.getX() > Gdxgame.w / 2 && Gdxgame.mygame.getScreen() == this){
                state = RUNNING;
            }
            break;
        case GAME_OVER:
            if(Gdx.input.isTouched()){
            }
            if(Gdxgame.mygame.getScreen() == this) {
                Gdxgame.mygame.setScreen(Gdxgame.mainmenu);
            }
            break;
        case RUNNING:
        	//TODO change based on player position!
            while (positionChanged()) {
            	addSegment();
            	removeSegment();
            	updateEndWall();
                time = 0;
            }
            
            player.update(delta,forced);
            break;
        case MAINMENU:
            
            break;
        default:
            break;
        }
        
        world.step(1/60f, 6, 2);
        
        if(restart ==1) {
            if(player.score > myHighScore) {
                myHighScore = player.score;
            }
            state = GAME_OVER;
            //restart();
        }
        if(state != WAITING_TO_START){
        if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Gdxgame.mygame.setScreen(Gdxgame.mainmenu);
            state = WAITING_TO_START;
        }
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }
    private PolygonShape createShape(Vector2 a, Vector2 ca, Vector2 b, Vector2 cb, Vector2[] verts) {
        PolygonShape box = new PolygonShape();
        Vector2 normala = new Vector2(a).sub(ca).nor().scl(0.1f);
        Vector2 normalb = new Vector2(b).sub(cb).nor().scl(0.1f);;
        Vector2 mid = new Vector2(b).sub(a).scl(0.5f).add(a);
        verts[1] = (new Vector2(a).add(normala)).sub(mid);
        verts[0] = (new Vector2(a).sub(normala)).sub(mid);
        verts[2] = (new Vector2(b).add(normalb)).sub(mid);
        verts[3] = (new Vector2(b).sub(normalb)).sub(mid);
        box.set(verts);
        return box;
    }
    
    private Body addStatic(Vector2 a, Vector2 ca, Vector2 b, Vector2 cb, Vector2[] verts) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2());
        Body body = world.createBody(groundBodyDef);
        body.createFixture(createShape(a,ca,b,cb,verts), 0.0f);
        body.setTransform(b.cpy().sub(a).scl(0.5f).add(a), 0);
        return body;
    }
    
    private Body createEndWall() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2());
        Body body = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();  
        groundBox.setAsBox(0.1f, 2.0f);
        body.createFixture(groundBox, 0.0f);
        return body;
    }
    private void updateEndWall() {
        TunnelStep ts = positions.getFirst();
        endWall.setTransform(ts.mid, ts.normal.getAngleRad() - MathUtils.PI/2);
    }
    
    private void addSegment() {
		TunnelStep ts = tg.step();
		positions.add(ts);
		if (positions.size() == 1) return;
		TunnelStep tsPrev = positions.get(positions.size() - 2);

		Vector2[] vaTop = new Vector2[4];
		Vector2[] vaBot = new Vector2[4];

		ts.bTop = addStatic(ts.top, ts.mid, tsPrev.top, tsPrev.mid, vaTop);
		ts.bBot = addStatic(ts.bot, ts.mid, tsPrev.bot, tsPrev.mid, vaBot);

		toDrawTop.add(vaTop);
		toDraw.add(vaBot);
    }
    
    private void removeSegment() {
    	if (positions.size() == 0) return;
		TunnelStep ts = positions.pop();
		if (ts.bTop == null) return; 
		world.destroyBody(ts.bTop);
		world.destroyBody(ts.bBot);
		toDraw.pop();
		toDrawTop.pop();
    }
    
	public static float difficulty(float easyVal, float hardVal) {
		return easyVal * (1 - hard) - hardVal * hard;
	}
    
	private void initStartRoom(){
	    
	    float top,bot;
	    for( int i = 0; i<25; i++) {
	        top = 1;
	        bot = 1;
	        TunnelStep ts = new TunnelStep(new Vector2(-5,0), top , bot);
	        positions.add(ts);
	        if (positions.size() == 1) continue;
	        TunnelStep tsPrev = positions.get(positions.size() - 2);

	        Vector2[] vaTop = new Vector2[4];
	        Vector2[] vaBot = new Vector2[4];

	        ts.bTop = addStatic(ts.top, ts.mid, tsPrev.top, tsPrev.mid, vaTop);
	        ts.bBot = addStatic(ts.bot, ts.mid, tsPrev.bot, tsPrev.mid, vaBot);

	        toDrawTop.add(vaTop);
	        toDraw.add(vaBot);
	    }
	}
	
    public void restart() {
    	restart = 0;
    	time = 0;
    	state = WAITING_TO_START;
		tg.init();
		wire.reset();
		player.reset();
		for(LightPickup lp : pickups) {
			lp.dispose();
		}
		pickups.clear();
		int n = NUM_SEGMENTS;
		while (positions.size() > 0) removeSegment();
		
		//initStartRoom();
		//n = 24;
		while (n-- > 0) addSegment();
		updateEndWall();
		totalSegments = 0;
		nextSegment = positions.get(NUM_SEGMENTS / 2 + 1);
    }
    
    @Override
    public void show() {
    	if(state != RUNNING) {
    		
    	}
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0, 0, 0, 0);
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
    
    public static void playSound(Sound s){
        if(MainMenu.sound_on){
            s.play(0.4f);
        }
    }
    
}
