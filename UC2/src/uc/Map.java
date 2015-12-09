package uc;


import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Map extends Entity{

	private Image map;
	
	public Map(final float x, final float y) {
		super(x,y);
		
		map = ResourceManager.getImage(UCGame.DESERT_RSC);
		//addImage(map);
		
		addShape(new ConvexPolygon(map.getWidth(), 50f), new Vector(map.getWidth()/2, map.getHeight() - 20f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(250f, 40f), new Vector(120f, 940f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(50f, 110f), new Vector(410f, map.getHeight()-100), Color.transparent, Color.white);
		
		addShape(new ConvexPolygon(200f, 44f), new Vector(485f, 790f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(95f, 35f), new Vector(170f, 640f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(70f, 23f), new Vector(705f, 650f), Color.transparent, Color.white);
		addShape(new ConvexPolygon(320f, 40f), new Vector(1020f, 585f), Color.transparent, Color.white);

	}

	public Image getImg(){
		return map;
	}
	
}
