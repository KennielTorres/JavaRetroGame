package Game.PacMan.World;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.GhostSpawner;
import Main.Handler;
import Resources.Images;

public class Map {

    ArrayList<BaseStatic> blocksOnMap;
    ArrayList<BaseDynamic> enemiesOnMap;
    Handler handler;
    private Random rand;

    public Map(Handler handler) {
        this.handler=handler;
        this.rand = new Random();
        this.blocksOnMap = new ArrayList<>();
        this.enemiesOnMap = new ArrayList<>();
    }

    public void addBlock(BaseStatic block){
        blocksOnMap.add(block);
    }

    public void addEnemy(BaseDynamic entity){

        enemiesOnMap.add(entity);

    }
    
    public void drawMap(Graphics2D g2) {
        for (BaseStatic block:blocksOnMap) {
            g2.drawImage(block.sprite, block.x, block.y, block.width, block.height, null);
            if(block instanceof BigDot) {
            	g2.drawImage(handler.getPacman().dotBlinkAnim.getCurrentFrame(), block.x, block.y, block.width, block.height, null);
            }
        }
        for (BaseDynamic entity:enemiesOnMap) {
            if (entity instanceof PacMan) {
                switch (((PacMan) entity).facing){
                    case "Right":
                        g2.drawImage(((PacMan) entity).rightAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Left":
                        g2.drawImage(((PacMan) entity).leftAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Up":
                        g2.drawImage(((PacMan) entity).upAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Down":
                        g2.drawImage(((PacMan) entity).downAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                }
            }
            else if(entity instanceof Ghost) {
            	g2.drawImage(entity.sprite, entity.x, entity.y, entity.width, entity.height, null);
            	if(handler.getPacman().eatGhost) {
            		g2.drawImage(handler.getPacman().eatGhostAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
            	}
            }
            else {
                g2.drawImage(entity.sprite, entity.x, entity.y, entity.width, entity.height, null);
            }
        }

    }

    public ArrayList<BaseStatic> getBlocksOnMap() {
        return blocksOnMap;
    }

    public ArrayList<BaseDynamic> getEnemiesOnMap() {
        return enemiesOnMap;
    }

    public void reset() {
    }
}