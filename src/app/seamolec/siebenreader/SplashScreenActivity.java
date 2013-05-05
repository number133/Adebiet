package app.seamolec.siebenreader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Animation.AnimationListener;
import android.widget.TableLayout;
import app.seamolec.siebenreader.view.DashboardActivity;

public class SplashScreenActivity extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen_layout);
        startAnimating();
    }
    
    private void startAnimating() {
        // Fade in top title
    	TableLayout logo1 = (TableLayout) findViewById(R.id.tableLayoutTop);
        Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo1.startAnimation(fade1);
        
        // Fade in bottom title 
        TableLayout logo2 = (TableLayout) findViewById(R.id.tableLayoutBottom);
        Animation fade2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo2.startAnimation(fade2);
        
        // Transition to Main Menu
        fade2.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
            	try {
        			Thread.sleep(1000);
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            	// The animation has ended
            	SplashScreenActivity.this.finish();
                startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        
        // Load animations for all views
        Animation spinin = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        LayoutAnimationController controller = new LayoutAnimationController(spinin);
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        table.setLayoutAnimation(controller);
        
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        // Stop the animation
        TableLayout logo1 = (TableLayout) findViewById(R.id.tableLayoutTop);
        logo1.clearAnimation();
        TableLayout logo2 = (TableLayout) findViewById(R.id.tableLayoutBottom);
        logo2.clearAnimation();
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        table.clearAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Start the animation
        startAnimating();
    }
    
}
