import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;

// all object in the game must extend this class because it basis for such objects!
public abstract class GameObject {
	protected double x;
	protected double y;
	protected double x_pre;
	protected double y_pre;
	protected double direction;
	protected double vspeed = 0;
	protected double hspeed = 0;
	protected int id = 0;
	protected boolean destory = false;
	
	int resources = 0;
	
	// the ability variables
	protected final int maxAbilityTokens = 100;
	protected int ability_sight = 0;
	protected int ability_speed = 0;
	protected int ability_strength = 0;
	//protected int ability_
	
	
	public String debugString = "test";
	public ArrayList<GameObject> sightList = null;	
	
	// the type of call object
	protected String name = null;
	
	//default room size
	// needs to go away
	public int room_width = 640;
	public int room_height = 640;
	
	// default color
	public Color color = Color.BLUE;

	// keep track of what is the next id to be used
	private static int nextID = 0;
	
	// default constructor
	public GameObject(String name, int x, int y)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		direction = 0;
		this.sightList = new ArrayList<GameObject>();

		synchronized (this)
		{
			id = nextID;
			nextID++;
		}
	}
	
	protected final boolean abilitySet(int sight, int speed, int strength)
	{
		if (sight+speed+strength > maxAbilityTokens)
			return false;
			
		ability_sight = sight;
		ability_speed = speed;
		ability_strength = strength;
		
		return true;
	}
	
	// returns a list of objects around the agent
	// uses ability_sight as the sight distance but that might need to be
	// multiplied by a number like 2 to keep the simulation good
	// protected ArrayList<GameObject> see()
	// {
	// 	// crate a new list to store the agents that can be seen
	// 	ArrayList<GameObject> outList = new ArrayList<GameObject>();
	// 	// // with each agent
	// 	// for(GameObject item : ALL.controller.list)
	// 	// {
	// 	// 	// get the distance from self to other
	// 	// 	double dis = ALL.point_distance(x+16, y+16, item.getX()+16, item.getY()+16);
	// 	// 	// if not self and in range add to list
	// 	// 	if (dis  < (ability_sight) && id != item.getID()) 
	// 	// 	{
	// 	// 		outList.add(item);
	// 	// 	}
	// 	// }
	// 	return outList;
	// }

	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}

	public void setSightList(ArrayList<GameObject> sightList){
		this.sightList.clear();
		this.sightList.addAll(sightList);	
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}

	public int getID()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}

	public double getDirection()
	{
		return direction;
	}
	
	public boolean getDestory()
	{
		return destory;
	}

	//Sets the motion of the calling object to the given direction and speed
	public void motion_set(double direction, double speed)
	{
		if (speed > ability_speed)
			speed = ability_speed;
		vspeed = ALL.lengthdir_y(speed, direction);
		hspeed = ALL.lengthdir_x(speed, direction);
	}
	
	// the updates the need to happen the the teams should not mess with
	final private void mandatory_update()
	{
		x_pre = x;
		y_pre = y;
		x+=hspeed;
		y+=vspeed;
		// sightList = see();
		if (x != x_pre || y != y_pre)
			direction = ALL.point_direction(x_pre, y_pre, x, y);
	}

	/*
		During the execution of the game this functions will be called before
		the rendering of each frame, so, this object can be correctly update 
		in relation to the passed time since the last frame and/or any events 
		that may have happened.
	*/
	final public void update()
	{
		/*
			The current implementation tries to simplify any subclass by separating
			the intrinsic behavior of this function from the subclass specific behavior
		*/

		// this is what NEED to be done regardless of the subclass implementation
		mandatory_update();

		// this is the specific behavior of the subclass
		update_logic();
		
	}

	/* 
		This function defines, specific to a subclass, what should happen, so, 
		this game object is up to date considering the current time difference 
		between the last frame and the current one and/or any events that may 
		have occurred.
	*/
	public abstract void update_logic();

	/*
		This defines how a game object is draw, repecting the current values 
		of its variables.
	*/
	public abstract void draw(Graphics2D g);
}
