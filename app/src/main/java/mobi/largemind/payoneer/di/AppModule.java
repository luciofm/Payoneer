package mobi.largemind.payoneer.di;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import mobi.largemind.payoneer.data.PaymentsRepository;
import mobi.largemind.payoneer.data.PaymentsService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return new OkHttpClient.Builder()
                .addInterceptor(logger)
                .build();
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://raw.githubusercontent.com/optile/checkout-android/develop/shared-test/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    @Provides
    PaymentsService providePaymentsService(Retrofit retrofit) {
        return retrofit.create(PaymentsService.class);
    }

    @Provides
    PaymentsRepository providesPaymentsRepository(PaymentsService service) {
        return new PaymentsRepository(service);
    }
}
