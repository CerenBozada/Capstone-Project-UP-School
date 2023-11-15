package com.example.applehub.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.applehub.data.model.entity.Favorite
import dagger.Binds
import javax.inject.Singleton


@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getAll(): List<Favorite>

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun getById(id: Int): Favorite

    @Query("DELETE FROM favorite")
    fun deleteAll()

    @Query("DELETE FROM favorite WHERE id = :id")
    fun deleteById(id: Int)

    @Insert
    fun insert(favorite: Favorite)
}

@Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}