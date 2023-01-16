package com.gsp.trusttags.mobile.mapping.networking;


import dagger.Subcomponent;

/**
 * A sub component to create ViewModels. It is called by the
 */
@Subcomponent
public interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }

}
