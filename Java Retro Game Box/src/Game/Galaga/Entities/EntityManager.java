package Game.Galaga.Entities;

import java.awt.*;
import java.util.ArrayList;

import Main.Handler;
import Resources.Images;

/**
 * Created by AlexVR on 1/25/2020
 */
public class EntityManager {

	public ArrayList<BaseEntity> entities;
	public PlayerShip playerShip;
	public Handler handler;
	public ArrayList<BaseEntity> laserAttack = new ArrayList<>();

	public EntityManager(PlayerShip playerShip) {
		entities = new ArrayList<>();
		this.playerShip = playerShip;
	}

	public void tick(){
		playerShip.tick();
		ArrayList<BaseEntity> toRemove = new ArrayList<>();
		for (BaseEntity entity: entities){
			if (entity.remove){
				toRemove.add(entity);
				continue;
			}
			entity.tick();
			if (entity.bounds.intersects(playerShip.bounds)){
				playerShip.damage(entity);
			}
			if (entity.attacking) {
				laserAttack.add(entity); 
				entity.tick();

				continue;
			}
		}

		for (BaseEntity entity:laserAttack) {
			entities.add(entity); 
		}
		laserAttack.clear();
		for (BaseEntity toErase:toRemove){
			entities.remove(toErase);
		}


	}

	public void render(Graphics g){
		for (BaseEntity entity: entities){
			entity.render(g);
		}
		for(BaseEntity lasers: laserAttack) {
			lasers.render(g);
		}
		playerShip.render(g);

	}

}
