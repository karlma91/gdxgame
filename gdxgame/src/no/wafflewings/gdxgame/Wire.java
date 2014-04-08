package no.wafflewings.gdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Wire {
    
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
            + "  gl_FragColor = vec4(0.0, 0.2, 1.0, 1.0);\n"
            + "}";
    
    Mesh quad;
    static final int MAXFLOATS = 2*512;
    float[] tmpBox;
    float[] giantArray;
    private int index = 0;
    ShaderProgram shader;
    
    public float wireWidth = 0.06f;
    
    public Wire() {
        shader = new ShaderProgram(vertexShader, fragmentShader);
        tmpBox = new float[8];
        giantArray = new float[MAXFLOATS];
        quad = new Mesh(true, MAXFLOATS, 0, 
                new VertexAttribute(Usage.Position, 2, "a_position")); 
               // new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));
    }
    
    public void reset() {
        index = 0;
        for(int i = 0; i<MAXFLOATS; i++) {
            giantArray[i] = 0;
        }
      /* 
        Vector2[] v = new Vector2[10];
        float t = 0;
        for( int i = 0; i<10; i++) {
            v[i] = new Vector2(-5 + i*0.1f, MathUtils.random());
        }
        
        CatmullRomSpline<Vector2> cat = new CatmullRomSpline<Vector2>(v, false);
        Vector2 a = new Vector2(-5,0);
        */
        Vector2 start = new Vector2(0,0);
        for( float i = -5; i <= 0; i += 0.1f) {
            addNewPointFromDir(start.cpy(), Vector2.X.cpy());
            start.x = i;
        }
    }
    
    /*
     * add two points trianglestrip
     */
    public void addNewPoint(Vector2[] p) {
        for(int i = 0; i<2; i++) {
            tmpBox[i*2] = p[i].x;
            tmpBox[i*2+1] = p[i].y;
        }
        
        if(index == 0) {
            giantArray[0] = giantArray[MAXFLOATS - 8];
            giantArray[1] = giantArray[MAXFLOATS - 7];
            giantArray[2] = giantArray[MAXFLOATS - 6];
            giantArray[3] = giantArray[MAXFLOATS - 5];
            index = 4;
        }
        
      //  Gdx.app.log("index", String.format("%d", index));
        fillOffset(tmpBox,0, index, 4);
        index+=4;
        fillOffset(tmpBox, 2, index, 2);
        if (index < MAXFLOATS-4) {
            giantArray[index+2] = giantArray[index+4];
            giantArray[index+3] = giantArray[index+5];
        }
//        fillOffset(tmpBox, 2, index+2, 2);
        index = (index >= MAXFLOATS-4) ? 0 : index;
    }
    
    public void addNewPointFromDir(Vector2 pos, Vector2 dir) {
        Vector2[] vv = new Vector2[2];
        dir.rotate90(1).nor().scl(wireWidth);
        vv[0] = new Vector2(pos).add(dir);
        vv[1] = new Vector2(pos).sub(dir);
        addNewPoint(vv);
    }
    
    
    private void fillOffset(float[] a, int aoffset, int boffset, int count){
        for(int i = 0; i< count; i++) {
            giantArray[boffset + i] = a[aoffset + i];
        }
    }
    
    public void render(Texture tex) {
        GL20 gl = Gdx.graphics.getGL20();
        gl.glEnable(GL20.GL_TEXTURE_2D);
        gl.glActiveTexture(GL20.GL_TEXTURE0);
        gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
        gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
        tex.bind();
        quad.setVertices(giantArray);
        shader.begin();
        shader.setUniformMatrix("u_worldView", Gdxgame.camera.combined);
        //shader.setUniformi("u_texture", 0);
        quad.render(shader, GL20.GL_TRIANGLE_STRIP, 0 , MAXFLOATS-4);
        shader.end();
    }
}
