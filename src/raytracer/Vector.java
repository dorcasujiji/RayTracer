package raytracer;

import java.awt.Color;

/**
 * Vector Class 
 * Creates vector with x, y and z components
 * Vectors are also used to represent colors with r,g,b components each <= 1.0
 */
public class Vector {
	private double x, y, z;
	
	/** 
	 * Creates vector with x,y and z components all equal to the @param a
	 */
	public Vector(double a) {
	    this.x = a;
	    this.y = a;
	    this.z = a;
	}
	
	/** 
	 * Creates vector with x,y, and z components equal to the respective parameter
	 */
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/** @return x component of vector */
	public double getX() {
		return x;
	}
	
	/** @return y component of vector */
	public double getY() {
		return y;
	}
	
	/** @return z component of vector */
	public double getZ() {
		return z;
	}
	
	/**
	 * @return vector of vector subtraction of parameter from this vector 
	 */
	public Vector subtract(Vector a) {
		return new Vector(x-a.x, y-a.y, z-a.z);
	}
	
	/**
     * @return vector result of vector addition of parameter from this vector  
     */
	public Vector add(Vector a) {
		return new Vector(x+a.x, y+a.y, z+a.z);
	}
	
	/**
     * @return double result of dot product of parameter and vector  
     */
	public double dot(Vector a) {
		return (x*a.x + y*a.y + z*a.z);
	}
	
	/**
     * @return vector result of dot product of parameter and vector  
     */
	public Vector cross(Vector a) {
		return new Vector((y*a.z - z*a.y), (z*a.x-x*a.z), (x*a.y -y*a.x));
	}
	
	/**
     * @return vector result of scalar multiplication of parameter and vector  
     */
	public Vector scalarMult(double scalar) {
		return new Vector(x*scalar, y*scalar, z*scalar);
	}
	
	/**
     * @return double magnitude of the vector  
     */
	public double magnitude() {
		return Math.sqrt((x*x)+(y*y)+(z*z));
	}
	
	/**
     * Changes vector's components to normalize vector  
     */
	public void normalize() {
		double mag = magnitude();
		x = x/mag;
		y = y/mag;
		z = z/mag;
	}
	
	/**
     * @return vector that is negative of this vector  
     */
	public Vector negative() {
		return new Vector(-x, -y, -z);
	}
	
	/**
     * @return color equivalent of vector  
     */
	public Color toColor() {
	    return new Color((int)Math.min(255, x*255), (int)Math.min(255, y*255), (int)Math.min(255, z*255));
    }
	
	/**
     * @return vector result that is an element product of parameter and vector  
     */
	public Vector elemProduct(Vector a) {
	    return new Vector(x*a.x, y*a.y, z*a.z);
	}
	
	/**
     * @return String representation of vector   
     */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
