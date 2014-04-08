package no.wafflewings.gdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

//import com.badlogic.gdx.scenes.scene2d.actions.*;

public class MainMenu implements Screen {

    Button play, sound;

    TextureRegion title_region = Gdxgame.atlas.findRegion("title_liten");

    Image title = new Image(title_region);
    float glblscale = 1;
    float titlew;
    float titleh;
    
    // Table hs_btn = new Table();

    Label scoretext, curr_score;

    public static boolean sound_on = true;

    private Stage stage;

    public MainMenu() {
        stage = new Stage();
        
        title.setWidth(400);
        title.setHeight(68);
        titlew = title.getWidth();
        titleh = title.getHeight();
        
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Adding widgets to the table

        play = make_button("playknapp_opp_liten", "playknapp_ned_liten", 140.0f, 70.0f);
        sound = make_button("speaker_on_liten", "speaker_off_liten", 55.0f, 55.0f, true);
        scoretext = meake_label("BEST", Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4,
                Gdx.graphics.getHeight() / (3.0f/2));
        curr_score = meake_label("SCORE", Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() /4,
        		Gdx.graphics.getHeight() / 2 - scoretext.getHeight()*2);
        
        play.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ((RandomCurves) Gdxgame.randomCurves).restart();
                Gdxgame.mygame.setScreen(Gdxgame.randomCurves);
            }
        });
        
        sound.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound_on = !sound_on;
            }
        });
        // table.add(play);
        stage.addActor(title);
        // stage.addActor(hs_btn);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdxgame.randomCurves.render(delta);
        Gdxgame.font2.setScale(0.7f * glblscale);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        Table.drawDebug(stage); // This is optional, but enables debug lines for
                                // tables.
    }

    @Override
    public void resize(int width, int height) {
        glblscale = width/(1.0f*Gdxgame.NATIV_W);
        stage.setViewport(width, height, true);
        play.setWidth(140.0f * (Gdx.graphics.getWidth() / Gdxgame.NATIV_W));
        play.setHeight(70 * (Gdx.graphics.getWidth() / Gdxgame.NATIV_W));
        
        sound.setWidth( 55.0f * (Gdx.graphics.getWidth() / Gdxgame.NATIV_W));
        sound.setHeight( 55.0f * (Gdx.graphics.getWidth() / Gdxgame.NATIV_W));
        float pos_x;
        float pos_y;
        pos_x = Gdx.graphics.getWidth() / 2 + sound.getWidth() / 3 + play.getWidth();
        pos_y = Gdx.graphics.getHeight() * 4 / 7 - sound.getHeight()*(3.0f/2) - 10;
        sound.setPosition(pos_x, pos_y);
        pos_x = Gdx.graphics.getWidth() / 2 + play.getWidth() / 2;
        pos_y = height * 4 / 7 - play.getHeight() / 2;
        play.setPosition(pos_x, pos_y);
        title.setWidth(titlew * (Gdx.graphics.getWidth() / Gdxgame.NATIV_W));
        title.setHeight(titleh * (Gdx.graphics.getWidth() / Gdxgame.NATIV_W));
        title.setPosition(Gdx.graphics.getWidth() / 2 - title.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4);
        pos_x = Gdx.graphics.getWidth() / 8;
        pos_y =  Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 24;
        scoretext.setPosition(pos_x, pos_y);
        pos_x =   Gdx.graphics.getWidth() / 8;
        pos_y = Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 8;
        curr_score.setPosition(pos_x, pos_y);
        curr_score.setFontScale(glblscale * 0.5f);
        scoretext.setFontScale(glblscale * 0.5f);
    }

    @Override
    public void show() {
        Gdxgame.randomCurves.show();
        Gdx.input.setInputProcessor(stage);
        // ((RandomCurves) Gdxgame.randomCurves).restart();
        float pos_x, pos_y;

        // Title
        title.setPosition(Gdx.graphics.getWidth() / 2 - title.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4);

        // group for other buttons
        int padding = 20;

        // Play start position
        pos_x = Gdx.graphics.getWidth() + play.getWidth();
        pos_y = Gdx.graphics.getHeight() * 4 / 7 - play.getHeight() / 2;

        play.setPosition(pos_x, pos_y);
        play.act(0.00000001f);

        // Animation for the playbutton
        pos_x = Gdx.graphics.getWidth() / 2 + play.getWidth() / 2;

        MoveToAction move = new MoveToAction();
        move.setPosition(pos_x, pos_y);
        move.setDuration(0.4f);
        move.setInterpolation(Interpolation.exp5Out);

        // ----------------------------------------------------
        VisibleAction sound_vis = new VisibleAction();
        sound_vis.setVisible(false);

        pos_x = Gdx.graphics.getWidth() / 2 + sound.getWidth() / 2 + play.getWidth() - padding / 2;
        pos_y = Gdx.graphics.getHeight() * 4 / 7 - sound.getHeight() / 2;
        sound.setPosition(pos_x, pos_y);
        MoveToAction s_move_hs = new MoveToAction();
        s_move_hs.setPosition(pos_x, pos_y);
        s_move_hs.setDuration(0.4f);
        s_move_hs.setInterpolation(Interpolation.exp5Out);

        VisibleAction sound_vis2 = new VisibleAction();
        sound_vis2.setVisible(true);

        pos_x = Gdx.graphics.getWidth() / 2 + sound.getWidth() / 2 + play.getWidth() - padding / 2;
        pos_y = Gdx.graphics.getHeight() * 4 / 7 - sound.getHeight()*(3.0f/2) - 10;

        MoveToAction s_hs_btns = new MoveToAction();
        s_hs_btns.setPosition(pos_x, pos_y);
        s_hs_btns.setDuration(0.4f);
        s_hs_btns.setInterpolation(Interpolation.swingOut);
        // --------------------------------------------------------------------------------
        // label start
        scoretext.setPosition(-scoretext.getWidth(), Gdx.graphics.getHeight() / 2);
        MoveToAction scoremove = new MoveToAction();
        scoremove.setPosition(Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 24);
        scoremove.setDuration(0.4f);
        scoremove.setInterpolation(Interpolation.exp5Out);
        scoretext.addAction(scoremove);
        scoretext.setText(String.format("BEST %d", ((RandomCurves)Gdxgame.randomCurves).myHighScore));
        // ----------------------------------------
        // score label start
        curr_score.setPosition(-scoretext.getWidth(), Gdx.graphics.getHeight() / 2 - scoretext.getHeight());
        MoveToAction curr_move = new MoveToAction();
        curr_move.setPosition(Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 8);
        curr_move.setDuration(0.4f);
        curr_move.setInterpolation(Interpolation.exp5Out);
        curr_score.setText(String.format("SCORE %d", ((RandomCurves)Gdxgame.randomCurves).player.score));
        curr_score.addAction(curr_move);
        // ----------------------------------------
        
        SequenceAction smove2 = new SequenceAction(sound_vis, s_move_hs, sound_vis2, s_hs_btns);
        sound.addAction(smove2);
        stage.addActor(sound);
        
        play.addAction(move);
        stage.addActor(play);
        stage.addActor(scoretext);
        stage.addActor(curr_score);
    }

    private float max(float height, float height2) {
        if (height < height2)
            return height2;
        return height;
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Button make_button(String up_texture, float x, float y) {
        ButtonStyle style = new ButtonStyle();

        TextureRegion upRegion = Gdxgame.atlas.findRegion(up_texture);
        style.up = new TextureRegionDrawable(upRegion);

        Button b = new Button(style);
        b.setWidth(x);
        b.setHeight(y);
        return b;
    }

    public Button make_button(String up_texture, String down_texture, float x, float y) {
        ButtonStyle style = new ButtonStyle();

        TextureRegion upRegion = Gdxgame.atlas.findRegion(up_texture);
        TextureRegion downRegion = Gdxgame.atlas.findRegion(down_texture);

        style.up = new TextureRegionDrawable(upRegion);
        style.down = new TextureRegionDrawable(downRegion);

        Button b = new Button(style);
        b.setWidth(x);
        b.setHeight(y);

        return b;
    }

    // boolean chk_btn, just to not confuse this with the one over.
    public Button make_button(String up_texture, String chk_texture, float x, float y, boolean chk_btn) {
        ButtonStyle style = new ButtonStyle();

        TextureRegion upRegion = Gdxgame.atlas.findRegion(up_texture);
        TextureRegion chkRegion = Gdxgame.atlas.findRegion(chk_texture);

        style.up = new TextureRegionDrawable(upRegion);
        style.checked = new TextureRegionDrawable(chkRegion);

        Button b = new Button(style);
        b.setWidth(x);
        b.setHeight(y);

        return b;
    }

    private Label meake_label(String string, float x, float y) {
    	Gdxgame.font2.setScale(0.5f * glblscale);
        LabelStyle s = new LabelStyle(Gdxgame.font2, Color.WHITE);
        Label l = new Label(string, s);
        l.setPosition(x, y);
        return l;
    }

}
