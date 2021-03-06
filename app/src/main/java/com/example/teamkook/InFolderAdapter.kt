package com.example.teamkook

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_post.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException

class InFolderAdapter (val context: Context, var items:ArrayList<FolderMoreInfo>, var ID : String): RecyclerView.Adapter<InFolderAdapter.ViewHolder>() {



    var itemClickListener:OnItemClickListener ?= null
    interface OnItemClickListener{
        fun onItemClick(view : View, position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.row_in_folder, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //썸네일
        var id1 : String = items[position].link!!.substring(items[position].link!!.lastIndexOf("=")+1)
        var id2 :String= items[position].link!!.substring(items[position].link!!.lastIndexOf("/")+1)
        var id = id1;
        if(id2.length < id1.length)
            id = id2;
        Log.i("파싱한 아이디값", id);
        var url1 : String = "https://img.youtube.com/vi/"+id+"/0.jpg"
        Glide.with(holder.image.context).load(url1).into(holder.image)

        //타이틀 파싱

        holder.title.text = items[position].title


        //이 게시물에 가장 최근 댓글
        val rdatabase = FirebaseDatabase.getInstance().getReference("Review").child("link").child(id)
        val query = rdatabase.orderByChild("id").equalTo(ID)//////////////
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                //Log.i("단계", "1")
                if(snapshot.exists()){
                    //Log.i("단계", "2")
                    for(snap in snapshot.children){
                        val content = snap.child("content").getValue().toString()
                        Log.i("content", content)

                        holder.content.text = content

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })



    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image : ImageView
        var title : TextView
        var content : TextView
        init {
           image = itemView.findViewById(R.id.in_folder_image)
            title = itemView.findViewById(R.id.in_folder_title)
            content = itemView.findViewById(R.id.in_folder_content)
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(itemView, adapterPosition)
            }
        }
    }
}