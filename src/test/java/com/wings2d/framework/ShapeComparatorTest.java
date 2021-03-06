package com.wings2d.framework;


import static org.junit.jupiter.api.Assertions.*;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.wings2d.framework.shape.ShapeComparator;

class ShapeComparatorTest {
	@BeforeEach
	void setUp() throws Exception {
	}
	
	// similarShapes tests
	@Test
	void testSimilarSquares() {
		Rectangle2D one = new Rectangle2D.Double(0, 0, 10, 10);
		Rectangle2D two = new Rectangle2D.Double(0, 0, 10, 10);
		assertTrue(ShapeComparator.similarShapes(one, two));
	}
	@Test
	void testDifferentSquares() {
		Rectangle2D one = new Rectangle2D.Double(0, 0, 20, 20);
		Rectangle2D two = new Rectangle2D.Double(0, 0, 10, 10);
		assertFalse(ShapeComparator.similarShapes(one, two));
	}
	@Test
	void testTranslatedSquares() {
		Rectangle2D one = new Rectangle2D.Double(10, 5, 12, 12);
		Rectangle2D two = new Rectangle2D.Double(3, 45, 12, 12);
		assertTrue(ShapeComparator.similarShapes(one, two));
	}	
	@Test
	void testNegativeTranslatedSquares() {
		Rectangle2D one = new Rectangle2D.Double(-10, 5, 10, 10);
		Rectangle2D two = new Rectangle2D.Double(3, -45, 10, 10);
		assertTrue(ShapeComparator.similarShapes(one, two));
	}
	@Test
	void testRotatedSquares() {
		Shape one = new Rectangle2D.Double(0, 0, 10, 10);
		Shape two = new Rectangle2D.Double(0, 0, 10, 10);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(45));
		one = transform.createTransformedShape(one);
		assertTrue(ShapeComparator.similarShapes(one, two));
	}
	@Test
	void testRotatedAndTranslatedSquares() {
		Shape one = new Rectangle2D.Double(0, 0, 10, 10);
		Shape two = new Rectangle2D.Double(0, 0, 10, 10);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(45));
		transform.translate(10, -50);
		one = transform.createTransformedShape(one);
		transform = new AffineTransform();
		transform.rotate(Math.toRadians(-162));
		transform.translate(-481, 2);
		two = transform.createTransformedShape(two);
		assertTrue(ShapeComparator.similarShapes(one, two));
	}
	@Test
	void testDifferentAmountOfPoints() {
		Shape one = new Rectangle2D.Double(0, 0, 10, 10);
		Path2D two = new Path2D.Double();
		two.moveTo(23, 3);
		two.lineTo(10, 4);
		two.lineTo(63, 2);
		assertFalse(ShapeComparator.similarShapes(one, two));
	}
	@Test
	void testDifferentAmountOfPointsTwo() {
		Path2D one = new Path2D.Double();
		one.moveTo(23, 3);
		one.lineTo(10, 4);
		one.lineTo(63, 2);
		Shape two = new Rectangle2D.Double(0, 0, 10, 10);
		assertFalse(ShapeComparator.similarShapes(one, two));
	}
	@Test
	void testComplexPath() {
		Path2D one = new Path2D.Double();
		one.moveTo(36, 0);
		one.lineTo(45, 23);
		one.lineTo(73, -100);
		one.lineTo(-1000, -14140);
		one.lineTo(44455, 122324);
		one.lineTo(0, 0);
		one.lineTo(0, 0);
		one.lineTo(34, 6);
		Path2D two = new Path2D.Double();
		two.moveTo(36, 0);
		two.lineTo(45, 23);
		two.lineTo(73, -100);
		two.lineTo(-1000, -14140);
		two.lineTo(44455, 122324);
		two.lineTo(0, 0);
		two.lineTo(0, 0);
		two.lineTo(34, 6);
		assertTrue(ShapeComparator.similarShapes(one, two));
	}
	@Test
	void testComplexPathRotateAndTranslate() {
		Path2D one = new Path2D.Double();
		one.moveTo(36, 0);
		one.lineTo(45, 23);
		one.lineTo(73, -100);
		one.lineTo(-1000, -14140);
		one.lineTo(44455, 122324);
		one.lineTo(0, 0);
		one.lineTo(0, 0);
		one.lineTo(34, 6);
		Path2D two = new Path2D.Double();
		two.moveTo(36, 0);
		two.lineTo(45, 23);
		two.lineTo(73, -100);
		two.lineTo(-1000, -14140);
		two.lineTo(44455, 122324);
		two.lineTo(0, 0);
		two.lineTo(0, 0);
		two.lineTo(34, 6);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(45));
		transform.translate(-15, 555555);
		transform.rotate(71);
		transform.translate(45, -.001);
		one = (Path2D)transform.createTransformedShape(one);
		transform = new AffineTransform();
		transform.rotate(1000000000); // SPIN!
		transform.translate(34, -1);
		two = (Path2D)transform.createTransformedShape(two);
		assertTrue(ShapeComparator.similarShapes(one, two));
	}
	@Test
	void testPreciseShapes() { 
		Path2D one = new Path2D.Double();
		one.moveTo(-30.0, -30.0);
		one.lineTo(30.0, -30.0);
		one.lineTo(71.72995780590716, 78.05907172995781);
		one.lineTo(-51.68776371308019, 79.1139240506329);
		Path2D two = new Path2D.Double();
		two.moveTo(-24.14953415699774, 34.882660448996724);
		two.lineTo(-34.882660448996724, -24.14953415699774);
		two.lineTo(63.9685269512598, -84.53657838986543);
		two.lineTo(87.08399760473866, 36.70170646613579);
		assertTrue(ShapeComparator.similarShapes(one, two));
	}
	
	// getRotationFrom tests
	@Test
	void testGetRotationNoRotation() { 
		Shape one = new Rectangle2D.Double(0, 0, 10, 10);
		Shape two = new Rectangle2D.Double(0, 0, 10, 10);
		assertEquals(0, ShapeComparator.getRotationFrom(one, two, false));
	}
	@Test
	void testGetRotationRotateSecondObject() { 
		Shape one = new Rectangle2D.Double(0, 0, 10, 10);
		Shape two = new Rectangle2D.Double(0, 0, 10, 10);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(45));
		two = transform.createTransformedShape(two);
		assertEquals(45, ShapeComparator.getRotationFrom(one, two, false));
	}
	@Test
	void testGetRotationRotateFirstObject() { 
		Shape one = new Rectangle2D.Double(0, 0, 10, 10);
		Shape two = new Rectangle2D.Double(0, 0, 10, 10);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(35));
		one = transform.createTransformedShape(one);
		assertEquals(-35, ShapeComparator.getRotationFrom(one, two, false)); // Is negative because the result is the second Shape
	}
	@Test
	void testGetRotationRotateBothObjects() { 
		Shape one = new Rectangle2D.Double(0, 0, 10, 10);
		Shape two = new Rectangle2D.Double(0, 0, 10, 10);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(45));
		one = transform.createTransformedShape(one);
		transform = new AffineTransform();
		transform.rotate(Math.toRadians(-12));
		two = transform.createTransformedShape(two);
		assertEquals(-57, ShapeComparator.getRotationFrom(one, two, false));
	}
	@Test
	void testGetRotationComplexShapes() { 
		Path2D one = new Path2D.Double();
		one.moveTo(-30.0, -30.0);
		one.lineTo(30.0, -30.0);
		one.lineTo(71.72995780590716, 78.05907172995781);
		one.lineTo(-51.68776371308019, 79.1139240506329);
		Path2D two = new Path2D.Double();
		two.moveTo(-24.14953415699774, 34.882660448996724);
		two.lineTo(-34.882660448996724, -24.14953415699774);
		two.lineTo(63.9685269512598, -84.53657838986543);
		two.lineTo(87.08399760473866, 36.70170646613579);
		assertEquals(259.6952, ShapeComparator.getRotationFrom(one, two, false));
	}
	@Test
	void testGetRotationSimilarComplexShapes() { 
		Path2D one = new Path2D.Double();
		one.moveTo(-30.0, -30.0);
		one.lineTo(30.0, -30.0);
		one.lineTo(71.72995780590716, 78.05907172995781);
		one.lineTo(-51.68776371308019, 79.1139240506329);
		Path2D two = new Path2D.Double();
		two.moveTo(-24.14953415699774, 34.882660448996724);
		two.lineTo(-34.882660448996724, -24.14953415699774);
		two.lineTo(63.9685269512598, -84.53657838986543);
		two.lineTo(87.08399760473866, 36.70170646613579);
		assertEquals(259.6952, ShapeComparator.getRotationFrom(one, two, true));
	}
}
