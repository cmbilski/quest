package com.quest.movement;

public class MovementOption {

	public int x;
	public int y;
	public int movementLeft;
	private MovementOption previous;
	private MovementOption next;
	
	public MovementOption(int x, int y, MovementOption prev, int movementLeft) {
		this.x = x;
		this.y = y;
		this.previous = prev;
		this.movementLeft = movementLeft;
	}

	public MovementOption(int x, int y) {
		this.x = x;
		this.y = y;
		this.movementLeft = -1;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public MovementOption getPrevious() {
		return previous;
	}
	public MovementOption getNext() {
		return next;
	}

	public void setNext(MovementOption next) {
		this.next = next;
	}

    public void setPrevious(MovementOption prev) {
        this.previous = prev;
    }

	public int getMovementLeft() {
		return movementLeft;
	}

	public boolean equals(Object o) {
		if (!(o instanceof MovementOption)) {
			return false;
		}

		MovementOption move = (MovementOption) o;
		return (move.x == this.x && move.y == this.y);
	}
	
	public String toString() {
		return String.format("Option %d %d", x, y);
	}
	
}
