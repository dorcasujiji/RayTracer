package raytracer;

/**
 * Ray Class - represents ray with Vector point origin 
 * and Vector direction.  
 */
public class Ray {
	private Vector origin; 
	private Vector direction; 
	/** t-value at point of intersection with a surface */
	private double tnear; 
	
	/**
	 * Creates Ray with passed in origin and direction
	 */
	public Ray(Vector origin, Vector direction) {
		this.origin = origin;
		this.direction = direction;
	}
	
	/**
	 * @return Vector direction of ray @
	 */
	public Vector getDirection() {
		return direction;
	}
	
	/**
	 * @return Vector origin of ray 
	 */
	public Vector getOrigin() {
		return origin;
	}
	
	/**
     * Sets value tnear of ray
     */
    public void setTNear(double tnear) {
        this.tnear = tnear;
    }
    
    /** @return tnear of ray */
    public double getTNear() {
        return tnear;
    }
    
}
