package Game.PacMan.entities.Statics;

import java.awt.event.KeyEvent;
import java.util.Random;

import Game.PacMan.World.Map;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Main.Handler;
import Resources.Images;

public class GhostSpawner extends BaseStatic {
	Random rand = new Random();
	Ghost ghost;
	boolean allowRespawn,numGen;
	int spawnCooldown,img;
	public GhostSpawner(int x, int y, int width, int height, Handler handler, Map map) {
		super(x, y, width, height, handler, null);
		for(int i=0;i<4;i++) {
			BaseDynamic ghost = new Ghost(x,y,width,height,handler,Images.ghost[i]);
			map.addEnemy(ghost);
		}
	}
	@Override
	public void tick(){
		if((handler.getMap()).getEnemiesOnMap().size()<5 && !allowRespawn) {
			numGen=true;
		}
		if(numGen) {
			spawnCooldown=rand.nextInt(60*4)+60;
			numGen=false;
			allowRespawn=true;
		}
		if(allowRespawn) {
			spawnCooldown--;
			if(spawnCooldown<=0) {
				img = rand.nextInt(4);
				BaseDynamic ghost = new Ghost(x,y,width,height,handler,Images.ghost[img]);
				(handler.getMap()).addEnemy(ghost);
				allowRespawn=false;
			}
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_C)) {
			img = rand.nextInt(4);
			BaseDynamic ghost = new Ghost(x,y,width,height,handler,Images.ghost[img]);
			(handler.getMap()).addEnemy(ghost);
		}
	}
}
