package Game.Zelda.Entities.Dynamic;

import Game.GameStates.Zelda.ZeldaMMGameState;
import Game.GameStates.Zelda.ZeldaMapMakerState;
import Game.Zelda.Entities.BaseEntity;
import Game.Zelda.Entities.MMBaseEntity;
import Game.Zelda.Entities.Statics.MMNewTeleport;
import Game.Zelda.Entities.Statics.MMSolidStaticEntities;
import Game.Zelda.Entities.Statics.MMTeleport;
import Game.Zelda.Entities.Statics.MMWalkingSolidEntities;
import Game.Zelda.Entities.Statics.SolidStaticEntities;
import Game.Zelda.World.Map;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Game.Zelda.Entities.Dynamic.Direction.DOWN;
import static Game.Zelda.Entities.Dynamic.Direction.UP;

/**
 * Created by AlexVR on 3/15/2020
 */
public class MMLink extends MMBaseMovingEntity {


	private final int animSpeed = 120;
	public Map map;
	private boolean traveling;

	public MMLink(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		speed = 4;
		width = sprite[0].getWidth() * ZeldaMapMakerState.scale;
		height = sprite[0].getHeight()* ZeldaMapMakerState.scale;
		BufferedImage[] animList = new BufferedImage[2];
		animList[0] = sprite[4];
		animList[1] = sprite[5];

		animation = new Animation(animSpeed,animList);
	}

	@Override
	public void tick() {
		if (handler.getKeyManager().up && !traveling) {
			if (direction != UP) {
				BufferedImage[] animList = new BufferedImage[2];
				animList[0] = sprites[4];
				animList[1] = sprites[5];
				animation = new Animation(animSpeed, animList);
				direction = UP;
				sprite = sprites[4];
			}
			animation.tick();
			move(direction);

		} else if (handler.getKeyManager().down && !traveling) {
			if (direction != DOWN) {
				BufferedImage[] animList = new BufferedImage[2];
				animList[0] = sprites[0];
				animList[1] = sprites[1];
				animation = new Animation(animSpeed, animList);
				direction = DOWN;
				sprite = sprites[0];
			}
			animation.tick();
			move(direction);
		} else if (handler.getKeyManager().left && !traveling) {
			if (direction != Direction.LEFT) {
				BufferedImage[] animList = new BufferedImage[2];
				animList[0] = Images.flipHorizontal(sprites[2]);
				animList[1] = Images.flipHorizontal(sprites[3]);
				animation = new Animation(animSpeed, animList);
				direction = Direction.LEFT;
				sprite = Images.flipHorizontal(sprites[3]);
			}
			animation.tick();
			move(direction);
		} else if (handler.getKeyManager().right && !traveling) {
			if (direction != Direction.RIGHT) {
				BufferedImage[] animList = new BufferedImage[2];
				animList[0] = (sprites[2]);
				animList[1] = (sprites[3]);
				animation = new Animation(animSpeed, animList);
				direction = Direction.RIGHT;
				sprite = (sprites[3]);
			}
			animation.tick();
			move(direction);
		} else {
			moving = false;
		}
		
		teleport();
	}

	@Override
	public void render(Graphics g) {
		if (moving) {
			g.drawImage(animation.getCurrentFrame(),x , y, width  , height   , null);
		} else {
			g.drawImage(sprite, x , y, width , height, null);
		}
	}

	@Override
	public void move(Direction direction) {
		moving = true;
		changeIntersectingBounds();
		//check for collisions
		for (MMBaseEntity solidStaticEntities:map.getBlocksOnMap()){
			if (solidStaticEntities instanceof MMSolidStaticEntities && solidStaticEntities.bounds.intersects(interactBounds)){
				return;
			}
		}
		switch (direction) {
		case RIGHT:
			x += speed;
			map.xOffset-=speed;
			break;
		case LEFT:
			x -= speed;
			map.xOffset+=speed;
			break;
		case UP:
			y -= speed;
			map.yOffset+=speed;
			break;
		case DOWN:
			y += speed;
			map.yOffset-=speed;
			break;
		}
		bounds.x = x;
		bounds.y = y;
		changeIntersectingBounds();

	}
	private void teleport() { // Recursive teleport travel tile
		for(MMBaseEntity block:map.getBlocksOnMap()) {
			if(block instanceof MMSolidStaticEntities && block.bounds.intersects(this.bounds)) { // Hit solid
				traveling=false;
				return;
			}
			if(block instanceof MMNewTeleport && block.bounds.intersects(this.bounds)) { // Teleport
				if(block.sprite.equals(Images.zeldaNewTeleporter.get(0))) { // Right
					this.y=block.y;
					bounds.y = y;
					changeIntersectingBounds();
					traveling=true;
					move(Direction.RIGHT);
					traveling=false;
					teleport();
				}
				else if(block.sprite.equals(Images.zeldaNewTeleporter.get(1))) { // Left
					this.y=block.y;
					bounds.y = y;
					changeIntersectingBounds();
					traveling=true;
					move(Direction.LEFT);
					traveling=false;
					teleport();
				}
				else if(block.sprite.equals(Images.zeldaNewTeleporter.get(2))) { // Up
					this.x=block.x;
					bounds.x = x;
					changeIntersectingBounds();
					traveling=true;
					move(Direction.UP);
					traveling=false;
					teleport();
				}
				else if(block.sprite.equals(Images.zeldaNewTeleporter.get(3))) { // Down
					this.x=block.x;
					bounds.x = x;
					changeIntersectingBounds();
					traveling=true;
					move(Direction.DOWN);
					traveling=false;
					teleport();
				}
			}
		}
	}


}
