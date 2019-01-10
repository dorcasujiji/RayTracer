package raytracer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/** 
 * The Raytracer program implements an raytracing algorithms that displays
 * user specified spheres, planes and light sources on a window. 
 * The program accepts description of the scene to be displayed as a json file
 * 
 * @author Dorcas Ujiji
 * @version 1.0
 * @since 2018-12-15
 * */
public class Main {
    private static int viewWidth; 
    private static int viewHeight; 
    private static int maxRayDepth; 
    private static double fov = 75;
    private static Vector backgroundColor; 
    private static ArrayList<Object> sceneObj = new ArrayList<>();
    
    /* This method specifies scene dimensions, maximum ray depth and background color */
    private static void createScene(JSONObject sceneParam) {
        viewWidth = ((Number) sceneParam.get("width")).intValue();
        viewHeight = ((Number) sceneParam.get("height")).intValue();
        maxRayDepth = ((Number) sceneParam.get("max_ray_depth")).intValue();
        fov = ((Number) sceneParam.get("field_of_view")).doubleValue();
        JSONObject col = (JSONObject) sceneParam.get("background_color");
        backgroundColor = new Vector(getColorRatio(col.get("r")),
                getColorRatio(col.get("g")),
                getColorRatio(col.get("b"))
                );
    }
    
    /* This method adds a source of light to list of scene objects */
    private static void addLight(JSONObject lightObj) {
        JSONObject pos = (JSONObject) lightObj.get("center");
        Vector center =  new Vector(((Number) pos.get("x")).doubleValue(),
                ((Number) pos.get("y")).doubleValue(),
                ((Number) pos.get("z")).doubleValue()
                );
        double radius = ((Number) lightObj.get("radius")).doubleValue();
        
        JSONObject col = (JSONObject) lightObj.get("emission_color");
        Vector emmColor = new Vector(getColorRatio(col.get("r")),
                getColorRatio(col.get("g")),
                getColorRatio(col.get("b"))
                );
        Sphere light = new Sphere(center, radius, new Vector(0), 0, 0, 1, emmColor);
        
        // add light to scene
        sceneObj.add(light);
    }
    
    /* This method adds sphere to list of scene objects */
    private static void addSphere(JSONObject sphereObj) {
        JSONObject pos = (JSONObject) sphereObj.get("center");
        Vector center =  new Vector(((Number) pos.get("x")).doubleValue(),
                ((Number) pos.get("y")).doubleValue(),
                ((Number) pos.get("z")).doubleValue()
                );
        double radius = ((Number) sphereObj.get("radius")).doubleValue();
        JSONObject col = (JSONObject) sphereObj.get("color");
        Vector color = new Vector(getColorRatio(col.get("r")),
                getColorRatio(col.get("g")),
                getColorRatio(col.get("b"))
                );
        double transp = ((Number) sphereObj.get("transparency")).doubleValue();
        double reflec = ((Number) sphereObj.get("reflectivity")).doubleValue();
        double idxOfRefrac = ((Number) sphereObj.get("index_of_refraction")).doubleValue();
        Sphere sphere = new Sphere(center, radius, color, transp, reflec, idxOfRefrac);
        
        // add sphere to scene 
        sceneObj.add(sphere);
    }
    
    /* This method adds a plane to the list of scene objects */
    private static void addPlane(JSONObject planeObj) {
        JSONObject pos = (JSONObject) planeObj.get("center");
        Vector center =  new Vector(((Number) pos.get("x")).doubleValue(),
                ((Number) pos.get("y")).doubleValue(),
                ((Number) pos.get("z")).doubleValue()
                );
        JSONObject norm = (JSONObject) planeObj.get("normal");
        Vector normal =  new Vector(((Number) norm.get("x")).doubleValue(),
                ((Number) norm.get("y")).doubleValue(),
                ((Number) norm.get("z")).doubleValue()
                );
        double width = ((Number) planeObj.get("width")).doubleValue();
        double height = ((Number) planeObj.get("height")).doubleValue();
        JSONObject col = (JSONObject) planeObj.get("color");
        Vector color = new Vector(getColorRatio(col.get("r")),
                getColorRatio(col.get("g")),
                getColorRatio(col.get("b"))
                );
        double reflec = ((Number) planeObj.get("reflectivity")).doubleValue();
        Plane plane = new Plane(center, normal, width, height, color, reflec);
        
        // add plane to list of scene objects
        sceneObj.add(plane);
    }
    
    /* This method returns colors as ratio */
    private static double getColorRatio(java.lang.Object num) {
        double number = ((Number) num).doubleValue();
        if(number > 0) {
            return number/255;
        } else {
            return 0;
        }
    }
    
    /* This method gets file from user and creates scene objects */
    public static void main(String[] args) {
        String filePath = "";
        if(args.length == 1) {
            filePath = args[0];
        } else { // get file path via user input 
            Scanner userInput = new Scanner(System.in);
            System.out.println("Please enter absolute filepath below: ");
            filePath = userInput.nextLine();
            userInput.close();
        }
        
        // convert json file to list of scene objects
        JSONParser parser = new JSONParser();
        JSONArray sceneDescr = null;
        
        try {
            sceneDescr = (JSONArray) parser.parse(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        
        if(sceneDescr != null) {
            for(java.lang.Object sceneObj: sceneDescr) {
                JSONObject object = (JSONObject) sceneObj;
                
                if(object.get("type").equals("scene")) {
                    createScene(object);
                } else if (object.get("type").equals("light")) {
                    addLight(object);
                } else if (object.get("type").equals("sphere")) {
                    addSphere(object);
                } else if (object.get("type").equals("plane")) {
                    addPlane(object);
                } else {
                    System.err.println("Scene object type incorrectly specified.");;
                }
            } 
        } else {
            System.err.println("Please pass in non-empty scene description file.");
        }
        
        Object[] objects = new Object[sceneObj.size()];
        objects = sceneObj.toArray(objects); 
        
        // create a raytracer and repaint to ensure all pixels are displayed
        Raytracer raytracer = new Raytracer(viewWidth, viewHeight, maxRayDepth, fov, backgroundColor, objects);
        raytracer.repaint();
    }

}
