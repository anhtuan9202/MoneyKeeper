package com.example.moneykeeper.presenter.setting

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentSettingBinding
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.category.view.CategoryFragment
import com.example.moneykeeper.presenter.category.viewmodel.CategoryViewModel
import com.example.moneykeeper.presenter.expense.viewmodel.ExpenseViewModel
import com.example.moneykeeper.presenter.setting.notification.NotificationFragment
import com.example.moneykeeper.presenter.utils.DateUtils
import com.example.moneykeeper.presenter.utils.PrefsUtils
import com.example.moneykeeper.presenter.utils.ResourceUtils.getDrawableResourceId
import com.example.moneykeeper.presenter.wallet.view.WalletFragment
import com.example.moneykeeper.presenter.wallet.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class FragmentSetting : BaseFragment<FragmentSettingBinding>() {

    private val expenseViewModel: ExpenseViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val walletViewModel: WalletViewModel by activityViewModels()
    override fun getLayout(container: ViewGroup?): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        expenseViewModel.getExpenses()


        binding.ivBack.setOnClickListener {
            callback.backToPrevious()
        }
        binding.layoutCategory.setOnClickListener {
            callback.showFragment(this::class.java, CategoryFragment::class.java,0,0,null,true)
        }
        binding.layoutWallet.setOnClickListener {
            callback.showFragment(this::class.java, WalletFragment::class.java,0,0,null,true)
        }
        binding.layoutNotification.setOnClickListener {
            callback.showFragment(this::class.java, NotificationFragment::class.java,0,0,null,true)
        }
        binding.layoutInterface.setOnClickListener {
            val list = arrayOf("Mặc định theo thiết bị", "Sáng", "Tối")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Chọn giao diện")
            var checkedItem  = 0
            when(AppCompatDelegate.getDefaultNightMode()){
                AppCompatDelegate.MODE_NIGHT_YES -> {
                    checkedItem = 2
                }
                AppCompatDelegate.MODE_NIGHT_NO -> {
                    checkedItem = 1
                }
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                    checkedItem = 0
                }
            }
            builder.setSingleChoiceItems(list,checkedItem) { _, which ->
                    when(which){
                        0 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            saveNightModePreference(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

                        }
                        1 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            saveNightModePreference(AppCompatDelegate.MODE_NIGHT_NO)


                        }
                        2 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            saveNightModePreference(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                    }

            }
            builder.setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }


        binding.layoutLanguage.setOnClickListener {
            var checkedItem  = 0
            when(getLocale()){
                "vi" -> {
                    checkedItem = 0
                }
                "en" -> {
                    checkedItem = 1
                }
            }
            val list = arrayOf("Tiếng Việt", "English")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Chọn ngôn ngữ")
            builder.setSingleChoiceItems(list,checkedItem) { dialog, which ->
                when(which){
                    0 -> {
                        requireActivity().recreate()
                        setLocale("vi")
                    }
                    1 -> {
                        requireActivity().recreate()
                        setLocale("en")
                    }
                }

            }
            builder.setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.layoutExcel.setOnClickListener {
            requestWritePermission()
        }
        binding.layoutCSV.setOnClickListener {
            notify(getString(R.string.feature_update))
        }
        binding.layoutPassword.setOnClickListener {
            notify(getString(R.string.feature_update))

        }


    }
    private fun saveNightModePreference(mode: Int) {
        PrefsUtils.initialize(requireContext())
        PrefsUtils.putInt("night_mode",mode)
    }



    private fun setLocale(lang: String){
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        requireActivity().resources.updateConfiguration(config,requireActivity().resources.displayMetrics)
        PrefsUtils.initialize(requireContext())
        PrefsUtils.putString("language",lang)
    }
    private fun getLocale(): String? {
        PrefsUtils.initialize(requireContext())
        return PrefsUtils.getString("language")
    }


    private fun exportExcel() {
        expenseViewModel.expensesLiveData.observe(this) { expenses ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val workbook = HSSFWorkbook()
                    val listRevenue = mutableListOf<Expense>()
                    val listExpense = mutableListOf<Expense>()

                    for (expense in expenses) {
                        if (expense.expType == 1) {
                            listRevenue.add(expense)
                        } else {
                            listExpense.add(expense)
                        }
                    }

                    val revenueSheet = workbook.createSheet("Doanh thu")
                    val headerRow = revenueSheet.createRow(0)
                    headerRow.createCell(0).setCellValue("Ngày")
                    headerRow.createCell(1).setCellValue("Số tiền")
                    headerRow.createCell(2).setCellValue("Ghi chú")
                    headerRow.createCell(3).setCellValue("Danh mục")
                    headerRow.createCell(4).setCellValue("Ví tiền")


                    for ((index, revenue) in listRevenue.withIndex()) {
                        val row = revenueSheet.createRow(index + 1)
                        row.createCell(0).setCellValue(DateUtils.formatDate(revenue.expDate))
                        row.createCell(1).setCellValue(revenue.expMoney)
                        row.createCell(2).setCellValue(revenue.expNote)

                        val category = categoryViewModel.getCategoryById(revenue.expCategory)
                        val wallet = walletViewModel.getWalletById(revenue.expWallet)
                        val categoryName = category.cateName
                        val walletName = wallet.walName

                        withContext(Dispatchers.Main) {
                            row.createCell(3).setCellValue(categoryName)
                            row.createCell(4).setCellValue(walletName)
                        }
                    }

                    val expenseSheet = workbook.createSheet("Chi phí")
                    val headerRow1 = expenseSheet.createRow(0)
                    headerRow1.createCell(0).setCellValue("Ngày")
                    headerRow1.createCell(1).setCellValue("Số tiền")
                    headerRow1.createCell(2).setCellValue("Ghi chú")
                    headerRow1.createCell(3).setCellValue("Danh mục")
                    headerRow1.createCell(4).setCellValue("Ví tiền")

                    for ((index, expense) in listExpense.withIndex()) {
                        val row = expenseSheet.createRow(index + 1)
                        row.createCell(0).setCellValue(DateUtils.formatDate(expense.expDate))
                        row.createCell(1).setCellValue(expense.expMoney)
                        row.createCell(2).setCellValue(expense.expNote)

                        val category = categoryViewModel.getCategoryById(expense.expCategory)
                        val wallet = walletViewModel.getWalletById(expense.expWallet)
                        val categoryName = category.cateName
                        val walletName = wallet.walName

                        withContext(Dispatchers.Main) {
                            row.createCell(3).setCellValue(categoryName)
                            row.createCell(4).setCellValue(walletName)
                        }
                    }

                    val date = SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(Date())

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val contentValues = ContentValues()
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$date.xlsx")
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.ms-excel")
                        val uri =
                            requireContext().contentResolver.insert(
                                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                                contentValues
                            )

                        uri?.let {
                            val outputStream =
                                requireContext().contentResolver.openOutputStream(uri as Uri)
                            workbook.write(outputStream)
                            outputStream?.close()
                            withContext(Dispatchers.Main) {
                                notify(getString(R.string.export_succ))

                            }

                        } ?: run {
                            withContext(Dispatchers.Main) {
                                notify(getString(R.string.export_fail))
                            }
                        }
                    } else {
                        val root =
                            File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                                "MoneyKeeper"
                            )
                        if (!root.exists()) {
                            root.mkdirs()
                        }
                        val file = File(root, "$date.xlsx")
                        val outputStream = FileOutputStream(file)
                        workbook.write(outputStream)
                        outputStream.close()
                        withContext(Dispatchers.Main) {
                            notify(getString(R.string.export_succ))

                        }
                    }
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    withContext(Dispatchers.Main) {
                        notify(getString(R.string.export_fail))


                    }
                }
            }
        }
    }




    private fun requestWritePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                101
            )
        } else {
            exportExcel()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportExcel()
            } else {
                notify(getString(R.string.permission_fail))
            }
        }
    }




}