package solutions.alterego.androidbound.android;

import com.alterego.advancedandroidlogger.implementations.NullAndroidLogger;
import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import solutions.alterego.androidbound.ViewModel;
import solutions.alterego.androidbound.android.interfaces.IBindableView;

@Accessors(prefix = "m")
public abstract class BindingAppCompatActivity extends AppCompatActivity implements IBindableView {

    @Setter
    protected IAndroidLogger mLogger = NullAndroidLogger.instance;

    @Getter
    @Setter
    private ViewModel mViewModel;

    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getViewBinder() != null) {
            getViewBinder().clearBindingForViewAndChildren(mView);
        }
        if (getViewModel() != null) {
            getViewModel().dispose();
            setViewModel(null);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mViewModel == null) {
            throw new RuntimeException("call setViewModel(Object) before calling setContentView!");
        }

        if (getViewBinder() == null) {
            throw new RuntimeException("getViewBinder must not be null!");
        }

        mView = getViewBinder().inflate(this, mViewModel, layoutResID, null);
        setContentView(mView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getViewModel() != null) {
            getViewModel().onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getViewModel() != null) {
            getViewModel().onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getViewModel() != null) {
            getViewModel().onSaveInstanceState(outState);
        }
    }
}