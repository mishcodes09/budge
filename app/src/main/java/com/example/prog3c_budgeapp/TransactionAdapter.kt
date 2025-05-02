package vcmsa.projects.prog7313budgetapp

import android.view.LayoutInflater
import android.view.SurfaceControl
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prog3c_budgeapp.R

class TransactionAdapter(
    private val transactions: MutableList<Transaction>,
    private val onClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storeNameText: TextView = itemView.findViewById(R.id.storeNameText)
        val amountText: TextView = itemView.findViewById(R.id.amountText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.storeNameText.text = transaction.storeName
        holder.amountText.text = "R${String.format("%.2f", transaction.amount)}"
        holder.dateText.text = transaction.date

        holder.itemView.setOnClickListener {
            onClick(transaction)
        }

    }

    override fun getItemCount() = transactions.size
}
