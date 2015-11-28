package pl.szymen.rxsample.api;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.List;

import pl.szymen.rxsample.Constants;
import pl.szymen.rxsample.data.model.Repo;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by szymen on 2015-11-25.
 */
public class ApiManager {

    private GithubService mGithubService;

    public ApiManager() {
        mGithubService = getRetrofit().create(GithubService.class);
    }

    private Retrofit getRetrofit() {
        OkHttpClient okhttp = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttp.interceptors().add(interceptor);

        return new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(okhttp)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public Observable<List<Repo>> getUserReposWithObservable(String username) {
        return mGithubService.getUserReposWithObservable(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
