package com.github.florent37.awesomebar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ActionMenuView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

/**
 * Created by florentchampigny on 29/01/2017.
 */

public class AwesomeBar extends FrameLayout {

    public static final int DELAYT_BEFORE_FIRST_MENU_ANIMATION = 1000;
    public static final int DELAY_BETWEEN_MENU_ANIMATION_AND_CLICK = 800;
    public static final int MESSAGE_ANIMATION_START = 1;
    public static final float RATIO_RADIUS_MIN_MAX = (3f / 5f);
    private final int finxRadius;
    private int radius;
    private int lightAnimateRadius = 0;
    private OnClickListener onMenuClickListener;
    private ActionItemClickListener actionItemClickListener;
    private OverflowActionItemClickListener overflowActionItemClickListener;
    private Paint paintDark;
    private Paint paintMain;
    private Settings settings;
    private int textSize = 0;

    private ImageView iconMenu;
    private ImageView iconBack;
    private ImageView iconApp;
    private ImageView iconAppBackground;
    private ViewGroup actionsLayout;
    private ViewGroup iconLayout;
    private LinearLayout actionLayout;
    private TextView titlebar;


    private int direction;

    private ActionMenuView actionMenuView;

    private Handler menuAnimationHandler = new MenuAnimationHandler();

    public AwesomeBar(Context context) {
        this(context, null);
    }

    public AwesomeBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AwesomeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        finxRadius = getResources().getDimensionPixelOffset(R.dimen.bar_radius);
        radius = finxRadius;

        setMinimumHeight(getResources().getDimensionPixelOffset(R.dimen.bar_min_height));

        settings = new Settings();
        settings.init(context, attrs);


        setWillNotDraw(false);
        paintDark = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDark.setColor(settings.getColorDark());
        paintDark.setStyle(Paint.Style.FILL_AND_STROKE);

        paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintMain.setColor(settings.getColorMain());
        paintMain.setStyle(Paint.Style.FILL_AND_STROKE);

        addView(LayoutInflater.from(context).inflate(R.layout.bar_layout, this, false));

        iconMenu = (ImageView) findViewById(R.id.bar_menu_icon);
        iconBack = (ImageView) findViewById(R.id.bar_back_icon);
        iconApp = (ImageView) findViewById(R.id.bar_app_icon);
        iconAppBackground = (ImageView) findViewById(R.id.bar_app_icon_background);
        actionsLayout = (ViewGroup) findViewById(R.id.bar_actions_layout);
        actionMenuView = (ActionMenuView) findViewById(R.id.bar_actions_menu_view);
        iconLayout = (FrameLayout) findViewById(R.id.bar_app_icon_layout);
        actionLayout = (LinearLayout) findViewById(R.id.actions_layout);
        titlebar = (TextView) findViewById(R.id.bar_app_text);

        iconMenu.setImageDrawable(AnimatedVectorDrawableCompat.create(getContext(), R.drawable.awsb_ic_menu_animated));

        iconApp.setImageDrawable(getAppIcon(context));
        iconAppBackground.setImageDrawable(getAppIcon(context));
        iconAppBackground.setColorFilter(Color.WHITE);

        if (settings.isAnimateMenu()) {
            menuAnimationHandler.sendEmptyMessageDelayed(MESSAGE_ANIMATION_START, DELAYT_BEFORE_FIRST_MENU_ANIMATION);
        }
        iconMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settings.isAnimateMenu()) {
                    animateMenuImage();
                    animateDarkView();
                    iconMenu.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (onMenuClickListener != null) {
                                onMenuClickListener.onClick(iconMenu);
                            }
                        }
                    }, DELAY_BETWEEN_MENU_ANIMATION_AND_CLICK);
                } else {
                    if (onMenuClickListener != null) {
                        onMenuClickListener.onClick(iconMenu);
                    }
                }
            }
        });

        DrawableCompat.setTint(actionMenuView.getOverflowIcon(), settings.getColorMain());
        actionMenuView.setVisibility(GONE);

        actionMenuView.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (overflowActionItemClickListener != null) {
                    overflowActionItemClickListener.onOverflowActionItemClicked(item.getOrder(), item.getTitle().toString());
                }
                return true;
            }
        });

        iconBack.setVisibility(GONE);
        iconBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMenuClickListener != null) {
                    onMenuClickListener.onClick(iconBack);
                }
            }
        });


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        textSize = titlebar.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        direction = ViewCompat.getLayoutDirection(this);
        int totalW = getMeasuredWidth();
        if (direction == ViewCompat.LAYOUT_DIRECTION_RTL) {
            LayoutParams params = (LayoutParams) iconMenu.getLayoutParams();
            params.leftMargin = (totalW - iconMenu.getWidth() - params.leftMargin);
            iconMenu.setLayoutParams(params);
            iconMenu.setRotation(180);
            iconBack.setRotation(180);


            LayoutParams backParams = (LayoutParams) iconBack.getLayoutParams();
            backParams.leftMargin = (totalW - iconBack.getWidth() - backParams.leftMargin);
            iconBack.setLayoutParams(backParams);


            LayoutParams iconParams = (LayoutParams) iconLayout.getLayoutParams();
            iconParams.leftMargin = (totalW - iconLayout.getWidth() - iconParams.leftMargin);
            iconLayout.setLayoutParams(iconParams);


            LayoutParams actionParams = (LayoutParams) actionLayout.getLayoutParams();
            actionParams.gravity = Gravity.START;
            actionLayout.setLayoutParams(actionParams);


        }


    }

    private void returnDarkToDefult() {
        titlebar.setText("");
        titlebar.setAlpha(0.0f);
        iconApp.setVisibility(View.VISIBLE);
        iconAppBackground.setVisibility(View.VISIBLE);


        final int duration = 300;
//        final int bestDuration = (finxRadius / radius) * duration;

        final ValueAnimator valueAnimator = ValueAnimator.ofInt(lightAnimateRadius, 0);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lightAnimateRadius = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }


    private void animateLightPart(String title) {

        if (title == null) return;

        iconApp.setVisibility(View.INVISIBLE);
        iconAppBackground.setVisibility(View.INVISIBLE);


        titlebar.setText(title);

        Paint paint = new Paint();
        paint.setTextSize(dp2px(20));
        float textsize = paint.measureText(title);


        final int newRad = (int) (textsize / 1.5f);


        final int duration = 400;

        final ValueAnimator valueAnimator = ValueAnimator.ofInt(lightAnimateRadius , newRad);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lightAnimateRadius = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                titlebar.setAlpha(1f);
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.start();


    }

    private void animateDarkView() {
        final int minAlpha = 100;
        final int duration = 200;
        final int delay = 350;

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, radius * RATIO_RADIUS_MIN_MAX);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animRadius = (float) animation.getAnimatedValue();
                paintDark.setAlpha(255 - (int) (minAlpha * animation.getAnimatedFraction()));
                postInvalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(radius * RATIO_RADIUS_MIN_MAX, 0);
                        valueAnimator2.setInterpolator(new AccelerateInterpolator());
                        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                animRadius = (float) animation.getAnimatedValue();
                                paintDark.setAlpha((255 - minAlpha) + (int) (minAlpha * animation.getAnimatedFraction()));
                                postInvalidate();
                            }
                        });
                        valueAnimator2.setDuration(duration);
                        valueAnimator2.start();
                    }
                }, delay);
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public void setIcon(final int iconId) {
        iconApp.setImageResource(iconId);
        iconAppBackground.setImageResource(iconId);
        iconAppBackground.setColorFilter(Color.WHITE);
    }

    private void animateMenuImage() {
        final Drawable drawable = iconMenu.getDrawable();
        if (drawable instanceof AnimatedVectorDrawableCompat) {
            ((AnimatedVectorDrawableCompat) iconMenu.getDrawable()).start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        menuAnimationHandler.removeMessages(MESSAGE_ANIMATION_START);
    }

    private float animRadius = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        final int cY = getHeight() / 2;

        if (direction == ViewCompat.LAYOUT_DIRECTION_RTL) {
            canvas.drawCircle(getMeasuredWidth(), cY, radius + lightAnimateRadius, paintMain);
            canvas.drawCircle(getMeasuredWidth() - (-radius * RATIO_RADIUS_MIN_MAX + animRadius), cY, radius, paintDark);
        } else {
            canvas.drawCircle(0, cY, radius + lightAnimateRadius, paintMain);
            canvas.drawCircle(-radius * RATIO_RADIUS_MIN_MAX + animRadius, cY, radius, paintDark);
        }

    }

    public Drawable getAppIcon(Context context) {
        return context.getPackageManager().getApplicationIcon(context.getApplicationInfo());
    }

    public void addAction(@Nullable @DrawableRes Integer drawable, String actionName) {
        final ActionItem actionItem = new ActionItem(getContext());
        actionItem.setText(actionName);
        actionItem.setDrawable(drawable);
        actionItem.setAnimateBeforeClick(settings.isAnimateMenu());
        actionItem.setBackgroundColor(settings.getActionsColor());
        this.actionsLayout.addView(actionItem);

        actionItem.setClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionItemClickListener != null) {
                    actionItemClickListener.onActionItemClicked(actionsLayout.indexOfChild(actionItem), actionItem);
                }
            }
        });

        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) actionItem.getLayoutParams();
        layoutParams.leftMargin = getResources().getDimensionPixelOffset(R.dimen.bar_actions_margin_left);
        layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.bar_actions_margin_right);
        actionItem.setLayoutParams(layoutParams);
    }

    public void clearActions() {
        this.actionsLayout.removeAllViews();
    }

    public void setOnMenuClickedListener(OnClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setActionItemClickListener(ActionItemClickListener actionItemClickListener) {
        this.actionItemClickListener = actionItemClickListener;
    }

    public void addOverflowItem(String item) {
        actionMenuView.getMenu().add(item);

        actionMenuView.setVisibility(VISIBLE);
    }

    public void cleanOverflowMenu() {
        actionMenuView.getMenu().clear();
    }

    public void setOverflowActionItemClickListener(OverflowActionItemClickListener overflowActionItemClickListener) {
        this.overflowActionItemClickListener = overflowActionItemClickListener;
    }

    public void displayHomeAsUpEnabled(boolean enabled, String title) {
        if (enabled) {
            this.iconBack.setVisibility(VISIBLE);
            this.iconMenu.setVisibility(GONE);
            animateLightPart(title);
        } else {
            returnDarkToDefult();
            this.iconBack.setVisibility(GONE);
            this.iconMenu.setVisibility(VISIBLE);
        }
    }

    public interface ActionItemClickListener {
        void onActionItemClicked(int position, ActionItem actionItem);
    }

    public interface OverflowActionItemClickListener {
        void onOverflowActionItemClicked(int position, String item);
    }

    class MenuAnimationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_ANIMATION_START:
                    final Drawable drawable = iconMenu.getDrawable();
                    if (drawable instanceof AnimatedVectorDrawableCompat) {
                        ((AnimatedVectorDrawableCompat) iconMenu.getDrawable()).start();
                        sendEmptyMessageDelayed(MESSAGE_ANIMATION_START, 5000);
                    }
                    break;
            }
        }
    }

    private float dp2px(int dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
