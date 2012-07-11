package mx.ferreyra.solomaneja.ui;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
 
public class RoutePathOverlay extends Overlay {
 
        private int _pathColor;
        private final List<GeoPoint> _points;
        private boolean _drawStartEnd;
		private boolean drawPath;
 
        public RoutePathOverlay(List<GeoPoint> points, boolean drawPath) {
                this(points, Color.RED, true);
                this.drawPath = drawPath;
        }
 
        public RoutePathOverlay(List<GeoPoint> points, int pathColor, boolean drawStartEnd) {
                _points = points;
                _pathColor = pathColor;
                _drawStartEnd = drawStartEnd;
        }
 
        private void drawOval(Canvas canvas, Paint paint, Point point) {
                Paint ovalPaint = new Paint(paint);
                ovalPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                ovalPaint.setStrokeWidth(2);
                int _radius = 6;
                RectF oval = new RectF(point.x - _radius, point.y - _radius, point.x + _radius, point.y + _radius);
                canvas.drawOval(oval, ovalPaint);               
        }
 
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
                Projection projection = mapView.getProjection();
                
                
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(_pathColor);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                paint.setAlpha(90);
                
                if (shadow == false && _points != null) {
                        Point startPoint = null, endPoint = null;
                        Path path = new Path();
                        //We are creating the path
                        for (int i = 0; i < _points.size(); i++) {
                                GeoPoint gPointA = _points.get(i);
                                Point pointA = new Point();
                                projection.toPixels(gPointA, pointA);
                                if (i == 0) { //This is the start point
                                        startPoint = pointA;
                                        if (drawPath)
                                        	path.moveTo(pointA.x, pointA.y);
                                        else 
                                        	drawOval(canvas, paint, pointA);
                                } else {
                                        if (i == _points.size() - 1)//This is the end point
                                                endPoint = pointA;
                                        if (drawPath)
                                        	path.lineTo(pointA.x, pointA.y);
                                        else
                                        	drawOval(canvas, paint, pointA);
                                }
                        }
 

                        if (getDrawStartEnd()) {
                                if (startPoint != null) {
                                        drawOval(canvas, paint, startPoint);
                                }
                                if (endPoint != null) {
                                        drawOval(canvas, paint, endPoint);
                                }
                        }
                        if (!path.isEmpty())
                                canvas.drawPath(path, paint);
                }
                return super.draw(canvas, mapView, shadow, when);
        }
 
        public boolean getDrawStartEnd() {
                return _drawStartEnd;
        }
 
        public void setDrawStartEnd(boolean markStartEnd) {
                _drawStartEnd = markStartEnd;
        }
}
