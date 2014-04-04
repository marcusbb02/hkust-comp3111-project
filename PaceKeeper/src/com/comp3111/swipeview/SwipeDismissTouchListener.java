package com.comp3111.swipeview;

import static com.nineoldandroids.view.ViewHelper.setAlpha;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * A {@link android.view.View.OnTouchListener} that makes any {@link View}
 * dismissable when the user swipes (drags her finger) horizontally across the
 * view.
 * 
 * <p>
 * <em>For {@link android.widget.ListView} list items that don't manage their own touch events
 * (i.e. you're using
 * {@link android.widget.ListView#setOnItemClickListener(android.widget.AdapterView.OnItemClickListener)}
 * or an equivalent listener on {@link android.app.ListActivity} or
 * {@link android.app.ListFragment}, use {@link SwipeDismissListViewTouchListener} instead.</em>
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * view.setOnTouchListener(new SwipeDismissTouchListener(view, null, // Optional
 * 																	// token/cookie
 * 																	// object
 * 		new SwipeDismissTouchListener.OnDismissCallback() {
 * 			public void onDismiss(View view, Object token) {
 * 				parent.removeView(view);
 * 			}
 * 		}));
 * </pre>
 * 
 * <p>
 * This class Requires API level 12 or later due to use of
 * {@link android.view.ViewPropertyAnimator}.
 * </p>
 * 
 * @see SwipeDismissListViewTouchListener
 */
public class SwipeDismissTouchListener implements View.OnTouchListener {
	// Cached ViewConfiguration and system-wide constant values
	private int mSlop;
	private int mMinFlingVelocity;
	private int mMaxFlingVelocity;
	private long mAnimationTime;

	// Fixed properties
	private View mView;
	private OnDismissCallback mCallback;
	private int mViewWidth = 1; // 1 and not 0 to prevent dividing by zero
	private int screenH, actionBarHeight;

	// Transient properties
	private float mDownY;
	private boolean mSwiping;
	private Object mToken;
	private VelocityTracker mVelocityTracker;
	private float mTranslationY;

	/**
	 * The callback interface used by {@link SwipeDismissTouchListener} to
	 * inform its client about a successful dismissal of the view for which it
	 * was created.
	 */
	public interface OnDismissCallback {
		/**
		 * Called when the user has indicated they she would like to dismiss the
		 * view.
		 * 
		 * @param view
		 *            The originating {@link View} to be dismissed.
		 * @param token
		 *            The optional token passed to this object's constructor.
		 */
		void onDismiss(View view, Object token);
	}

	/**
	 * Constructs a new swipe-to-dismiss touch listener for the given view.
	 * 
	 * @param view
	 *            The view to make dismissable.
	 * @param token
	 *            An optional token/cookie object to be passed through to the
	 *            callback.
	 * @param callback
	 *            The callback to trigger when the user has indicated that she
	 *            would like to dismiss this view.
	 */
	public SwipeDismissTouchListener(View view, int abHeight, Object token, OnDismissCallback callback) {
		ViewConfiguration vc = ViewConfiguration.get(view.getContext());
		mSlop = vc.getScaledTouchSlop();
		mMinFlingVelocity = vc.getScaledMinimumFlingVelocity()*2;
		mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
		mAnimationTime = view.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
		mView = view;
		mToken = token;
		mCallback = callback;
		actionBarHeight = abHeight;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		// offset because the view is translated during swipe
		motionEvent.offsetLocation(mTranslationY, 0);

		if (mViewWidth < 2) {
			mViewWidth = mView.getWidth();
		}
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        screenH = dm.heightPixels;

		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_DOWN: {
			Log.i("Motion","ACTION_DOWN");
			// TODO: ensure this is a finger, and set a flag
			mDownY = motionEvent.getRawY();
			mVelocityTracker = VelocityTracker.obtain();
			mVelocityTracker.addMovement(motionEvent);
			view.onTouchEvent(motionEvent);
			return true;
		}

		case MotionEvent.ACTION_UP: {
			Log.i("Motion","ACTION_UP");
			if (mVelocityTracker == null) {
				break;
			}

			float deltaY = motionEvent.getRawY() - mDownY;
			mVelocityTracker.addMovement(motionEvent);
			mVelocityTracker.computeCurrentVelocity(1000);
			float velocityX = Math.abs(mVelocityTracker.getXVelocity());
			float velocityY = Math.abs(mVelocityTracker.getYVelocity());
			boolean dismiss = false;
			boolean dismissRight = false;
			if (deltaY > mView.getHeight() / 2) {
				dismiss = true;
				// TODO: fix the below line
			} else if (mMinFlingVelocity <= velocityX && velocityX <= mMaxFlingVelocity && velocityY < velocityX) {
				dismiss = true;
			}
			if (dismiss) {
				// dismiss
				animate(mView).translationY(screenH - actionBarHeight - view.getHeight()).setDuration(mAnimationTime).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animator arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animator arg0) {
						performDismiss();
					}

					@Override
					public void onAnimationCancel(Animator arg0) {
						// TODO Auto-generated method stub

					}
				});
			} else {
				// cancel
				animate(mView).translationY(0).alpha(1).setDuration(mAnimationTime).setListener(null);

			}
			mVelocityTracker = null;
			mTranslationY = 0;
			mDownY = 0;
			mSwiping = false;
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			Log.i("Motion","ACTION_MOVE");
			if (mVelocityTracker == null) {
				break;
			}

			mVelocityTracker.addMovement(motionEvent);
			float deltaY = motionEvent.getRawY() - mDownY;
			if (deltaY > mSlop) {
				mSwiping = true;
				mView.getParent().requestDisallowInterceptTouchEvent(true);

				// Cancel listview's touch
				MotionEvent cancelEvent = MotionEvent.obtain(motionEvent);
				cancelEvent.setAction(MotionEvent.ACTION_CANCEL | (motionEvent.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
				mView.onTouchEvent(cancelEvent);
			}

			if (mSwiping) {
				mTranslationY = deltaY;
				ViewHelper.setTranslationY(mView, deltaY);
				// TODO: use an ease-out interpolator or such
				//setAlpha(mView, Math.max(0f, Math.min(1f, 1f - 2f * Math.abs(deltaX) / mViewWidth / 1.2f)));
				return true;
			}
			break;
		}
		}
		return false;
	}

	private void performDismiss() {
		// Animate the dismissed view to zero-height and then fire the dismiss
		// callback.
		// This triggers layout on each animation frame; in the future we may
		// want to do something
		// smarter and more performance.
		mCallback.onDismiss(mView, mToken);
	}
}