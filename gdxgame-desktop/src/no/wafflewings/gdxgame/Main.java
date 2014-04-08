package no.wafflewings.gdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;


public class Main {
	public static void main(String[] args) {
		
        Settings settings = new Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
        //TexturePacker2.process(settings, "../gdxgame/textures", "../gdxgame-android/assets", "game");
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Lightbringer";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 450;
		
		new LwjglApplication(new Gdxgame(), cfg);
	}
}
