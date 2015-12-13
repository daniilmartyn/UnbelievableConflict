package NetworkClasses;

public class SetXY {

	public int id;
	
	public float x, y; // x,y for player location. 
	
	public float x1, y1; // x1, y1 for weapon location
	
	public boolean jumped;
	
	public int health, priAmmo, secAmmo, kills, deaths;
	
	public int run, state;	// run is running direction. state is run/stand/crouch...
	
	public float rotate;
	
	public int weapon;
	
	public int wepDirection;
	
	public  boolean hit, hitisStopped;

	public boolean fired, hurt, kaboom;

	public boolean justjumped;
}
