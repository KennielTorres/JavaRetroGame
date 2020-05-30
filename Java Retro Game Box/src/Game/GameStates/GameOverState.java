package Game.GameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Display.UI.UIManager;
import Main.Handler;
import Resources.Images;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class GameOverState extends State {

    private UIManager uiManager;

    public GameOverState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);
      
    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
        
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.gameOver,0,0,handler.getWidth(),handler.getHeight(),null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString("High Score: " + handler.getScoreManager().getPacmanHighScore(), handler.getWidth()/2, 125);
        uiManager.Render(g);
    }

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}
