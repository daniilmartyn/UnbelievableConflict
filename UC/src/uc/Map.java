package uc;


import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Map extends Entity{

	public static Image map;
	
	public Map(final float x, final float y) {
		super(x,y);
		
		map = ResourceManager.getImage(UCGame.DESERT_RSC);
		
		//the following are floor tiles and side of map boundaries
		addShape(new ConvexPolygon(map.getWidth(), 50f, new Vector(map.getWidth()/2, map.getHeight() - 20f)), new Vector(map.getWidth()/2, map.getHeight() - 20f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(40f, (float)map.getHeight(), new Vector(0f, map.getHeight()/2)), new Vector(0f, map.getHeight()/2), Color.transparent, Color.white);
		addShape(new ConvexPolygon(40f, (float)map.getHeight(), new Vector(map.getWidth(), map.getHeight()/2)), new Vector(map.getWidth(), map.getHeight()/2), Color.transparent, Color.white);
	
		//these are the platforms found in the map
		addShape(new ConvexPolygon(250f, 40f, new Vector(120f, 940f)), new Vector(120f, 940f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(50f, 110f, new Vector(410f, map.getHeight()-100)), new Vector(410f, map.getHeight()-100), Color.transparent, Color.white);
		
		addShape(new ConvexPolygon(200f, 44f, new Vector(485f, 790f)), new Vector(485f, 790f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(95f, 35f, new Vector(170f, 640f)), new Vector(170f, 640f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(70f, 23f, new Vector(705f, 650f)), new Vector(705f, 650f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(320f, 40f, new Vector(1020f, 585f)), new Vector(1020f, 585f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(166f, 55f, new Vector(1679f, 935f)), new Vector(1679f, 935f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(266f, 28f, new Vector(1909f, 791f)), new Vector(1909f, 791f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(278f, 26f, new Vector(1487f, 413f)), new Vector(1487f, 413f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(190f, 42f, new Vector(1952f, 449f)), new Vector(1952f, 449f), Color.transparent, Color.white);	

		
		UCGame.items.add(new Item(1909f, 760f, 0));
		UCGame.items.add(new Item(1682f, 883f, 1));
		UCGame.items.add(new Item(1027f, 665f, 2));  
	}

	public Image getImg(){
		return map;
	}
	
}
