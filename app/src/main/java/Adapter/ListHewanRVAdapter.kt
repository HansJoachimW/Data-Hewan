package Adapter

import Interface.CardListener
import Model.Hewan
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hewanlist.R
import com.example.hewanlist.databinding.HewanCardBinding

class ListHewanRVAdapter(val listHewan: ArrayList<Hewan>, val cardListener: CardListener):
    RecyclerView.Adapter<ListHewanRVAdapter.viewHolder>() {

    class viewHolder(itemView: View, val cardListener1: CardListener): RecyclerView.ViewHolder(itemView) {
        val binding = HewanCardBinding.bind(itemView)

        fun setData(data:Hewan){
            binding.animalName.text=data.name

            if(!data.imageUri!!.isEmpty()){
                binding.animalImage.setImageURI(Uri.parse(data.imageUri))
            }
            itemView.setOnClickListener{
                cardListener1.onCardClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.hewan_card, parent, false)
        return viewHolder(view, cardListener)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.setData(listHewan[position])
    }

    override fun getItemCount(): Int {
        return listHewan.size
    }
}