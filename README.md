# Raytracer 
A ray tracer program that displays a scene based on description of input file in JSON format.

## Before running: 
1. Add json.simple jar file to build path 

## Input Format: 
The raytracer program accepts filepath as command line arguments or as user input after running the program. 

The input file should be a JSON file with a list of scene objects and their descriptions. The file should have a single list of objects in square braces, separated by commas. The objects' parameters should be string (for object type), numbers (for dimension, color rgb-values or vector xyz-component) or objects themselves (vectors and colors). 


### How to create image with following "type": 
1. "scene" requires parameters: width, height (of window & viewport), max_ray_depth, field_of_view and background_color.

2. "light" objects are spherical with light rays and need parameters: center, radius and emmission_color (color of light rays). 

3. "sphere" objects require: center, radius, color, reflectivity (0>=r>=1), transparency (0>=t>=1), and index_of_refraction (recommeded 0.9 >= i >= 1.1). 

// Note: planes do not display shadows for now but are fine otherwise
4. "plane" objects require: center, normal (to the plane), width, height, color, normal, reflectivity (0>=r>=1).

Example: Acceptable file with light, sphere and plane
[
&nbsp;{
&nbsp;&nbsp;"type": "scene", 
&nbsp;&nbsp;"width": 1000,
&nbsp;&nbsp;"height": 800, 
&nbsp;&nbsp;"max_ray_depth": 20,
&nbsp;&nbsp;"field_of_view": 75,
&nbsp;&nbsp;"background_color": {"r":255, "g":255, "b":255}
&nbsp;}, 
&nbsp;{
&nbsp;&nbsp;"type": "light", 
&nbsp;&nbsp;"center": { "x":0, "y":4, "z":-13},
&nbsp;&nbsp;"radius": 2,
&nbsp;&nbsp;"emission_color": {"r":765, "g":765, "b":765}
&nbsp;}, 
&nbsp;{
&nbsp;&nbsp;"type": "sphere", 
&nbsp;&nbsp;"center": {"x":0, "y":0, "z":-10}, 
&nbsp;&nbsp;"radius": 1, 
&nbsp;&nbsp;"color": {"r":0, "g":255, "b":0}, 
&nbsp;&nbsp;"transparency": 0.5,
&nbsp;&nbsp;"reflectivity": 0.9, 
&nbsp;&nbsp;"index_of_refraction": 1.1
&nbsp;}, 
&nbsp;{
&nbsp;&nbsp;"type": "plane", 
&nbsp;&nbsp;"center": {"x":0, "y":-1, "z":-10}, 
&nbsp;&nbsp;"normal": {"x":0, "y":1, "z":0.1}, 
&nbsp;&nbsp;"width": 3, 
&nbsp;&nbsp;"height": 4,
&nbsp;&nbsp;"color": {"r":127, "g":0, "b":0}, 
&nbsp;&nbsp;"reflectivity": 0.9
&nbsp;}
]


### References: 
1. Blog at The Bottom of the Sea. Raytracing Reflection, Refraction, Fresnel, Total Internal Reflection, and Beer’s Law. Accessed on Dec 10th 2018. https://blog.demofox.org/2017/01/09/raytracing-reflection-refraction-fresnel-total-internal-reflection-and-beers-law/

2. math.stackexchange.com. Transforming point between euclidean coordinate systems. Accessed on Dec 14th 2018. https://math.stackexchange.com/questions/2306319/transforming-point-between-euclidean-coordinate-systems

3. math.stackexchange.com. Find Points on a Plane. Accessed on Dec 14th 2018. https://math.stackexchange.com/questions/2563909/find-points-on-a-plane

4. math.ucla.edu. Calculus Problems: Finding equation of plane through point with normal vector. Accessed Dec 14th 2018. http://www.math.ucla.edu/~ronmiech/Calculus_Problems/32A/chap11/section5/717d21/717_21.html

5. cs.unc.edu. Ray Tracing: Graphics for the Masses. Accessed on Dec 12th 2018. https://www.cs.unc.edu/~rademach/xroads-RT/RTarticle.html

6. Scratchapixel 2.0. Introduction to Ray Tracing: a Simple Method for Creating 3D Images. Accessed on Dec 10th 2018. https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-ray-tracing/ray-tracing-practical-example
