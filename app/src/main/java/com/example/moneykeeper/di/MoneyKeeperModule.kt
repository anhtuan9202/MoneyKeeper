package com.example.moneykeeper.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.moneykeeper.data.local.Database
import com.example.moneykeeper.data.repository.CategoryRepositoryImpl
import com.example.moneykeeper.data.repository.ExpenseRepositoryImpl
import com.example.moneykeeper.data.repository.WalletRepositoryImpl
import com.example.moneykeeper.domain.repository.CategoryRepository
import com.example.moneykeeper.domain.repository.ExpenseRepository
import com.example.moneykeeper.domain.repository.WalletRepository
import com.example.moneykeeper.domain.use_cases.*
import com.example.moneykeeper.domain.use_cases.category.CategoryUseCases
import com.example.moneykeeper.domain.use_cases.category.GetCategories
import com.example.moneykeeper.domain.use_cases.category.GetCategoryById
import com.example.moneykeeper.domain.use_cases.expense.*
import com.example.moneykeeper.domain.use_cases.wallet.DeleteWallet
import com.example.moneykeeper.domain.use_cases.wallet.GetWalletById
import com.example.moneykeeper.domain.use_cases.wallet.GetWallets
import com.example.moneykeeper.domain.use_cases.wallet.InsertWallet
import com.example.moneykeeper.domain.use_cases.wallet.UpdateWallet
import com.example.moneykeeper.domain.use_cases.wallet.WalletUseCases
import com.example.moneykeeper.presenter.CategoryAdapter
import com.example.moneykeeper.presenter.viewmodel.CategoryViewModel
import com.example.moneykeeper.presenter.ExpenseAdapter
import com.example.moneykeeper.presenter.WalletAdapter
import com.example.moneykeeper.presenter.WalletImageAdapter
import com.example.moneykeeper.presenter.viewmodel.WalletViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoneyKeeperModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): Database {
        return Room.databaseBuilder(
            app,Database::class.java,Database.DATABASE_NAME
        ).createFromAsset("database/MoneyManagement.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(db:Database): CategoryRepository {
        return CategoryRepositoryImpl(db.categoryDao)
    }

    @Provides
    @Singleton
    fun provideCategoryUseCases(repository: CategoryRepository): CategoryUseCases {
        return CategoryUseCases(
            getCategories = GetCategories(repository),
            getCategoryById = GetCategoryById(repository),
            getExpenses =  com.example.moneykeeper.domain.use_cases.category.GetExpenses(repository),
            getRevenues = com.example.moneykeeper.domain.use_cases.category.GetRevenues(repository)
        )
    }


    @Provides
    @Singleton
    fun provideCategoryViewModel(categoryUseCases: CategoryUseCases): CategoryViewModel {
        return CategoryViewModel(categoryUseCases)
    }



    @Provides
    @Singleton
    fun provideExpenseCategory(db: Database): ExpenseRepository {
        return ExpenseRepositoryImpl(db.expenseDao)
    }

    @Provides
    @Singleton
    fun provideExpenseUseCases(repository: ExpenseRepository): ExpenseUseCases {
        return ExpenseUseCases(
            getExpenses = GetExpenses(repository),
            insertExpense = InsertExpense(repository),
            deleteExpense = DeleteExpense(repository),
            updateExpense = UpdateExpense(repository),
            getExpensesForMonth = GetExpensesForMonth(repository),
            getExpensesForDay = GetExpensesForDay(repository),
            getExpensesForYear = GetExpensesForYear(repository),
            get5Expenses = Get5Expenses(repository)
        )
    }


    @Provides
    @Singleton
    fun provideWalletCategory(db: Database): WalletRepository {
        return WalletRepositoryImpl(db.walletDao)
    }


    @Provides
    @Singleton
    fun provideWalletUseCases(repository: WalletRepository): WalletUseCases {
        return WalletUseCases(
            getWallets = GetWallets(repository),
            updateWallet = UpdateWallet(repository),
            getWalletById = GetWalletById(repository),
            insertWallet = InsertWallet(repository),
            deleteWallet = DeleteWallet(repository)
        )
    }

    @Provides
    @Singleton
    fun provideWalletViewModel(walletUseCases: WalletUseCases): WalletViewModel {
        return WalletViewModel(walletUseCases)
    }






    @Provides
    @Singleton
    fun provideExpenseAdapter(
        categoryViewModel: CategoryViewModel,
        walletViewModel: WalletViewModel,
        @ApplicationContext context: Context
    ): ExpenseAdapter {
        return ExpenseAdapter(categoryViewModel,walletViewModel, context)
    }

    @Provides
    @Singleton
    fun provideCategoryAdapter(
        @ApplicationContext context: Context
    ): CategoryAdapter {
        return CategoryAdapter(context)
    }

    @Provides
    @Singleton
    fun provideWalletAdapter(
        @ApplicationContext context: Context
    ): WalletAdapter {
        return WalletAdapter(context)
    }

    @Provides
    @Singleton
    fun provideWalletImageAdapter(
        @ApplicationContext context: Context
    ): WalletImageAdapter {
        return WalletImageAdapter(context)
    }


}