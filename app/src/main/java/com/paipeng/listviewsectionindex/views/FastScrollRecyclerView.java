package com.paipeng.listviewsectionindex.views;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
/**
 * Created by flaviusmester on 23/02/15.
 */
public class FastScrollRecyclerView extends RecyclerView{
    private final static String TAG = FastScrollRecyclerView.class.getSimpleName();
    private Context ctx;

    private boolean setupThings = false;
    public static int indWidth = 25;
    public static int indHeight= 18;
    public float scaledWidth;
    public float scaledHeight;
    public String[] sections;
    public float sx;
    public float sy;
    public String section;
    public boolean showLetter = false;
    private Handler listHandler;
    private int scrollDirection = 0;
    private int prePosition;

    public FastScrollRecyclerView(Context context) {
        super(context);
        ctx = context;
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ctx = context;
    }

    @Override
    public void onDraw(Canvas c) {
        if(!setupThings)
            setupThings();
        super.onDraw(c);
    }

    private void setupThings() {
        //create az text data
        Set<String> sectionSet = ((FastScrollRecyclerViewInterface)getAdapter()).getMapIndex().keySet();
        ArrayList<String> listSection = new ArrayList<>(sectionSet);
        Collections.sort(listSection);
        sections = new String[listSection.size()];
        int i=0;
        for(String s:listSection) {
            sections[i++] = s;
        }

        scaledWidth = indWidth * ctx.getResources().getDisplayMetrics().density;
        scaledHeight= indHeight* ctx.getResources().getDisplayMetrics().density;
        sx = this.getWidth() - this.getPaddingRight() - (float)(1.2*scaledWidth);
        sy = (float)((this.getHeight() - (scaledHeight * sections.length) )/2.0);
        setupThings = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (x < sx - scaledWidth || y < sy || y > (sy + scaledHeight*sections.length))
                    return super.onTouchEvent(event);
                else {
                    // We touched the index bar
                    float yy = y - this.getPaddingTop() - getPaddingBottom() - sy;
                    int currentPosition = (int) Math.floor(yy / scaledHeight);
                    if(currentPosition<0)currentPosition=0;
                    if(currentPosition>=sections.length)currentPosition=sections.length-1;
                    prePosition = currentPosition;
                    section = sections[currentPosition];
                    showLetter = true;
                    int positionInData = 0;

                    if (currentPosition> 0 ) {
                        if( ((FastScrollRecyclerViewInterface)getAdapter()).getMapIndex().containsKey(sections[currentPosition-1].toUpperCase()) )
                            positionInData = ((FastScrollRecyclerViewInterface)getAdapter()).getMapIndex().get(section.toUpperCase());

                    }
                    Log.i(TAG, "scroll to position " + positionInData);
                    this.scrollToPosition(positionInData);
                    FastScrollRecyclerView.this.invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                if (!showLetter && (x < sx  - scaledWidth || y < sy || y > (sy + scaledHeight*sections.length)))
                    return super.onTouchEvent(event);
                else {
                    float yy = y - sy;

                    int currentPosition = (int) Math.floor(yy / scaledHeight);

                    if (prePosition > currentPosition) {
                        scrollDirection = 1;
                    } else {
                        scrollDirection = 0;
                    }

                    prePosition = currentPosition;

                    if(currentPosition<0)
                        currentPosition=0;
                    if(currentPosition>=sections.length)
                        currentPosition=sections.length-1;

                    int positionInData = 0;
                    section = sections[currentPosition];
                    showLetter = true;


                    if ((currentPosition-scrollDirection) >= 0 ) {
                        String preSection = sections[currentPosition-scrollDirection];
                        if(((FastScrollRecyclerViewInterface)getAdapter()).getMapIndex().containsKey(preSection.toUpperCase()) )
                            positionInData = ((FastScrollRecyclerViewInterface)getAdapter()).getMapIndex().get(preSection.toUpperCase());

                    }
                    Log.i(TAG, "direction " + scrollDirection + " move " + section +  " current position " + currentPosition + " scroll to position " + positionInData);
                    this.scrollToPosition(positionInData);
                    FastScrollRecyclerView.this.invalidate();
                    break;
                }


            }
            case MotionEvent.ACTION_UP: {
                listHandler = new ListHandler();
                listHandler.sendEmptyMessageDelayed(0, 100);
                if (x < sx - scaledWidth || y < sy || y > (sy + scaledHeight*sections.length))
                    return super.onTouchEvent(event);
                else
                    return true;
//                break;
            }
        }
        return true;
    }

    private class ListHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showLetter = false;
            FastScrollRecyclerView.this.invalidate();
        }


    }

}
