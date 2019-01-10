package raytracer;

/**
 * The Plane class, a subclass of the Object Class
 */
public class Plane extends Object{
	private Vector normal;
	// Vectors p1 and p2 are in direction of the plane-based coordinate syst. axis
	private Vector p1;  
	private Vector p2;
	private double width;
	private double height;
	
	/**
	 * Creates plane with the same properties as parameters past in
	 * and @param emission color = black
	 */
	public Plane(Vector center, Vector normal,  double width, double height, Vector color, double reflectivity) {
	    super(color, center, reflectivity);
	    emissionColor = new Vector(0); 
		this.normal = normal;
		this.center = center;
		this.width = width;
		this.height = height;
		p1 = calcP1();
		p1.normalize();
		p2 = normal.cross(p1);
		p2.normalize();
	}
	
	/** 
	 * Calculates vector in direction as plane-based coordinate system axis
	 */
	private Vector calcP1() {
		if (normal.getX() != 0) {
			return center.subtract(new Vector(center.getX() - normal.getY() / normal.getX(), center.getY() + 1, center.getZ()));
		}
		if (normal.getY() != 0) {
			return center.subtract(new Vector(center.getX() + 1, center.getY() - normal.getX() / normal.getY(), center.getZ()));
		}
		if (normal.getZ() != 0) {
			return center.subtract(new Vector(center.getX() + 1, center.getY(), center.getZ() - normal.getX() / normal.getZ()));
		}

		throw new RuntimeException("Error: Normal to plane cannot be zero vector.");
	}

	/**
	 * Overrides intersect method of Object class 
	 * @return true of ray intersects plane 
	 */
	@Override
	public boolean intersect(Ray ray) {
		Vector direction = ray.getDirection();

		double dotProduct = direction.dot(normal);
		if(dotProduct > 1e-6) { // ray is || to plane
		    return false;
		} else {
			// Find point where ray intersects plane
			double distance = (center.subtract(ray.getOrigin()).dot(normal)) / ray.getDirection().dot(normal);
			ray.setTNear(distance);
			Vector intersectPoint = ray.getDirection().scalarMult(distance).add(ray.getOrigin());
			
			// Transform from world- to plane-based coordinate systems 
			double newX = intersectPoint.subtract(center).dot(p1);
			double newY = intersectPoint.subtract(center).dot(p2);
			double newZ = intersectPoint.subtract(center).dot(normal);
			
			intersectPoint = new Vector(newX, newY, newZ);

			// Check if point is within rectangle
			return Math.abs(intersectPoint.getX()) < width / 2 && Math.abs(intersectPoint.getY()) < height / 2;
		}
	}
	
	/** 
	 * Overrides method in Object class
	 * @return normal of the plane, which is same at all points 
	 */
	@Override
	public Vector getNormal(Vector pointHit) {
	    return normal;
	}
	
	/** 
	 * Overrides method in Object class
	 * returns true
	 */
	@Override
	public boolean isPlane() {
	    return true;
	}
}
