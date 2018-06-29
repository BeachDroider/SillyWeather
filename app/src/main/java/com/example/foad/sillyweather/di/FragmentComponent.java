package com.example.foad.sillyweather.di;

import com.example.foad.sillyweather.ui.weather.WeatherFragment;
import com.example.foad.sillyweather.ui.weather.WeatherViewModel;

import dagger.BindsInstance;
import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {FragmentModule.class})
public interface FragmentComponent {

    @Component.Builder
    interface Builder {

        FragmentComponent build();

        Builder appComponent(AppComponent appComponent);

        @BindsInstance
        Builder fragment(android.support.v4.app.Fragment fragment);
    }

//    void inject(WeatherFragment weatherFragment);
}
