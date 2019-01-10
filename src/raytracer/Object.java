package raytracer;

/**
 * This is the parent class of scene objects 
 */
public class Object {
	private Vector color;
	protected Vector emissionColor;	
	protected Vector center; 

	private double transparency;
	private double reflectivity;
	private double idxOfRefrac;
	
	/**
	 * Creates an object with the properties of its parameters 
	 * */
	public Object(Vector color, Vector center, double transparency, double reflectivity, double idxOfRefrac) {
		this.color = color;
		this.center = center;
		this.transparency = transparency;
		this.reflectivity = reflectivity;
		this.idxOfRefrac = idxOfRefrac;
	}
	
	/** 
	 * Creates an object with the properties of its parameters 
	 * and @param transparency = 0; @param idxOfRefrac = 1
	 * */
	public Object(Vector color, Vector center, double reflectivity) {
		this(color, center, 0, reflectivity, 1);
	}
	
	/**
	 * @return true if @param ray intersects object
	 *  */
	public boolean intersect(Ray ray) { return true;}
	
	/**
	 * @return transparency of object
	 *  */
	public double getTransparency() {
		return transparency;
	}
	
	/* */
	public double getReflectivity() {
	    return reflectivity;
	}
	
	/** 
	 * @return color of object 
	 * */
	public Vector getSurfaceColor() {
		return color;
	}
	
	/** 
	 * @return refractive index of object 
	 * */ 
	public double getIdxOfRefrac() {
		return idxOfRefrac;
	}
	
	/** 
	 * @return center of object 
	 * */ 
	public Vector getCenter() {
		return center;
	}
	
	/** 
	 * @return emmission color of object 
	 * needed when object is light source 
	 * */ 
	public Vector getEmissionColor() {
	    return emissionColor;
	}
	
	/**
	 * @return normal of object at a point
	 * child classes override this method
	 *  */ 
	public Vector getNormal(Vector pointHit) {
	    return new Vector(0);
	}
	
	/** 
     * Overriden by plane class to return true
     * @return false
     */
	public boolean isPlane() {
	    return false;
	}
}

