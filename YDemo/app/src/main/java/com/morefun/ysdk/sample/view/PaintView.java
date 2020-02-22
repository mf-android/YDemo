package com.morefun.ysdk.sample.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class PaintView extends View {

    final public static int MODE_NONE = 0;
    final public static int MODE_DRAW_LINE = 1;
    final public static int MODE_DRAW_CURVE = 2;
    final public static int MODE_ERASER_LINE = 3;
    final public static int MODE_ERASER_NORMAL = 4;
    final public static int MODE_LASER = 5;
    protected static final float TOUCH_TOLERANCE = 4;
    protected int DEFAULT_COLOR = Color.BLACK;

    public int mMode = MODE_DRAW_CURVE;
    protected Handler mHandler = null;
    protected float mX, mY;
    public int mPaintWidth = 4;
    public int paintColor;
    public int laserColor;
    public int highliterColor = 0x7Ff7f701;
    public int highliterWidth = 40;
    public int mEraseWidth = 30;
    private Context mContext = null;
    private float mZoomRatio = 1.0f;
    private float mDrawLineStartX = 0f;
    private float mDrawLineStartY = 0f;
    protected boolean mIsTouchDown = false;
    protected ArrayList<WBPath> mPathList;
    protected WBPath mPath = null;
    protected Paint mPaint;
    protected Paint mLaserPaint;
    protected Paint mEraserPaint;
    protected Paint mBmpPaint;
    protected float mDensity = 0;
    protected boolean mbEdit = false;
    protected Canvas mCanvas;
    protected Bitmap mCanvasBmp = null;
    protected int mBmpWidth = 0, mBmpHeight = 0;
    protected Matrix mParentMatrix = null;
    private float[] mMatrixValues = new float[9];

    private float mLasterX, mLasterY;
    private boolean mSartPath = false;
    private int mTouchCount = 0;
    private boolean bZoom = false;


    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public Bitmap getCanvasBitmap() {
        return mCanvasBmp;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0 && w != mBmpWidth && h != mBmpHeight) {
            mBmpWidth = w;
            mBmpHeight = h;
            if (mCanvasBmp == null || mCanvasBmp.isRecycled()) {
                try {
                    mCanvasBmp = Bitmap.createBitmap(mBmpWidth, mBmpHeight, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError e) {

                }
            } else {
                Bitmap sbmp = null;
                try {
                    sbmp = Bitmap.createBitmap(mBmpWidth, mBmpHeight, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError e) {
                }
                if (sbmp != null) {
                    Canvas canvas = new Canvas(sbmp);
                    RectF dst = new RectF(0, 0, mBmpWidth, mBmpHeight);
                    canvas.drawBitmap(mCanvasBmp, null, dst, null);
                    mCanvasBmp.recycle();
                    mCanvasBmp = sbmp;
                    canvas = null;
                }

            }
            if (mCanvasBmp != null && !mCanvasBmp.isRecycled()) {
                mCanvas = new Canvas(mCanvasBmp);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mCanvasBmp != null && !mCanvasBmp.isRecycled()) {
            canvas.drawBitmap(mCanvasBmp, 0, 0, mBmpPaint);
        }

        if (mMode == MODE_ERASER_NORMAL && mIsTouchDown) {
            canvas.drawCircle(mX, mY, mPaint.getStrokeWidth() / 2, mEraserPaint);
        }

    }

    protected void init(Context context) {
        mContext = context;

        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        mDensity = dm.density;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(getLineWidth());

        mBmpPaint = new Paint(Paint.DITHER_FLAG);

        setColor(DEFAULT_COLOR);


        mLaserPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLaserPaint.setAntiAlias(true);
        mLaserPaint.setDither(true);
        mLaserPaint.setStyle(Paint.Style.FILL);

        mEraserPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setDither(true);
        mEraserPaint.setColor(Color.BLACK);
        mEraserPaint.setStyle(Paint.Style.STROKE);

        mPathList = new ArrayList<WBPath>();
        setLaserColor(DEFAULT_COLOR);

        setLineWidth(2);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (mMode != MODE_NONE) {
            // postTouchEvent(event.getAction(), x, y);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mLasterX = x;
                    mLasterY = y;
                    mTouchCount = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (bZoom) {
                        return false;
                    } else {
                        if (!mSartPath
                                && (Math.abs(x - mLasterX) >= 5 || Math.abs(y - mLasterY) >= 5)) {
                            mSartPath = true;
                            touch_start(mLasterX, mLasterY);
                        } else if (mSartPath) {
                            mTouchCount++;
                            touch_move(x, y);
                        }
                        invalidate();

                        return true;
                    }
                case MotionEvent.ACTION_UP:

                    bZoom = false;
                    if (mSartPath) {
                        touch_move(x, y);
                        touch_up();
                        invalidate();
                        mSartPath = false;
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    bZoom = true;
                    return false;
                case MotionEvent.ACTION_POINTER_UP:
                    // bZoom = false;
                    return false;
            }

        } else {
            return super.dispatchTouchEvent(event);
        }

        if (bZoom) {
            return super.dispatchTouchEvent(event);
        } else {
            return true;
        }
    }

    private void touch_start(float x, float y) {
        mIsTouchDown = true;
        if ((mMode == MODE_DRAW_LINE || mMode == MODE_DRAW_CURVE || mMode == MODE_ERASER_NORMAL)) {
            if (mParentMatrix != null) {
                float ratio = getRatio(mParentMatrix);
                if (ratio > 1) {
                    ratio = 1 / ratio;
                    RectF rect = new RectF(0, 0, getWidth(), getHeight());
                    mParentMatrix.mapRect(rect);
                    x = (x - rect.left) * ratio;
                    y = (y - rect.top) * ratio;
                }
            }

            /*
             * new a path and path info add them to path list and info list
             */
            if (mPath == null) {
                mPath = new WBPath();
                mPath.mMode = mMode;
                if (mMode == MODE_DRAW_LINE) {
                    mPath.mColor = getHighliterColor();
                    mPath.mWidth = (int) getHighliterWidth();
                } else if (mMode == MODE_ERASER_NORMAL || mMode == MODE_ERASER_LINE) {
                    mPath.mColor = 0;
                    mPath.mWidth = getEraseWidth();
                } else {
                    mPath.mColor = getColor();
                    mPath.mWidth = (int) getLineWidth();
                }
                mPath.mZoomRatio = mZoomRatio;
                mPathList.add(mPath);
                mPath.mIndex = mPathList.size() - 1;
                mPath.reset();
                if (mHandler != null) {
                    Message msg = new Message();
                    msg.what = 101;
                    msg.arg1 = 0; // show undo
                    mHandler.sendMessage(msg);
                }
                deleteHidePath();
                mbEdit = true;
            }
            mPath.moveTo(x, y);

            if (mCanvas != null && mPath != null) {

                mCanvas.drawPath(mPath, mPaint);
            }

            if (mMode == MODE_DRAW_LINE) {
                mDrawLineStartX = x;
                mDrawLineStartY = y;
            }
        }
        mX = x;
        mY = y;

    }

    /**
     * if in crop mode, update crop position if in draw curve mode, add point to
     * path and point info to path info if in draw line mode, reset the path and
     * add point and point info
     */

    private void touch_move(float x, float y) {
        if (mParentMatrix != null) {
            float ratio = getRatio(mParentMatrix);
            if (ratio > 1) {
                ratio = 1 / ratio;
                RectF rect = new RectF(0, 0, getWidth(), getHeight());
                mParentMatrix.mapRect(rect);
                x = (x - rect.left) * ratio;
                y = (y - rect.top) * ratio;
            }
        }
        if (mMode == MODE_DRAW_CURVE || mMode == MODE_ERASER_NORMAL) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                if (mPath != null) {
                    mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                }
            }
        } else if (mMode == MODE_DRAW_LINE) {
            if (mPath != null) {
                mPath.reset();
                mPath.moveTo(mDrawLineStartX, mDrawLineStartY);
                mPath.lineTo(x, y);
            }

        } else if (mMode == MODE_LASER && mTouchCount % 3 == 0) {

        }
        if (mCanvas != null && mPath != null) {
            mCanvas.drawPath(mPath, mPaint);
        }
        mX = x;
        mY = y;

    }

    private void touch_up() {
        mIsTouchDown = false;
        if (mMode == MODE_LASER) {

        }
        if (mPath == null)
            return;
        if ((mMode == MODE_DRAW_LINE || mMode == MODE_DRAW_CURVE || mMode == MODE_ERASER_NORMAL)) {

            mPath = null;
        }

    }

    public void clearCanvas() {

        boolean bClean = false;

        if (mPathList.size() > 0) {
            mPathList.clear();
            bClean = true;
        }
        if (bClean) {
            reDrawAfterPathShowChange();
            Message msg = new Message();
            msg.what = 101;
            msg.arg1 = 1;
            if (mHandler != null) {
                mHandler.sendMessage(msg);
            }
            msg = new Message();
            msg.what = 101;
            msg.arg1 = 3;
            if (mHandler != null) {
                mHandler.sendMessage(msg);
            }
            mbEdit = false;
        }
        invalidate();

    }

    protected void reDrawAfterPathShowChange() {
        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
            mBmpWidth = getWidth();
            mBmpHeight = getHeight();
        }

        if (mCanvasBmp != null && !mCanvasBmp.isRecycled()) {
            mCanvasBmp.recycle();
        }
        mCanvasBmp = null;
        try {
            mCanvasBmp = Bitmap.createBitmap(mBmpWidth, mBmpHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
        }
        if (mCanvasBmp != null && !mCanvasBmp.isRecycled()) {
            Canvas canvas = new Canvas(mCanvasBmp);

            Paint paint = new Paint(mPaint);
            int size;
            /*
             * draw path list to paint view
             */
            size = mPathList.size();
            for (int i = 0; i < size; i++) {

                WBPath path = mPathList.get(i);
                if (!path.bRemove && path.bShow) {
                    setPaintMode(paint, path.mMode);
                    paint.setColor(path.mColor);
                    paint.setStrokeWidth(path.mWidth);
                    canvas.drawPath(mPathList.get(i), paint);
                }
            }
            // canvas.drawBitmap(mCanvasBmp, 0, 0, mBmpPaint);
            if (mCanvas == null) {
                mCanvas = new Canvas(mCanvasBmp);
            } else {
                mCanvas.setBitmap(mCanvasBmp);
            }

            invalidate();
        }
    }

    public void setPaintMode(Paint paint, int mode) {
        if (mode == MODE_ERASER_NORMAL || mode == MODE_ERASER_LINE) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            paint.setXfermode(null);
        }
    }


    private void deleteHidePath() {
        boolean bRemove = false;
        for (int i = 0; i < mPathList.size(); i++) {
            if (!mPathList.get(i).bShow) {
                mPathList.get(i).bRemove = true;
                bRemove = true;
            }
        }
        if (bRemove) {
            if (mHandler != null) {
                Message msg = new Message();
                msg.what = 101;
                msg.arg1 = 3;// hide redo
                mHandler.sendMessage(msg);
            }
        }
    }


    protected float getRatio(Matrix m) {
        if (m == null) {
            return 1;
        }
        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        m.mapRect(rect);
        float ratio = getScale(m);
        return ratio;
    }

    public float getScale(Matrix matrix) {
        return getValue(matrix, Matrix.MSCALE_X);
    }

    private float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    public void setColor(int color) {
        mPaint.setColor(color);
        paintColor = color;
    }

    public int getColor() {
        return paintColor;
    }


    public int getHighliterColor() {
        return highliterColor;
    }


    public int getHighliterWidth() {
        return highliterWidth;
    }

    public int getEraseWidth() {
        return (int) (mEraseWidth * getDensity() + 0.5f);
    }

    public void setLineWidth(int width) {
        mPaint.setStrokeWidth(width * getDensity());
        mPaintWidth = width;
    }

    public int getLineWidth() {
        return (int) (mPaintWidth * getDensity() + 0.1f);
    }

    public void setLaserColor(int color) {
        mLaserPaint.setColor(color);
        laserColor = color;
    }

    public float getDensity() {
        if (mDensity <= 0) {
            final WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            display.getMetrics(dm);
            mDensity = dm.density;
        }
        return mDensity;
    }

    public class WBPath extends Path {
        public int mMode = 0;
        public float mZoomRatio = 0.5f;
        public int mColor = 0;
        public int mWidth;
        public boolean bShow = true;
        public boolean bRemove = false;
        public int mIndex = -1;

        public WBPath() {
        }
    }

}
