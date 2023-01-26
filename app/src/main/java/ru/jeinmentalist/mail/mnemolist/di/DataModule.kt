package ru.jeinmentalist.mail.mnemolist.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.jeinmentalist.mail.data.db.MnemoListDatabase
import ru.jeinmentalist.mail.data.db.dao.NoteDao
import ru.jeinmentalist.mail.data.db.dao.ProfileDao
import ru.jeinmentalist.mail.data.db.dao.TimestampDao
import ru.jeinmentalist.mail.data.db.dbForMobile
import ru.jeinmentalist.mail.data.note.NoteRepositoryImpl
import ru.jeinmentalist.mail.data.profile.ProfileRepositoryImpl
import ru.jeinmentalist.mail.data.timestamp.TimestampRepositoryImpl
import ru.jeinmentalist.mail.domain.note.INoteRepository
import ru.jeinmentalist.mail.domain.note.noteUseCase.*
import ru.jeinmentalist.mail.domain.profile.IProfileRepository
import ru.jeinmentalist.mail.domain.profile.profileUseCase.ChangeCompletedEntriesUseCase
import ru.jeinmentalist.mail.domain.profile.profileUseCase.ChangeRunningEntriesUseCase
import ru.jeinmentalist.mail.domain.profile.profileUseCase.GetProfileByIdUseCase
import ru.jeinmentalist.mail.domain.timestamp.ITimestampRepository
import ru.jeinmentalist.mail.domain.timestamp.timstampUseCase.LoadTimestampListUseCase
import ru.jeinmentalist.mail.mnemolist.background.reminder.IReminderManager
import ru.jeinmentalist.mail.mnemolist.background.reminder.RemindManagerOnWorkManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideMnemoListDatabase(@ApplicationContext appContext: Context): MnemoListDatabase {
        val database = Room.databaseBuilder(
            appContext,
                MnemoListDatabase::class.java,
                dbForMobile
            )
//            .addCallback(MnemoListDatabase.DB_CALLBACK)
                .build()
           return database
    }

    @Provides
    fun provideAddNote(repository: INoteRepository): AddNoteUseCase{
        return AddNoteUseCase(repository)
    }

    @Provides
    fun provideGetNoteId(repository: INoteRepository): GetNoteByIdUseCase{
        return GetNoteByIdUseCase(repository)
    }

    @Provides
    fun provideCheckNoteForExistenceUseCase(repository: INoteRepository): CheckNoteForExistenceUseCase{
        return CheckNoteForExistenceUseCase(repository)
    }

    @Provides
    fun provideChangeCompletedEntries(repository: IProfileRepository): ChangeCompletedEntriesUseCase{
        return ChangeCompletedEntriesUseCase(repository)
    }

    @Provides
    fun provideGetProfileById(repository: IProfileRepository): GetProfileByIdUseCase {
        return GetProfileByIdUseCase(repository)
    }

    @Provides
    fun provideGetPerformedNotes(repository: INoteRepository): GetPerformedNotesUseCase{
        return GetPerformedNotesUseCase(repository)
    }

    @Provides
    fun provideUpdateNote(repository: INoteRepository): UpdateNoteNextTimestampUseCase{
        return UpdateNoteNextTimestampUseCase(repository)
    }

    @Provides
    fun provideUpdateNoteState(repository: INoteRepository): UpdateNoteStateUseCase{
        return UpdateNoteStateUseCase(repository)
    }

    @Provides
    fun provideGetTimestamps(repository: ITimestampRepository): LoadTimestampListUseCase{
        return LoadTimestampListUseCase(repository)
    }

    @Provides
    fun provideTimestampRepository(dao: TimestampDao): ITimestampRepository{
        return TimestampRepositoryImpl(dao)
    }

    @Provides
    fun provideProfileRepository(dao: ProfileDao): IProfileRepository{
        return ProfileRepositoryImpl(dao)
    }

    @Provides
    fun provideNoteRepository(dao: NoteDao): INoteRepository{
        return NoteRepositoryImpl(dao)
    }

    @Provides
    fun provideNoteDao(database: MnemoListDatabase): NoteDao{
        return database.noteDao()
    }

    @Provides
    fun provideProfileDao(database: MnemoListDatabase): ProfileDao{
        return database.profileDao()
    }

    @Provides
    fun provideTimestampDao(database: MnemoListDatabase): TimestampDao{
        return database.timestampDao()
    }

    @Provides
    fun provideRemindManager(getNote: GetNoteByIdUseCase,
                             updateNote: UpdateNoteNextTimestampUseCase,
                             updateState: UpdateNoteStateUseCase): IReminderManager{
        return RemindManagerOnWorkManager(getNote, updateNote, updateState)
    }

}