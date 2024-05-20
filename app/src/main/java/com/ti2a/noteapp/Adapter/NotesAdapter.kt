package com.ti2a.noteapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ti2a.noteapp.MainActivity
import com.ti2a.noteapp.Models.Note
import com.ti2a.noteapp.R
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listener: MainActivity) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val NoteList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return  NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent, false)
        )
    }

    override fun getItemCount(): Int {
        return NoteList.size
    }

    fun UpdateList(newList : List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        NoteList.clear()
        NoteList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search : String){
        NoteList.clear()
        for (item in fullList){
            if(item.title.lowercase().contains(search.lowercase())==true ||
                item.note.lowercase().contains(search.lowercase())==true){

                NoteList.add(item)
            }
        }

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val curretNote = NoteList[position]
        holder.title.text= curretNote.title
        holder.title.isSelected= true

        holder.Note_tv.text= curretNote.note
        holder.date.text= curretNote.date
        holder.date.isSelected= true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(RandomColor(), null))

        holder.notes_layout.setOnClickListener{
            listener.onItemClicked(NoteList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener{
            listener.onLongItemClicked(NoteList[holder.adapterPosition],holder.notes_layout)
            true
        }
    }


    fun RandomColor() : Int {
        val list = arrayListOf<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)
        list.add(R.color.NoteColor7)
        list.add(R.color.NoteColor8)
        list.add(R.color.NoteColor9)
        list.add(R.color.NoteColor10)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    inner class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val Note_tv = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)

    }

    interface NotesClicklistener{
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)
    }
}