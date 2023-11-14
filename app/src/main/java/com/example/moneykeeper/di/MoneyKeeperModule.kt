package com.example.moneykeeper.di


import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.moneykeeper.data.local.Database
import com.example.moneykeeper.data.repository.BudgetRepositoryImpl
import com.example.moneykeeper.data.repository.CategoryRepositoryImpl
import com.example.moneykeeper.data.repository.ExpenseRepositoryImpl
import com.example.moneykeeper.data.repository.WalletRepositoryImpl
import com.example.moneykeeper.domain.repository.BudgetRepository
import com.example.moneykeeper.domain.repository.CategoryRepository
import com.example.moneykeeper.domain.repository.ExpenseRepository
import com.example.moneykeeper.domain.repository.WalletRepository
import com.example.moneykeeper.domain.use_cases.*
import com.example.moneykeeper.domain.use_cases.budget.BudgetUseCases
import com.example.moneykeeper.domain.use_cases.budget.DeleteBudget
import com.example.moneykeeper.domain.use_cases.budget.Get5Budget
import com.example.moneykeeper.domain.use_cases.budget.GetBudgetForMonthAndYear
import com.example.moneykeeper.domain.use_cases.budget.GetBudgets
import com.example.moneykeeper.domain.use_cases.budget.InsertBudget
import com.example.moneykeeper.domain.use_cases.budget.UpdateBudget
import com.example.moneykeeper.domain.use_cases.category.CategoryUseCases
import com.example.moneykeeper.domain.use_cases.category.DeleteCategory
import com.example.moneykeeper.domain.use_cases.category.GetCategories
import com.example.moneykeeper.domain.use_cases.category.GetCategoryById
import com.example.moneykeeper.domain.use_cases.category.InsertCategory
import com.example.moneykeeper.domain.use_cases.expense.*
import com.example.moneykeeper.domain.use_cases.wallet.DeleteWallet
import com.example.moneykeeper.domain.use_cases.wallet.GetWalletById
import com.example.moneykeeper.domain.use_cases.wallet.GetWallets
import com.example.moneykeeper.domain.use_cases.wallet.InsertWallet
import com.example.moneykeeper.domain.use_cases.wallet.UpdateWallet
import com.example.moneykeeper.domain.use_cases.wallet.WalletUseCases
import com.example.moneykeeper.presenter.budget.adapter.BudgetAdapter
import com.example.moneykeeper.presenter.category.adapter.CategoryAdapter
import com.example.moneykeeper.presenter.category.adapter.CategoryImageAdapter
import com.example.moneykeeper.presenter.category.viewmodel.CategoryViewModel
import com.example.moneykeeper.presenter.expense.adapter.ExpenseAdapter
import com.example.moneykeeper.presenter.wallet.adapter.WalletAdapter
import com.example.moneykeeper.presenter.wallet.adapter.WalletImageAdapter
import com.example.moneykeeper.presenter.budget.viewmodel.BudgetViewModel
import com.example.moneykeeper.presenter.expense.viewmodel.ExpenseViewModel
import com.example.moneykeeper.presenter.wallet.viewmodel.WalletViewModel
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
            getRevenues = com.example.moneykeeper.domain.use_cases.category.GetRevenues(repository),
            insertCategory = InsertCategory(repository),
            deleteCategory = DeleteCategory(repository)
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
    fun provideBudgetCategory(db: Database): BudgetRepository{
        return BudgetRepositoryImpl(db.budgetDao)
    }


    @Provides
    @Singleton
    fun provideBudgetUseCases(repository: BudgetRepository): BudgetUseCases {
        return BudgetUseCases(
            getBudgets = GetBudgets(repository),
            insertBudget = InsertBudget(repository),
            deleteBudget = DeleteBudget(repository),
            updateBudget = UpdateBudget(repository),
            getBudgetForMonthAndYear = GetBudgetForMonthAndYear(repository),
            get5Budget = Get5Budget(repository)
        )
    }

    @Provides
    @Singleton
    fun provideBudgetViewModel(budgetUseCases: BudgetUseCases): BudgetViewModel {
        return BudgetViewModel(budgetUseCases)
    }


    @Provides
    @Singleton
    fun provideExpenseViewModel(expenseUseCases: ExpenseUseCases): ExpenseViewModel {
        return ExpenseViewModel(expenseUseCases)
    }





    @Provides
    @Singleton
    fun provideBudgetAdapter(
        categoryViewModel: CategoryViewModel,
        expenseViewModel: ExpenseViewModel,
        @ApplicationContext context: Context
    ): BudgetAdapter {
        return BudgetAdapter(categoryViewModel,expenseViewModel, context)
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
    @Provides
    @Singleton
    fun provideCategoryImageAdapter(
        @ApplicationContext context: Context
    ): CategoryImageAdapter {
        return CategoryImageAdapter(context)
    }




}