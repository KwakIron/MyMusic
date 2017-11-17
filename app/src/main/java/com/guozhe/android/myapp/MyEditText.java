package com.guozhe.android.myapp;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by guozhe on 2017. 10. 26..
 */

public class MyEditText extends android.support.v7.widget.AppCompatAutoCompleteTextView {
    private Context context;
    private Drawable imgSearch;



    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    private void init(){
        imgSearch = context.getDrawable(R.drawable.nav_logout);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setDrawable();
            }
        });
    }
    private void setDrawable(){
        if(length()<1){
            setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }else {
            setCompoundDrawablesWithIntrinsicBounds(null,null,imgSearch,null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(imgSearch != null && event.getAction() == event.ACTION_UP){
            int eventX = (int)event.getRawX();
            int eventY = (int)event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 240;
            if(rect.contains(eventX,eventY)){
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

}
