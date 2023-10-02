package com.example.moneykeeper.presenter.interfaces

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {

    fun onSwiped(viewHolder: RecyclerView.ViewHolder)
}