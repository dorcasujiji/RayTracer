package raytracer;

/**
 * Sphere class - child class of Object
 */
public class Sphere extends Object{

    /** Radius of sphere */
	private double radius;
	
	/**
	 * Creates sphere with properties respective to passed parameters 
	 */
	public Sphere(Vector center, double radius, Vector color, double transparency, double reflectivity, double idxOfRefrac, Vector emissionColor) {
		super(color, center, transparency, reflectivity, idxOfRefrac);
		this.radius = radius;
		this.emissionColor = emissionColor;
	}
	
	/**
     * Creates sphere with properties respective to passed parameters and black emission color 
     */
	public Sphere(Vector center, double radius, Vector color, double transparency, double reflectivity, double idxOfRefrac) {
        super(color, center, transparency, reflectivity, idxOfRefrac);
        this.radius = radius;
        this.emissionColor = new Vector(0);
    }
	
	/**
     * Creates sphere with properties respective to passed parameters 
     * and emission color = black, transparency = 0 and reflectivity = 0 
     * and index_of_refraction = 1
     */
	public Sphere(Vector center, double radius, Vector color) {
		super(color, center, 0, 1, 1);
		this.radius = radius;
		this.emissionColor = new Vector(0);
	}
	
	/** 
	 * Overrides intersect method of Object class
	 * Returns true if ray intersects surface of Sphere 
	 * @param ray
	 */
	@Override
	public boolean intersect(Ray ray ) {
	    Vector centerToRay = center.subtract(ray.getOrigin());
	    double tCenter = centerToRay.dot(ray.getDirection());
	    if(tCenter < 0)    
	        return false;
	    double d2 = centerToRay.dot(centerToRay) - tCenter*tCenter;
	    if (d2 > radius*radius)
	        return false;
	    double tDiff = Math.sqrt(radius*radius - d2);
	    double t0 = tCenter - tDiff;
	    double t1 = tCenter + tDiff;
	    double tnear = Double.MAX_VALUE;
	    if(t0 < 0)
            t0 = t1;
        if(t0 < tnear) 
            tnear = t0;
        ray.setTNear(tnear);
	    
	    return true;
	}
	
	/**
	 * Returns normal at point ray hits sphere's surface
	 */
	@Override
	public Vector getNormal(Vector pointHit) {
	    Vector normalAtHit = pointHit.subtract(center);
	    normalAtHit.normalize();
	    return normalAtHit;
	}
	
	@Override
	public boolean isPlane() { return false; }
	
}
