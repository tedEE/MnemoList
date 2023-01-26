package ru.jeinmentalist.mail.mnemolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.note.noteUseCase.*
import ru.jeinmentalist.mail.domain.profile.IProfileRepository
import ru.jeinmentalist.mail.domain.profile.profileUseCase.*
import ru.jeinmentalist.mail.domain.timestamp.ITimestampRepository
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.AddTimestampsListUseCase
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.LoadTimestampListUseCase
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.TimestampsUseCases
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.UpdateTimestampUseCase

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideNoteUseCases(repository: INoteRepository): NoteUseCases {
        return NoteUseCases(
            AddNoteUseCase(repository),
            GetNoteByIdUseCase(repository),
            GetNoteByProfileIdUseCase(repository),
            GetNoteListUseCase(repository),
            UpdateNoteNextTimestampUseCase(repository),
            DeleteNoteUseCase(repository),
            GetNotesFlowUseCase(repository)
        )
    }

    @Provides
    fun provideProfileUseCases(repository: IProfileRepository): ProfileUseCases {
        return ProfileUseCases(
            AddProfileUseCase(repository),
            GetProfileByIdUseCase(repository),
            GetProfileListFlowUseCase(repository),
            GetProfileListUseCase(repository),
            RemoveProfileUseCase(repository),
            ChangeRunningEntriesUseCase(repository)
        )
    }

    @Provides
    fun provideTimestampsUseCases(repository: ITimestampRepository): TimestampsUseCases {
        return TimestampsUseCases(
            AddTimestampsListUseCase(repository),
            LoadTimestampListUseCase(repository),
            UpdateTimestampUseCase(repository),
        )
    }

    @Provides
    fun provideDecrementCounterCompletedEntries(repository: IProfileRepository): DecrementCounterCompletedEntries {
        return DecrementCounterCompletedEntries(repository)
    }

    @Provides
    fun provideDecrementCounterRunningEntries(repository: IProfileRepository): DecrementCounterRunningEntries {
        return DecrementCounterRunningEntries(repository)
    }
}