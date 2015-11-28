package pl.szymen.rxsample.api;

import java.util.List;

import pl.szymen.rxsample.data.model.Repo;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by szymen on 2015-11-25.
 */
public interface GithubService {

        @GET("users/{user}/repos")
        Observable<List<Repo>> getUserReposWithObservable(@Path("user") String user);
}
