package Game.GameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Display.UI.UIManager;
import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.Dot;
import Main.Handler;
import Resources.Images;

public class PacManState extends State {

    private UIManager uiManager;
    public String Mode = "Intro";
    public int startCooldown = 60*4;//seven seconds for the music to finish
    private boolean boost;
    private int boostCooldown;
    public ArrayList<BaseDynamic> enemRemove = new ArrayList<>(); //Lists for entities to remove
    public PacManState(Handler handler){
        super(handler);
        handler.setMap(MapBuilder.createMap(Images.map1, handler));
    }
    @Override
    public void tick() {
    	 if(boost) {
         	handler.getPacman().setSpeed(2);
         	boostCooldown--;
         }
         if(boostCooldown<=0) {
         	boost=false;
         	handler.getPacman().setSpeed(1);
         }
        if (Mode.equals("Stage")){
            if (startCooldown<=0) {
            	for (BaseStatic blocks : handler.getMap().getBlocksOnMap()) {
                    blocks.tick();
                }
                for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
                    entity.tick();
                }
                for(BaseDynamic enemy:handler.getMap().getEnemiesOnMap()) {
					if(enemy instanceof Ghost) {
						if(handler.getPacman().eatGhost && enemy.getBounds().intersects(handler.getPacman().getBounds())) {
							handler.getMusicHandler().playEffect("pacman_chomp.wav");
							enemRemove.add(enemy);
		            		handler.getScoreManager().addPacmanCurrentScore(500);
						}
					}
				}
                ArrayList<BaseStatic> toREmove = new ArrayList<>();
                for (BaseStatic blocks: handler.getMap().getBlocksOnMap()){ 
                    if (blocks instanceof Dot){
                        if (blocks.getBounds().intersects(handler.getPacman().getBounds()) && blocks.sprite.equals(Images.pacmanDots[1])){
                            handler.getMusicHandler().playEffect("pacman_chomp.wav");
                            toREmove.add(blocks);
                            handler.getScoreManager().addPacmanCurrentScore(10);
                        }
                        else if (blocks.getBounds().intersects(handler.getPacman().getBounds()) && (blocks.sprite.equals(Images.pacmanDots[3]) || blocks.sprite.equals(Images.pacmanDots[4]) || blocks.sprite.equals(Images.pacmanDots[5]))){
                            boost=true;
                            boostCooldown=60*2;
                        	handler.getMusicHandler().playEffect("pacman_chomp.wav");
                            toREmove.add(blocks);
                            handler.getScoreManager().addPacmanCurrentScore(120);
                        }
                    }else if (blocks instanceof BigDot){
                        if (blocks.getBounds().intersects(handler.getPacman().getBounds())){
                        	handler.getPacman().isEdible = true;
                            handler.getMusicHandler().playEffect("pacman_chomp.wav");
                            toREmove.add(blocks);
                            handler.getScoreManager().addPacmanCurrentScore(100);
                        }
                    }
                }
                for (BaseStatic removing: toREmove){
                    handler.getMap().getBlocksOnMap().remove(removing);
                }
                for(BaseDynamic remove: enemRemove) { //Removes dead enemies from list
                	handler.getMap().getEnemiesOnMap().remove(remove);
                }
            }else{
                 startCooldown--;
            }
        }else if (Mode.equals("Menu")){
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                Mode = "Stage";
                handler.getMusicHandler().playEffect("pacman_beginning.wav");
            }
        }else{
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                Mode = "Menu";
            }
        }
        

    }
    @Override
    public void render(Graphics g) {

        if (Mode.equals("Stage")){
            Graphics2D g2 = (Graphics2D) g.create();
            handler.getMap().drawMap(g2);
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
            g.drawString("Score: " + handler.getScoreManager().getPacmanCurrentScore(),(handler.getWidth()/2) + handler.getWidth()/6, 25);
            g.drawString("High-Score: " + handler.getScoreManager().getPacmanHighScore(),(handler.getWidth()/2) + handler.getWidth()/6, 75);
            g.drawString("Lives: " + handler.getPacman().getHealth(), (handler.getWidth()/2) + handler.getWidth()/6, 125);
            if(handler.getScoreManager().getPacmanCurrentScore() > handler.getScoreManager().getPacmanHighScore()) {
            	handler.getScoreManager().setPacmanHighScore(handler.getScoreManager().getPacmanCurrentScore());
            }
        }else if (Mode.equals("Menu")){
            g.drawImage(Images.start,0,0,handler.getWidth()/2,handler.getHeight(),null);
        }else{
            g.drawImage(Images.intro,0,0,handler.getWidth()/2,handler.getHeight(),null);

        }
    }

    @Override
    public void refresh() {
    	
    }


}