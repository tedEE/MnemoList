package ru.jeinmentalist.mail.mnemolist.screens.createProfile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.mentalist.R

/////////// создание слушателя событий на item //////////////////////
interface ClickDelegate{
    fun delTimestamp(item: Timestamp, position: Int)
}
////////////////////////////////////////////////////////////////////
class TimeStampAdapter : RecyclerView.Adapter<TimeStampAdapter.TimeStampViewHolder>(){
    ///////////////////////////////////////////////////////////////////
    private var callback: ClickDelegate? = null

    fun attachCallback(callback: ClickDelegate){
        this.callback = callback
    }
    ///////////////////////////////////////////////////////////////////
    private var data: SortedList<Timestamp> = SortedList(Timestamp::class.java,
        object : SortedList.Callback<Timestamp>(){

            override fun compare(item1: Timestamp?, item2: Timestamp?): Int {
                return item1!!.executionTime.compareTo(item2!!.executionTime)
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: Timestamp?, newItem: Timestamp?): Boolean {
                return newItem!! == oldItem // newItem!!.equals(oldItem)
            }

            override fun areItemsTheSame(item1: Timestamp?, item2: Timestamp?): Boolean {
                return item2!!.executionTime.equals(item1)
            }

        })

    fun setData(items: List<Timestamp>){
        data.addAll(items)
    }

    fun getData(): List<Timestamp>{
        val list = mutableListOf<Timestamp>()
        for (i in 0 until data.size()){
            list.add(data[i])
        }
        return list
    }

    fun addItem(item: Timestamp){
        data.add(item)
    }

    fun clearItemList(){
        data.clear()
    }

    fun removeItemList(itemPosition: Int){
        data.removeItemAt(itemPosition)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimeStampViewHolder{
        return TimeStampViewHolder(item = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_teme_stamp, parent, false) , callback = callback)
    }

    override fun onBindViewHolder(holder: TimeStampViewHolder, position: Int) {
        holder.bind(data[position])
        holder.container.tag = position
    }

    override fun getItemCount(): Int {
        return data.size()
    }

    inner class TimeStampViewHolder(item: View, val callback: ClickDelegate?) : RecyclerView.ViewHolder(item){

        val intervals: TextView = item.findViewById(R.id.time_stamp_text)
        val container: LinearLayout = item.findViewById(R.id.time_stamp_container)

        fun bind(model: Timestamp) {
            intervals.text = "${ model.translationFromMilliseconds().first } ${ model.translationFromMilliseconds().second }"

/////////////////////////////////////////////////////////////////////
            container.setOnClickListener {
//                callback?.delTimespamp(model, it.tag as Int)
            }
//////////////////////////////////////////////////////////////////////
        }

    }

}