package pl.szymen.rxsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.szymen.rxsample.api.ApiManager;
import retrofit.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by szymen on 2015-11-25.
 * Sample with RxJava, RxAndroid, RxBinding, Retrofit
 */

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.et_search_user) EditText mSearchUserEditText;
    @Bind(R.id.tv_results) TextView mResultsTextView;
    private static final String TAG = "RxSample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        ApiManager apiManager = new ApiManager();
        setupTextChangesBinding(apiManager);
    }

    private void setupTextChangesBinding(ApiManager apiManager) {
        RxTextView.textChanges(mSearchUserEditText)
                .debounce(Constants.DEBOUNCE, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .map(String::trim)
                .switchMap(username -> apiManager.getUserReposWithObservable(username)
                                .doOnError(this::showError)
                                .onErrorResumeNext(Observable.empty())
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateRepos, this::showError);
    }

    private void updateRepos(List repos) {
        mResultsTextView.setText(repos.toString());
    }

    private void showError(Throwable t) {
        Log.e(TAG, t.toString());

        String message = t.getMessage();
        if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            if (httpException.code() == 403)
                message = getString(R.string.error_limit_exceeded);
            else if (httpException.code() == 404)
                message = getString(R.string.error_invalid_user);
        }
        mResultsTextView.setText(message);
    }

}
