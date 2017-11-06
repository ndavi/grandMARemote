package fr.octopus.grandmaremote.Activities;
        import android.annotation.TargetApi;
        import android.content.Context;
        import android.os.Build;
        import android.support.annotation.Nullable;
        import android.util.AttributeSet;
        import android.view.GestureDetector;
        import android.view.MotionEvent;
        import android.view.ScaleGestureDetector;
        import android.view.View;
        import android.widget.RelativeLayout;

        import java.util.HashMap;
        import java.util.Map;

/**
 * Created by Alexander Thomas (@Cawfree) on 20/07/2017.
 */

/** Enables users to customize Regions Of Interest on a Canvas. */
public class MovableActivity extends RelativeLayout implements View.OnTouchListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {

    /* Member Variables. */
    private final GestureDetector      mGestureDetector;
    private final ScaleGestureDetector mScaleGestureDetector;
    private final Map<Integer, View>   mViewMap;
    private       boolean              mScaling;
    private       float                mScale;
    private       boolean              mWrapContent;
    private       boolean              mDropOnScale;

    public MovableActivity(Context context) {
        // Implement the Parent.
        super(context);
        // Initialize Member Variables.
        this.mGestureDetector      = new GestureDetector(context, this);
        this.mViewMap              = new HashMap<>();
        this.mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.mScaling              = false;
        this.mScale                = Float.NaN;
        this.mWrapContent          = false;
        this.mDropOnScale          = false;
        // Register ourself as the OnTouchListener.
        this.setOnTouchListener(this);
    }

    public MovableActivity(Context context, @Nullable AttributeSet attrs) {
        // Implement the Parent.
        super(context, attrs);
        // Initialize Member Variables.
        this.mGestureDetector      = new GestureDetector(context, this);
        this.mViewMap              = new HashMap<>();
        this.mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.mScaling              = false;
        this.mWrapContent          = false;
        this.mDropOnScale          = false;
        // Register ourself as the OnTouchListener.
        this.setOnTouchListener(this);
    }

    public MovableActivity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        // Implement the Parent.
        super(context, attrs, defStyleAttr);
        // Initialize Member Variables.
        this.mGestureDetector      = new GestureDetector(context, this);
        this.mViewMap              = new HashMap<>();
        this.mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.mScaling              = false;
        this.mWrapContent          = false;
        this.mDropOnScale          = false;
        // Register ourself as the OnTouchListener.
        this.setOnTouchListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MovableActivity(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        // Implement the Parent.
        super(context, attrs, defStyleAttr, defStyleRes);
        // Initialize Member Variables.
        this.mGestureDetector      = new GestureDetector(context, this);
        this.mViewMap              = new HashMap<>();
        this.mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.mScaling              = false;
        this.mWrapContent          = false;
        this.mDropOnScale          = false;
        // Register ourself as the OnTouchListener.
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        // Calculate the PointerId.
        final int lPointerId = event.getPointerId(event.getActionIndex());
        // Handle the TouchEvent.
        this.getGestureDetector().onTouchEvent(event);
        this.getScaleGestureDetector().onTouchEvent(event);
        // Did the user release a pointer?
        if(event.getAction() == MotionEvent.ACTION_UP) {
            // Was there a View associated with this Action?
            final View lView = this.getViewMap().get(lPointerId);
            // Does the View exist?
            if(lView != null) {
                // Remove the View from the Map.
                this.getViewMap().remove(lPointerId); /** TODO: Provide a Callback? */
                if(lView instanceof SelectableImageView) {
                    SelectableImageView image = (SelectableImageView) lView;
                    image.selectedItem(image);
                }
            }
        }
        // Consume all events for now.
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // Calculate the PointerId.
        final Integer lPointerId = Integer.valueOf(e.getPointerId(e.getActionIndex()));
        // Fetch the View.
        final View    lView      = this.getViewFor(Math.round(e.getRawX()), Math.round(e.getRawY()));
        // Is it valid?
        if(lView != null) {
            // Watch the View.
            this.getViewMap().put(lPointerId, lView);
            // Configure the Anchor.
            lView.setPivotX(0);
            lView.setPivotY(0);
            // Assert that we handled the event.
            return true;
        }
        // Assert that we ignored the event.
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // Are we not scaling?
        if(!this.isScaling()) {
            // Calculate the PointerId.
            final Integer lPointerId = Integer.valueOf(e1.getPointerId(e1.getActionIndex()));
            // Fetch the View.
            final View    lView      = this.getViewMap().get(lPointerId);
            // Is the scroll valid for a given View?
            if(lView != null) {
                // Calculate the Scaled Width and Height of the View.
                final float lWidth    = lView.getWidth()  * lView.getScaleX();
                final float lHeight   = lView.getHeight() * lView.getScaleY();
                // Declare the initial position.
                final int[] lPosition = new int[] { (int)(e2.getX() - ((lWidth)  / 2)), (int)(e2.getY() - ((lHeight) / 2)) };
                // Are we wrapping content?
                if(this.isWrapContent()) {
                    // Wrap the Position.
                    this.onWrapContent(lPosition, lWidth, lHeight);
                }
                // Update the Drag.
                this.onUpdateDrag(lView, lPosition);
            }
            // Assert we handled the scroll.
            return true;
        }
        // Otherwise, don't permit scrolling. Don't consume the MotionEvent.
        return false;
    }

    /** Forces X/Y values to be coerced within the confines of the RegionView. */
    private final void onWrapContent(final int[] pPosition, final float pWidth, final float pHeight) {
        // Limit the parameters. (Top-Left)
        pPosition[0] = Math.max(pPosition[0], 0);
        pPosition[1] = Math.max(pPosition[1],  0);
        // Limit the parameters. (Bottom-Right)
        pPosition[0] = Math.min(pPosition[0], (int)(this.getWidth()  - pWidth));
        pPosition[1] = Math.min(pPosition[1], (int)(this.getHeight() - pHeight));
    }

    /** Updates the Drag Position of a child View within the Layout. Implicitly, we update the LayoutParams of the View. */
    private final void onUpdateDrag(final View pView, final int pLeft, final int pTop) {
        // Allocate some new MarginLayoutParams.
        final MarginLayoutParams lMarginLayoutParams = new MarginLayoutParams(pView.getLayoutParams());
        // Update the Margin.
        lMarginLayoutParams.setMargins(pLeft, pTop, 0, 0);
        // Refactor the MarginLayoutParams into equivalent LayoutParams for the RelativeLayout.
        pView.setLayoutParams(new RelativeLayout.LayoutParams(lMarginLayoutParams));
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // Calculate the ScaleFactor.
        float lScaleFactor = detector.getScaleFactor() - 1;
        // Fetch the Scaled View.
        final View  lView        = this.getViewMap().entrySet().iterator().next().getValue();
        // Update the ScaleFactor.
        final float lScale       = this.getScale() + lScaleFactor;
        // Calculate the Proposed Width and Height.
        final int   lWidth  = Math.round(lView.getWidth()  * lScale);
        final int   lHeight = Math.round(lView.getHeight() * lScale);
        // Is the View already too large for wrap content?
        if(lWidth >= this.getWidth() || lHeight >= this.getHeight()) {
            // Don't update the scale.
            return false;
        }
        // Persist this Scale for the View.
        lView.setScaleX(lScale);
        lView.setScaleY(lScale);
        // Assign the Scale.
        this.setScale(lScale);
        // Compute the Position.
        final int[] lPosition = new int[] { Math.round(detector.getFocusX()) - (lWidth / 2), Math.round(detector.getFocusY()) - (lHeight / 2) };
        // Are we wrapping the Position?
        if(this.isWrapContent()) {
            // Wrap the Position.
            this.onWrapContent(lPosition, lWidth, lHeight);
        }
        // Update the Drag.
        this.onUpdateDrag(lView, lPosition);
        // Assert that we handled the scale.
        return true;
    }

    /** Update the Drag. */
    private final void onUpdateDrag(final View pView, final int[] pPosition) {
        // Call the sub-implementation.
        this.onUpdateDrag(pView, pPosition[0], pPosition[1]);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // Is the user not dragging at all?
        if(this.getViewMap().size() == 1) {
            // Fetch the View.
            final View lView = this.getViewMap().entrySet().iterator().next().getValue();
            // Initialize the Scale.
            this.setScale(lView.getScaleX());
            // Assert that we've started scaling.
            this.setScaling(true);
            // Inform the callback.
            return true;
        }
        // Otherwise, don't allow scaling.
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        // Were we scaling?
        if(this.isScaling()) {
            // Assert that we've stopped scaling.
            this.setScaling(false);
            // Reset the Scale.
            this.setScale(Float.NaN);
            // Should we stop dragging now that we've finished scaling?
            if(this.isDropOnScale()) {
                // Clear the ViewMap.
                this.getViewMap().clear();
            }
        }
    }

    /** Returns the View colliding with the given co-ordinates. */
    private final View getViewFor(final int pX, final int pY) {
        // Declare the LocationBuffer.
        final int[] lLocationBuffer = new int[2];
        // Iterate the Views.
        for(int i = 0; i < this.getChildCount(); i++) {
            // Fetch the child View.
            final View lView = this.getChildAt(i);
            // Fetch its absolute position.
            lView.getLocationOnScreen(lLocationBuffer);
            // Determine if the MotionEvent collides with the View.
            if(pX > lLocationBuffer[0] && pY > lLocationBuffer[1] && (pX < lLocationBuffer[0] + (lView.getWidth() * lView.getScaleX())) && (pY < lLocationBuffer[1] + (lView.getHeight() * lView.getScaleY()))) {
                // Return the View.
                return lView;
            }
        }
        // We couldn't find a View.
        return null;
    }

    /* Unused Overrides. */
    @Override public void      onShowPress(MotionEvent e) {  }
    @Override public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    @Override public void      onLongPress(MotionEvent e) { }
    @Override public boolean       onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { return false; }

    /* Getters and Setters. */
    private final GestureDetector getGestureDetector() {
        return this.mGestureDetector;
    }

    private final ScaleGestureDetector getScaleGestureDetector() {
        return this.mScaleGestureDetector;
    }

    private final Map<Integer, View> getViewMap() {
        return this.mViewMap;
    }

    private final void setScaling(final boolean pIsScaling) {
        this.mScaling = pIsScaling;
    }

    private final boolean isScaling() {
        return this.mScaling;
    }

    private final void setScale(final float pScale) {
        this.mScale = pScale;
    }

    private final float getScale() {
        return this.mScale;
    }

    /** Defines whether we coerce the drag and zoom of child Views within the confines of the Layout. */
    public final void setWrapContent(final boolean pIsWrapContent) {
        this.mWrapContent = pIsWrapContent;
    }

    public final boolean isWrapContent() {
        return this.mWrapContent;
    }

    /** Defines whether a drag operation is considered 'finished' once the user finishes scaling a view. */
    public final void setDropOnScale(final boolean pIsDropOnScale) {
        this.mDropOnScale = pIsDropOnScale;
    }

    public final boolean isDropOnScale() {
        return this.mDropOnScale;
    }

}
