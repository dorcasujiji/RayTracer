package raytracer;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Raytracer Class - Creates window and displays scene
 * according to description file provided by user 
 */
public class Raytracer extends JPanel {
    /** Maximum number of times single ray is bounced reflected/refracted */
	private int maxRayDepth; 
	
	/** Width of window and viewport */
	private int viewWidth;
	
	/** Height of window and viewport */
	private int viewHeight;
	
	/** Scene's background color */
	private Vector backgroundColor;
	
	/** Scene's field of view */
	double fov;
	
	/** List of objects in scene */
	private Object[] objects;
	
	/** List of colors for each pixel to be displayed to draw scene */
	private Vector[] image;
	
	/** 
	 * Creates raytracer object with dimensions and background color
	 *  from parameters. Also sets up window to display scene
	 */
	public Raytracer(int width, int height, int maxRayDepth, double fov, Vector backgroundColor, Object[] sceneObj) {
	    this.maxRayDepth = maxRayDepth;
	    this.fov = fov;
	    this.viewWidth = width;
	    this.viewHeight = height;
	    this.backgroundColor = backgroundColor;
	    this.objects = sceneObj;
	    this.image = new Vector[viewWidth * viewHeight];
	    // set up window
		JFrame frame = new JFrame("Ray Tracer");
		frame.setBounds(0, 0, viewWidth, viewHeight);
		frame.add(this);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// draw scene
		drawScene();
	}
	
	/* 
	 * Overrides JPanel paint method
	 * paints each pixel from raytraced calculated color 
	 * */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		for(int pixel = 0; pixel < image.length; pixel++) {
		    if(image[pixel] != null) {
		        g2d.setColor(image[pixel].toColor());
	            g2d.fillRect(pixel%viewWidth, pixel/viewWidth, 1, 1);
		    }
		}
	}
	
	/** 
	 * Calculates ratio of fresnel effect based incidence ratio 
	 */
	private static double fresnelEffect(double a, double b, double mix) {
		return b* mix + a * (1 - mix);
	}
	
	/** 
	 * @return color of transparent and/or reflective surfaces 
	 */
	private Vector getTransRefSurfColor(Object object, Vector surfaceCol, 
	        Vector pointHit, Vector normalHit, Vector rayDir, 
	        int depth, double bias, boolean insideObj) 
	{
	    // calculate incidence ratio and fresnel ratio
	    double incidenceAngleRatio = rayDir.negative().dot(normalHit);
        double fresneleffect = fresnelEffect(Math.pow((1-incidenceAngleRatio), 3), 1, 0.1);
        // get direction of reflected ray
        Vector reflectDir = rayDir.subtract(normalHit.scalarMult(2).scalarMult(rayDir.dot(normalHit)));
        reflectDir.normalize();
        // reflect ray 
        Vector reflection = trace(new Ray(pointHit.add(normalHit).scalarMult(bias), reflectDir), depth+1);
        Vector refraction = new Vector(0);
        
        // if object is transparent
        if (object.getTransparency() > 0) {
            double refrac = object.getIdxOfRefrac();
            double indRefrac = (insideObj) ? refrac : 1 / refrac; // are we inside or outside the surface? 
            
            double cosInd = normalHit.negative().dot(rayDir);
            double k = 1 - indRefrac * indRefrac * (1 - cosInd * cosInd); 
            Vector refractDir =  rayDir
                    .scalarMult(indRefrac)
                    .add(normalHit.scalarMult((indRefrac*cosInd - Math.sqrt(k))));
            
            refractDir.normalize(); 
            refraction = trace(new Ray(pointHit.subtract(normalHit.scalarMult(bias)), refractDir), depth+1);
        }
        surfaceCol = (reflection.scalarMult(fresneleffect).scalarMult(object.getReflectivity())
                .add(
                        refraction.scalarMult(1-fresneleffect).scalarMult(object.getTransparency())
                        )
                ).elemProduct(object.getSurfaceColor());
        
        // return final color
        return surfaceCol;
	}

	/** 
	 * @return color of diffuse surface or non-diffuse surface when 
	 * maximum ray depth is reached  
	 */
	private Vector traceLightRay(Object object, Vector surfaceCol, Vector pointHit, Vector normalHit, double bias) {
	    for(int i = 0; i < objects.length; i++) {
	        
	        // if object is a light source
            if(objects[i].getEmissionColor().magnitude() > 0 ) { 
                Vector transmission = new Vector(1);
                Vector lightDirection = objects[i].getNormal(pointHit);
                for(int j =0; j < objects.length; j++) {
                    //any object not current light source
                    if(i != j) { 
                        
                        // if another object obstructs light source; form shadow
                        if(objects[j].intersect(new Ray(pointHit.add(normalHit.scalarMult(bias)), lightDirection))) {
                            transmission = new Vector(0); 
                            break;
                        }
                    }
                }
                surfaceCol.add(
                            object.getSurfaceColor().elemProduct(transmission)
                            .scalarMult(Math.max(0, normalHit.dot(lightDirection)))
                            .elemProduct(objects[i].getEmissionColor())
                            );
            }
        }
	    // return final color
	    return surfaceCol;
	}
	
	
	/**
	 * @return color of pixel on object surface hit by ray
	 */
	private Vector trace(Ray ray, int depth) {
	    Vector rayDir = ray.getDirection();
	    double tnear = Double.MAX_VALUE;
	    Object object = null;
	    
	    // find nearest object hit by ray
        for(int i=0; i< objects.length; i++) {
            if(objects[i].intersect(ray)) {
                if(ray.getTNear() < tnear) {
                    tnear = ray.getTNear();
                    object = objects[i];
                }
            }
        }
        
        // if ray does not intersect with any object
        if(object == null) { 
            return backgroundColor; 
        }
        
        // initialize color of pixel to black
        Vector surfaceCol = new Vector(0); 
        Vector pointHit = ray.getOrigin().add(rayDir.scalarMult(tnear));
        Vector normalHit = object.getNormal(pointHit);
        
        // add some bias to the point from which we will be tracing 
        double bias = 1e-4; 
        boolean insideObj = false; 
        
        // true if normal and ray face same direction, inside object
        if(rayDir.dot(normalHit) > 0) {    
            normalHit = normalHit.negative(); // normal should be reversed
            insideObj = true;
        }
        
        // if object is transparent or reflective
        if((object.getTransparency() > 0 || object.getReflectivity() > 0) && depth < maxRayDepth) {
            surfaceCol = getTransRefSurfColor(object, surfaceCol, pointHit, normalHit, rayDir, depth, bias, insideObj);
        }
        else { // object is opaque and diffuse or maximum ray tracing reached 
            surfaceCol = traceLightRay(object, surfaceCol, pointHit, normalHit, bias);
        } 
        return surfaceCol.add(object.getEmissionColor());
	}
	
	/**
	 * @return color to be displayed in each pixel of viewport
	 * to display the scene on window  
	 */
	public void drawScene() {	
	    int aspectRatio = viewWidth/viewHeight;
	    int pixel = 0; // counter for pixel array
	    double invWidth = (double) 1/viewWidth;
	    double invHeight = (double) 1/viewHeight;
	    double tanFov = Math.tan(0.5 * Math.PI * fov/180);

	    // trace rays from each pixel on viewport
	    for(int y=0; y<viewHeight; y++) {
	        for(int x=0; x<viewWidth; x++, pixel++) {
	            // x-coordinate on viewport
	            double veiwX = (2 * ((x + 0.5) * invWidth) - 1) * tanFov * aspectRatio;
	            // y-coordinate on viewport
	            double veiwY = (1 - 2 * ((y + 0.5) * invHeight)) * tanFov;
	            Vector rayDir = new Vector(veiwX, veiwY, -1); 
	            rayDir.normalize(); 
	            image[pixel] = trace(new Ray(new Vector(0), rayDir), 0); 
	        }
	    }
	} 
} 

