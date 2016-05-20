package com.quest.entity;

public class Entity {

	public int state;
	// Remember, these are ALWAYS in tiles
	private int x, y;
	protected int movementSpeed;
	
	public Entity() {
		state = 0;
		movementSpeed = 1;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getMovementSpeed() {
		return movementSpeed;
	}
	
}
