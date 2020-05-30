package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
/**
 * Created by AlexVR on 1/25/2020
 */
public class PlayerShip extends BaseEntity{

	private int health = 3,attackCooldown = 30,speed =6,destroyedCoolDown = 60*7;
	private boolean attacking = false, destroyed = false, moving = false;
	private Animation deathAnimation;
	public int[][]entitySpawns = new int[5][8];
	public int beeSpawnCD= random.nextInt(60*2)+60*2;
	public boolean allowBeeSpawn=true;
	public int eShipSpawnCD = random.nextInt(60*4)+60*2;
	public boolean allowEshipSpawn=true;
	public boolean initSpawn=true;
	public PlayerShip(int x, int y, int width, int height, BufferedImage sprite, Handler handler) {
		super(x, y, width, height, sprite, handler);

		deathAnimation = new Animation(256,Images.galagaPlayerDeath);

	}

	@Override
	public void tick() {
		super.tick();
		if (destroyed){
			if (destroyedCoolDown<=0){
				destroyedCoolDown=60*7;
				destroyed=false;
				deathAnimation.reset();
				bounds.x=x;
			}else{
				deathAnimation.tick();
				destroyedCoolDown--;
			}
		}else {
			if (attacking) {
				if (attackCooldown <= 0) {
					attacking = false;
				} else {
					attackCooldown--;
				}
			}
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) && !attacking) {
				handler.getMusicHandler().playEffect("laser.wav");
				attackCooldown = 30;
				attacking = true;
				handler.getGalagaState().entityManager.entities.add(new PlayerLaser(this.x + (width / 2), this.y - 3, width / 5, height / 2, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));

			}
			if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N) && !destroyed) { //Debug - N player dies
				destroyed = true;
				deathAnimation.tick();
				destroyedCoolDown --;
				handler.getMusicHandler().playEffect("explosion.wav");
				health --;

			}
			if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_L) && health <3) { //Debug - Press L to add a Life when not full
				health ++;
			}
			if(handler.getGalagaState().Mode.equals("Stage")) { 
				if(initSpawn) { // Spawns entities when launched
					int randR = new Random().nextInt(2)+3;//Spawns Bees when launched
					int randC = new Random().nextInt(8);
					for(int r=3;r<5;r++) {
						for(int c=0;c<8;c++) {
							if(entitySpawns[randR][randC]!=1) {
								entitySpawns[randR][randC]=1;
								handler.getGalagaState().entityManager.entities.add(new EnemyBee(0,0, 32, 32, handler,randR, randC));
								return;
							}
							else if(entitySpawns[randR][randC]==1) {
								for(int ro=3;ro<5;ro++) {
									for(int co=0;co<8;co++) {
										if(entitySpawns[ro][co]!=1) {
											entitySpawns[ro][co]=1;
											handler.getGalagaState().entityManager.entities.add(new EnemyBee(0,0, 32, 32, handler,ro, co));
											return;
										}
									}
								}
							}
							else{
								continue;
							}
						}
					}
					int enemR = new Random().nextInt(2)+1; //Spawns EnemyShips on launch
					int enemC = new Random().nextInt(4)+2;
					for(int eR=1;eR<3;eR++) {
						for(int eC=2;eC<6;eC++) {
							if(entitySpawns[enemR][enemC]!=1) {
								entitySpawns[enemR][enemC]=1;
								handler.getGalagaState().entityManager.entities.add(new EnemyShip(0, 0 , 32, 32, handler,enemR, enemC));
								return;
							}
							else if(entitySpawns[enemR][enemC]==1) {
								for(int eRo=1;eRo<3;eRo++) {
									for(int eCo=2;eCo<6;eCo++) {
										if(entitySpawns[eRo][eCo]!=1) {
											entitySpawns[eRo][eCo]=1;
											handler.getGalagaState().entityManager.entities.add(new EnemyShip(0, 0 , 32, 32, handler, eRo, eCo));
											return;
										}
									}
								}
							}
							else{
								continue;
							}
						}
					}
					initSpawn=false; //Stops initial spawn after all positions are filled
				}
				if(allowBeeSpawn){ //Activates flag which allows bees to spawn after one is killed
					beeSpawnCD--;
					if(beeSpawnCD<=0){
						int randR = new Random().nextInt(2)+3;
						int randC = new Random().nextInt(8);
						for(int r=3;r<5;r++) {
							for(int c=0;c<8;c++) {
								if(entitySpawns[randR][randC]!=1) {
									entitySpawns[randR][randC]=1;
									handler.getGalagaState().entityManager.entities.add(new EnemyBee(0,0, 32, 32, handler,randR, randC));
									beeSpawnCD=random.nextInt(60*2)+60*2;
									return;
								}
								else if(entitySpawns[randR][randC]==1) {
									for(int ro=3;ro<5;ro++) {
										for(int co=0;co<8;co++) {
											if(entitySpawns[ro][co]!=1) {
												entitySpawns[ro][co]=1;
												handler.getGalagaState().entityManager.entities.add(new EnemyBee(0,0, 32, 32, handler,ro, co));
												beeSpawnCD=random.nextInt(60*2)+60*2;
												return;
											}
										}
									}
								}
								else{
									continue;
								}
							}
						}
					}
				}
				if(allowEshipSpawn){ //Activates flag which allows EnemyShips to spawn after one is killed
					eShipSpawnCD--;
					if(eShipSpawnCD<=0){
						int enemR = new Random().nextInt(2)+1;
						int enemC = new Random().nextInt(4)+2;
						for(int eR=1;eR<3;eR++) {
							for(int eC=2;eC<6;eC++) {
								if(entitySpawns[enemR][enemC]!=1) {
									entitySpawns[enemR][enemC]=1;
									handler.getGalagaState().entityManager.entities.add(new EnemyShip(0, 0 , 32, 32, handler,enemR, enemC));
									eShipSpawnCD=random.nextInt(60*4)+60*2;
									return;
								}
								else if(entitySpawns[enemR][enemC]==1) {
									for(int eRo=1;eRo<3;eRo++) {
										for(int eCo=2;eCo<6;eCo++) {
											if(entitySpawns[eRo][eCo]!=1) {
												entitySpawns[eRo][eCo]=1;
												handler.getGalagaState().entityManager.entities.add(new EnemyShip(0, 0 , 32, 32, handler, eRo, eCo));
												eShipSpawnCD=random.nextInt(60*4)+60*2;
												return;
											}
										}
									}
								}
								else{
									continue;
								}
							}
						}
					}
				}
			}
			if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)) { //Debug - SpawnBee
				int randR = new Random().nextInt(2)+3;
				int randC = new Random().nextInt(8);
				if(randR>1 && randC>-1) {
					for(int r=3;r<5;r++) {
						for(int c=0;c<8;c++) {
							if(entitySpawns[randR][randC]!=1) {
								entitySpawns[randR][randC]=1;
								handler.getGalagaState().entityManager.entities.add(new EnemyBee(0, 0 , 32, 32, handler,randR, randC));
								return;
							}
							else if(entitySpawns[randR][randC]==1) {
								for(int ro=3;ro<5;ro++) {
									for(int co=0;co<8;co++) {
										if(entitySpawns[ro][co]!=1) {
											entitySpawns[ro][co]=1;
											handler.getGalagaState().entityManager.entities.add(new EnemyBee(0, 0 , 32, 32, handler,ro, co));
											return;
										}
									}
								}
							}
							else{
								continue;
							}
						}
					}
				}
			}
			if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_O)) { //Debug - Spawn Enemy Ship
				int enemR = new Random().nextInt(2)+1;
				int enemC = new Random().nextInt(4)+2;
				if(enemR>-1 && enemC>-1) {
					for(int eR=1;eR<3;eR++) {
						for(int eC=2;eC<6;eC++) {
							if(entitySpawns[enemR][enemC]!=1) {
								entitySpawns[enemR][enemC]=1;
								handler.getGalagaState().entityManager.entities.add(new EnemyShip(0, 0 , 32, 32, handler,enemR, enemC));
								return;
							}
							else if(entitySpawns[enemR][enemC]==1) {
								for(int eRo=1;eRo<3;eRo++) {
									for(int eCo=2;eCo<6;eCo++) {
										if(entitySpawns[eRo][eCo]!=1) {
											entitySpawns[eRo][eCo]=1;
											handler.getGalagaState().entityManager.entities.add(new EnemyShip(0, 0 , 32, 32, handler, eRo, eCo));
											return;
										}
									}
								}
							}
							else{
								continue;
							}
						}
					}
				}
			}
			if (handler.getKeyManager().left) {
				x -= (speed);
				if (x<handler.getWidth()/2-handler.getWidth()/4) { 
					x = (handler.getWidth()/2-handler.getWidth()/4); }
				bounds.x = x;
			}

		}
		if (handler.getKeyManager().right) { 
			x += (speed);
			if (x>handler.getWidth()/2+(handler.getWidth()/4)-65) {
				x = (handler.getWidth()/2 + (handler.getWidth()/4)-65);
			} bounds.x = x;

		}
	}





	private Random newRandom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render(Graphics g) {
		if (destroyed){
			if (deathAnimation.end){
				g.drawString("READY",handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2);
			}else {
				g.drawImage(deathAnimation.getCurrentFrame(), x, y, width, height, null);
			}
		}else{
			super.render(g);
		}
	}

	@Override
	public void damage(BaseEntity damageSource) {
		if (damageSource instanceof PlayerLaser){
			return;
		}
		health--;
		destroyed = true;
		handler.getMusicHandler().playEffect("explosion.wav");

		bounds.x = -10;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

}
