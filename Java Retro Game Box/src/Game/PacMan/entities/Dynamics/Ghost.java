package Game.PacMan.entities.Dynamics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Main.Handler;
import Resources.Images;

public class Ghost extends BaseDynamic{

	protected double velX,velY,speed = 1;
	public String facing = "Up";
	public boolean turnFlag,justSpawned,moving,randSide;
	public int turnCooldown = 10,side;
	Random rand = new Random();

	Ghost ghost = this;
	public int timer;
	public Ghost(int x, int y, int width, int height, Handler handler, BufferedImage sprite) {
		super(x, y, width, height, handler, sprite);
		justSpawned=true;
	}

	@Override
	public void tick(){
		switch (facing){
		case "Right":
			x+=velX;
			break;
		case "Left":
			x-=velX;
			break;
		case "Up":
			y-=velY;
			break;
		case "Down":
			y+=velY;
			break;
		}
		if (turnFlag){
			turnCooldown--;
		}
		if (turnCooldown<=0){
			turnFlag= false;
		}
		switch(side) {
		case 1:
			facing="Left";
			break;
		case 2:
			facing="Up";
			break;
		case 3:
			facing="Right";
			break;
		case 4:
			facing="Down";
			break;
		}

		if(randSide) {
			side = rand.nextInt(4)+1;
			randSide=false;
		}

		if(ghost.sprite.equals(Images.ghost[0]) || ghost.sprite.equals(Images.ghost[1])) {
			speed=2;
		}
		else {
			speed=1;
		}
		

		ArrayList<BaseStatic> bounds = handler.getMap().getBlocksOnMap();
		if(justSpawned) { //Spawn only, move out of cage
			velY=speed;
			y-=velY;
			for(BaseStatic bound:bounds) {
				boolean topCollision = ghost.getTopBounds().intersects(bound.getBottomBounds());
				if(bound instanceof BoundBlock) {
					if(topCollision) {
						velY=0;
						ghost.setY(bound.getY() + ghost.getDimension().height);
						justSpawned=false;
						facing="Left";
						moving=true;
						turnFlag=true;
					}
				}
			}
		}
		else if(moving) { //Continue movement
			velY=speed;
			velX=speed;
			if(facing.equals("Left")) { //Move left
				for(BaseStatic bound:bounds) {
					boolean leftCollision = ghost.getLeftBounds().intersects(bound.getRightBounds());
					if(bound instanceof BoundBlock) {
						if(!leftCollision) { //No collision?, continue
							facing="Left";
						}
						else { //Collision, move to another direction
							velX=0;
							ghost.setX(bound.getX() + ghost.getDimension().width);
							randSide=true;
						}
					}
				}
			}
			else if(facing.equals("Up")){ //Move up
				for(BaseStatic bound:bounds) {
					boolean topCollision = ghost.getTopBounds().intersects(bound.getBottomBounds());
					if(bound instanceof BoundBlock) {
						if(!topCollision) { //No collision?, continue
							facing="Up";
						}
						else { //Collision, move to another direction
							velY=0;
							ghost.setY(bound.getY() + ghost.getDimension().height);
							randSide=true;
						}
					}
				}
			}
			else if(facing.equals("Right")){ //Move right
				for(BaseStatic bound:bounds) {
					boolean rightCollision = ghost.getRightBounds().intersects(bound.getLeftBounds());
					if(bound instanceof BoundBlock) {
						if(!rightCollision) { //No collision?, continue
							facing="Right";
						}
						else { //Collision, move to another direction
							velX=0;
							ghost.setX(bound.getX() - ghost.getDimension().width);
							randSide=true;
						}
					}
				}
			}
			else if(facing.equals("Down")){ //Move down
				for(BaseStatic bound:bounds) {
					boolean downCollision = ghost.getBottomBounds().intersects(bound.getTopBounds());
					if(bound instanceof BoundBlock) {
						if(!downCollision) { //No collision?, continue
							facing="Down";
						}
						else { //Collision, move to another direction
							velY=0;
							ghost.setY(bound.getY() - ghost.getDimension().height);
							randSide=true;
						}
					}
				}
			}
		}
	}

	public double getVelX() {
		return velX;
	}
	public double getVelY() {
		return velY;
	}
}
