package Game.Zelda.Entities.Dynamic;

import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.Statics.DungeonDoor;
import Game.Zelda.Entities.Statics.SectionDoor;
import Game.Zelda.Entities.Statics.SolidStaticEntities;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static Game.Zelda.Entities.Dynamic.Direction.DOWN;

/**
 * Created by AlexVR on 3/15/2020
 */
public class Enemy extends BaseMovingEntity {


	private final int animSpeed = 480;
	int newMapX=0,newMapY=0,xExtraCounter=0,yExtraCounter=0, mult = 2, cooldown = 60;
	public boolean movingMap = false;
	Direction movingTo;
	int test=2;


	public Enemy(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		speed = 2;
		health = 3;
		BufferedImage[] animList = new BufferedImage[2];
		animList[0] = Images.bouncyEnemyFrames[0];
		animList[1] = Images.bouncyEnemyFrames[1];

		animation = new Animation(animSpeed,animList);
	}

	@Override
	public void tick() {

		animation.tick();

		if(cooldown<=0) {
			test = new Random().nextInt(4);
			cooldown = 60*1;
		}

		cooldown--;
		switch(test) {

		case 0: 
			direction = Direction.UP;
			move(direction);
			break;
		case 1:
			direction = Direction.DOWN;
			move(direction);
			break;
		case 2:
			direction = Direction.RIGHT;
			move(direction);
			break;
		case 3:
			direction = Direction.LEFT;
			move(direction);
			break;
		default:
			direction = Direction.RIGHT;
			move(direction);
			break;
		}




	}

	@Override
	public void render(Graphics g) {
		handler.getZeldaGameState();
		if(!(ZeldaGameState.ded)) {
			if (moving) {
				g.drawImage(animation.getCurrentFrame(),x , y, width , height  , null);

			}
			else {
				g.drawImage(sprite, x , y, width , height , null);
			}
		}
	}

	@Override
	public void move(Direction direction) {
		moving = true;
		changeIntersectingBounds();
		//chack for collisions
		if (ZeldaGameState.inCave){
			for (SolidStaticEntities objects : handler.getZeldaGameState().caveObjects) {
				if ((objects instanceof DungeonDoor) && objects.bounds.intersects(bounds) && direction == ((DungeonDoor) objects).direction) {
					if (((DungeonDoor) objects).name.equals("caveStartLeave")) {
						ZeldaGameState.inCave = false;
						x = ((DungeonDoor) objects).nLX;
						y = ((DungeonDoor) objects).nLY;
						direction = DOWN;
					}
				} else if (!(objects instanceof DungeonDoor) && objects.bounds.intersects(interactBounds)) {
					//dont move
					return;
				}
			}
		}
		else {
			for (SolidStaticEntities objects : handler.getZeldaGameState().objects.get(handler.getZeldaGameState().mapX).get(handler.getZeldaGameState().mapY)) {
				if ((objects instanceof SectionDoor) && objects.bounds.intersects(bounds) && direction == ((SectionDoor) objects).direction) {
					if (!(objects instanceof DungeonDoor)) {
						movingMap = true;
						movingTo = ((SectionDoor) objects).direction;
						//						switch (((SectionDoor) objects).direction) {
						//						case RIGHT:
						//							newMapX = -(((handler.getZeldaGameState().mapWidth) + 1) * worldScale);
						//							newMapY = 0;
						//							handler.getZeldaGameState().mapX++;
						//							xExtraCounter = 8 * worldScale + (2 * worldScale);
						//							break;
						//						case LEFT:
						//							newMapX = (((handler.getZeldaGameState().mapWidth) + 1) * worldScale);
						//							newMapY = 0;
						//							handler.getZeldaGameState().mapX--;
						//							xExtraCounter = 8 * worldScale + (2 * worldScale);
						//							break;
						//						case UP:
						//							newMapX = 0;
						//							newMapY = -(((handler.getZeldaGameState().mapHeight) + 1) * worldScale);
						//							handler.getZeldaGameState().mapY--;
						//							yExtraCounter = 8 * worldScale + (2 * worldScale);
						//							break;
						//						case DOWN:
						//							newMapX = 0;
						//							newMapY = (((handler.getZeldaGameState().mapHeight) + 1) * worldScale);
						//							handler.getZeldaGameState().mapY++;
						//							yExtraCounter = 8 * worldScale + (2 * worldScale);
						//							break;
						//						}
						return;
					}
					//					else {
					//						if (((DungeonDoor) objects).name.equals("caveStartEnter")) {
					//							ZeldaGameState.inCave = true;
					//							x = ((DungeonDoor) objects).nLX;
					//							y = ((DungeonDoor) objects).nLY;
					//							direction = UP;
					//						}
					//					}
				}
				else if (!(objects instanceof SectionDoor) && objects.bounds.intersects(interactBounds)) {
					//dont move
					return;
				}
			}
		}
		//overworld
		switch (direction) {
		case RIGHT:
			x += speed;
			break;
		case LEFT:
			x -= speed;

			break;
		case UP:
			y -= speed;
			break;
		case DOWN:
			y += speed;

			break;
		}
		bounds.x = x;
		bounds.y = y;
		changeIntersectingBounds();

	}
	public int getHealth() {
		return health;
	}
}
