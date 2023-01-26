package ru.jeinmentalist.mail.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import ru.jeinmentalist.mail.data.db.dao.NoteDao
import ru.jeinmentalist.mail.data.db.dao.ProfileDao
import ru.jeinmentalist.mail.data.db.dao.TimestampDao
import ru.jeinmentalist.mail.data.db.model.NoteEntity
import ru.jeinmentalist.mail.data.db.model.ProfileEntity
import ru.jeinmentalist.mail.data.db.model.TimestampEntity

const val dbForMobile = "mnemo_list_database"

@Database(
    entities = [NoteEntity::class, ProfileEntity::class, TimestampEntity::class],
    version = 2,
    exportSchema = true
)
abstract class MnemoListDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun profileDao(): ProfileDao
    abstract fun timestampDao(): TimestampDao

    companion object{
//        val DB_CALLBACK = object : RoomDatabase.Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                db.execSQL(
//                    """
//                    CREATE TRIGGER insert_entries_trigger AFTER INSERT
//                        ON notes_table
//                        BEGIN
//                            UPDATE profile_table
//	                        SET running_entries = running_entries + 1 WHERE profile_id = NEW.prof_id;
//                        END;
//                """.trimIndent()
//                )
//
//                db.execSQL(
//                    """
//                    CREATE TRIGGER update_entries_trigger AFTER UPDATE
//                        OF state
//                        ON notes_table
//                        BEGIN
//                            UPDATE profile_table
//	                        SET completed_entries = completed_entries + 1,
//		                        running_entries = running_entries - 1
//	                        WHERE profile_id = NEW.prof_id;
//                        END;
//                """.trimIndent()
//                )
//            }
//        }
    }

//    companion object {
//        @Volatile
//        private var database: MnemoListDatabase? = null
//
//        @Synchronized
//        fun getInstance(context: Context): MnemoListDatabase {
//            return if (database == null) {
//                database = Room.databaseBuilder(
//                    context,
//                    MnemoListDatabase::class.java,
//                    dbForMobile
//                )
//                    .build()
//                database as MnemoListDatabase
//            } else {
//                database as MnemoListDatabase
//            }
//        }
//    }
}