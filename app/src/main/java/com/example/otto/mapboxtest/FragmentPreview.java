package com.example.otto.mapboxtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FragmentPreview extends Fragment implements View.OnTouchListener, View.OnClickListener {
    private SurfaceHolder sfhTrackHolder;
    private SurfaceView drawSurface;
    private Canvas canvas;
    private ImageView iv;
    private ScaleGestureDetector mScaleDetector;

    private boolean first = true;
    private boolean drawn = false;
    private int radius = 100;
    private float circleCoordx;
    private float circleCoordy;
    private float x, y;
    private Paint paint = new Paint();
    private String mFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_preview, container, false);
        mScaleDetector = new ScaleGestureDetector(getActivity(), new simpleOnScaleGestureListener());

        drawSurface = (SurfaceView) v.findViewById(R.id.surface);
        drawSurface.setZOrderOnTop(true);
        sfhTrackHolder = drawSurface.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        Button btnConf = (Button)v.findViewById(R.id.btnConf);
        btnConf.setOnClickListener(this);

        iv = (ImageView)v.findViewById(R.id.latestPicture);
        iv.setOnTouchListener(this);
        //TODO: maybe change string on following line
        mFile = getActivity().getExternalFilesDir(null)+ "/pic.jpg";
        Bitmap bit = BitmapFactory.decodeFile(mFile);
        //Bitmap resized = Bitmap.createScaledBitmap(bit,  , true);
        iv.setImageBitmap(bit);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);
        paint.setColor(Color.WHITE);

        return v;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
          /*  if (event.getPointerCount() > 1) {
                Log.i("multi", " Touch: " + event);
                multi = true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.i("Tähän,", " jotain");
                multi = false;
                scaling = false;
            } */
        if (event.getAction() == MotionEvent.ACTION_MOVE ) {
            //These will get the location of the circle in percentage of screen
            x = event.getX() / iv.getWidth();
            y = event.getY() / iv.getHeight();
            if (first ||  circleCoordx - radius <= event.getX() && event.getX() <= circleCoordx + radius && circleCoordy - radius <= event.getY() && event.getY() <= circleCoordy + radius) {
                circleCoordx = event.getX();
                circleCoordy = event.getY();
                first = false;
            }

            canvas = sfhTrackHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawCircle(circleCoordx, circleCoordy, radius, paint);
            sfhTrackHolder.unlockCanvasAndPost(canvas);
            drawn = true;
        }
        return true;
    }

    //this combines the image and circle as one bitmap
    @Override
    public void onClick(View v) {
        if (drawn) {
            Bitmap bibb = BitmapFactory.decodeFile(mFile);
            canvas = sfhTrackHolder.lockCanvas();
            bibb = resize(bibb, bibb.getWidth(), bibb.getHeight());
            Bitmap bitmap = Bitmap.createBitmap( bibb.getWidth(), bibb.getHeight(), Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bitmap);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawBitmap(bibb , 0,0, null);
            canvas.drawCircle(bitmap.getWidth() * x, bitmap.getHeight() * y, radius, paint);
            sfhTrackHolder.unlockCanvasAndPost(canvas);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(getActivity().getExternalFilesDir(null), "pic2.jpg"));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Log.i("Tried to save", "saved?");
            } catch (FileNotFoundException e) {
                Log.e("erfile not found,", String.valueOf(e));
                e.printStackTrace();
            } finally {
                try {
                    Toast.makeText(getActivity(), "Saved to: " +fos,
                            Toast.LENGTH_SHORT).show();
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //This is used to resize the original image for editing
    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    //This class tracks the zooming/pinching gesture
    public class simpleOnScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.i("scaling", "..." + detector.getCurrentSpan());
            radius = (int) (radius * detector.getScaleFactor());
            return true;
        }
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            //scaling = true;
            return true;
        }
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    }
}
