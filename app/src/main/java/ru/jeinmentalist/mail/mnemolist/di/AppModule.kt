package ru.jeinmentalist.mail.mnemolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.jeinmentalist.mail.domain.profile.profileUseCase.GetProfileByIdUseCase
import ru.jeinmentalist.mail.mnemolist.screens.delegat.GatProfileImp
import ru.jeinmentalist.mail.mnemolist.screens.delegat.IGetProfile


@InstallIn(ViewModelComponent::class)
@Module
class AppModule {
//    @Provides
//    fun provideRemindManager(): IReminderManager{
//        return RemindManagerOnWorkMagager()
//    }
    @Provides
    fun provideGetAndSaveProfile(getProfileUC: GetProfileByIdUseCase): IGetProfile {
        return GatProfileImp(getProfileUC)
    }
}